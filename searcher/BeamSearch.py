import copy
from Interfaces.IGraph import IGraph
from Interfaces.IQuery import IQuery
from Interfaces.ISearcher import ISearcher
# from searcher.model.WordEmbedding import WordEmbedding
from searcher.group import Group
from searcher.heap import Heap


def top(k, weights_map: dict) -> list:
    heap = Heap() # MAX HEAP
    for item in weights_map:
        heap.push(weights_map[item],item)
    res = []
    while(min(k,heap.size)>0):
        candidate = heap.pop()
        res.append(candidate[0])
        k-=1
    return res

def top_groups(k, beam: list) -> list:
    heap = Heap(key=lambda x: -x) # MIN HEAP
    for group in beam:
        heap.push(group.cost, group)
    res = []
    while(min(k,heap.size)>0):
        candidate = heap.pop()
        res.append(candidate[0])
        k-=1
    return res


class BeamSearch(ISearcher):

    def __init__(self, graph):
        self.graph = graph
        # self.model = WordEmbedding(self.graph)
        # self.ranker = Ranker(self.model)

    def getDelta(self, key1, key2):
        return 1

    def generate_subgraph(self, k :int, candidates_by_token :dict, weights :dict) ->set:
        beam = []
        for c in top(k, weights):
            beam.append(Group(c))

        for Ci in candidates_by_token.values():
            new_beam = []
            for group in beam:
                for c_key in Ci:
                    delta = 0
                    for v_key in group.vertices:
                        delta += self.getDelta(c_key,v_key)
                    cpy_group = copy.deepcopy(group)
                    cpy_group.add_vertex_key(c_key)
                    cpy_group.set_cost(cpy_group.cost + delta)
                    new_beam.append(cpy_group)
            beam = top_groups(k, new_beam)
        return top_groups(1, beam)[0].vertices


############################################################################
    def aux1(self, candidates_by_token):
        res = {}
        for key in candidates_by_token.keys():
            res[key] = set()
            for val in candidates_by_token[key]:
                res[key].add(val.key)
        return res
    def aux2(self, weights):
        res = {}
        for v in weights.keys():
            res[v.key] = weights[v]
        return res
############################################################################

    def search(self, query, k :int=2):
        cc = self.graph.get_candidates(query)
        candidates_by_token = self.aux1(cc)
        print(candidates_by_token)

        weights = self.aux2(self.graph.get_score_relevant(cc, query))
        print(weights)

        vertices_keys = self.generate_subgraph(k, candidates_by_token, weights)
        print(vertices_keys)
        # graph = self.extend_vertex_set_to_connected_subgraph(vertices_keys)
        # return graph

    def dist(self, vertex1, vertex2) ->float:
        return self.model.euclid(vertex1, vertex2)

    def extend_vertex_set_to_connected_subgraph(self, vertices_keys) ->IGraph:
        Y = vertices_keys
        E = set()
        V = set()
        E = set()
        while(Y.__len__()>0):
            X = Y.pop()
            v, path = self.findShortestPath(X, Y)
            if path != None:
                for edge in path:
                    E.add(edge)
                    V.add(edge.source)
                    V.add(edge.to)
        graph: IGraph = self.build_sub_graph(V, E)
        return graph

    def findShortestPath(self, X_key :int, Y :int):
        shortest_path = float('inf')
        path :list = None
        v :int = None

        for goal_key in Y:
            dir1 :list = self.graph.bfs(goal_key, X_key)
            dir2: list = self.graph.bfs(X_key, goal_key)
            new_path = []
            if dir1!=None and dir2!=None:
                if len(dir1) < len(dir2):
                    new_path = dir1
                else:
                    new_path = dir2
            elif dir1!=None:
                new_path = dir1
            else:
                new_path = dir2

            if new_path != None and len(new_path) < shortest_path:
                shortest_path = len(new_path)
                path = new_path
                v = goal_key
        return v, path

    def build_sub_graph(self, vertices :set, edges :set) -> IGraph:
        g = IGraph()
        for v_key in vertices:
            g.add_vertex(self.graph.get_vertex(v_key))
        for e in edges:
            g.add_edge(e)
        return g

