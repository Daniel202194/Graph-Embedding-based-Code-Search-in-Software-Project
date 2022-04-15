from BeamSearch.group import Group
from BeamSearch.maxheap import MaxHeap
from Interfaces import IVertex
from Interfaces.IGraph import IGraph
from Interfaces.IQuery import IQuery
from Interfaces.ISearcher import ISearcher

def top(k, weights_map: dict) -> list:
    heap = MaxHeap()
    for item in weights_map:
        heap.push(weights_map[item],item)
    res = []
    while(min(k,heap.size)>0):
        candidate = heap.pop()
        res.append(candidate[0])
        k-=1
    return res

def build_sub_graph(vertices, edges) ->IGraph:
    g = IGraph()
    for v in vertices:
        g.add_vertex(v)
    for e in edges:
        g.add_edge(e)
    return g

def top_groups(k, beam: list) -> list:
    heap = MaxHeap()
    for group in beam:
        heap.push(group.cost, group)
    res = []
    while(min(k,heap.size)>0):
        candidate = heap.pop()
        res.append(candidate[0])
        k-=1
    return res


class BeamSearch(ISearcher):
    def __init__(self, graph:IGraph, query:IQuery):
        self.graph :IGraph = graph
        self.query :IGraph = query
        # self.model = WordEmbedding(Graph, 'src1')
        # self.ranker = IRanker(self.model)


    def generate_subgraph(self, k, candidates_by_token, weights):
        beam = []
        for c in top(k, weights):
            beam.append(Group(c))

        for group in beam:
            for Ci in candidates_by_token.values():
                group.select_candidate(Ci, self.model)

        beam = top_groups(k, beam)
        return top_groups(1, beam)[0].vertices

    def search(self, k=2):
        candidates_by_token, weights = self.__get_candidates()
        vertices = self.generate_subgraph(k, candidates_by_token, weights)
        graph :IGraph = self.extend_vertex_set_to_connected_subgraph(vertices)
        graph.draw()
        return graph

    def findShortestPath(self, X :IVertex, Y :set):
        shortest_path = float('inf')
        path :list = None
        v = None
        for goal in Y:
            new_path :list = self.graph.bfs(goal, X)
            if new_path == None: new_path = self.graph.bfs(X, goal)
            if new_path != None and len(new_path) < shortest_path:
                shortest_path = len(new_path)
                path = new_path
                v = goal
        return v, path

    def extend_vertex_set_to_connected_subgraph(self, vertex_set) ->IGraph:
        Y = vertex_set
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

        graph: IGraph = build_sub_graph(V, E)
        return graph


if __name__ == '__main__':
    query = Query("class list implements class iterable,class list contains class node")
    # query.graph.draw()
    graph = CodeParser('../../Files/codes/src1').graph
    searcher = BeamSearch(graph, query)
    searcher.search()
    # searcher.model.db.print_table('src1')
    # searcher.model.db.delete_db()