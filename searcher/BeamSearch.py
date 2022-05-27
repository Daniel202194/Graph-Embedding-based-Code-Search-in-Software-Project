import copy
import string
# from searcher.model.WordEmbedding import WordEmbedding
from graph import Graph
from searcher.auxiliaries import aux1, aux2
from searcher.group import Group
from searcher.heap import Heap


def top(k, weights_map: dict) -> list:
    heap = Heap()  # MAX HEAP
    for item in weights_map:
        heap.push(weights_map[item], item)
    res = []
    while (min(k, heap.size) > 0):
        candidate = heap.pop()
        res.append(candidate[0])
        k -= 1

    if (k == 0 and heap.size > 0):
        last_rank = weights_map[res[-1]]
        while (heap.size > 0 and heap.top()[1] == last_rank):
            res.append(heap.pop()[0])
    return res


def top_groups(k, beam: list) -> list:
    heap = Heap(key=lambda x: -x)  # MIN HEAP
    for group in beam:
        heap.push(group.cost, group)
    res = []
    while (min(k, heap.size) > 0):
        candidate = heap.pop()
        res.append(candidate[0])
        last_rank = candidate[1]
        k -= 1

    if (k == 0 and heap.size > 0):
        while (heap.size > 0 and heap.top()[1] == last_rank):
            res.append(heap.pop()[0])
    return res


class BeamSearch():

    def __init__(self, graph: Graph):
        self.graph: Graph = graph
        # self.model = WordEmbedding(self.graph)
        # self.ranker = Ranker(self.model)

    def getDelta(self, key1, key2, weigths):
        dist = self.graph.vertexes_distance(key1, key2)
        w1 = weigths[key1]
        w2 = weigths[key2]
        delta = dist / ((w1 * w2) + 0.0001)
        return delta

    def generate_subgraph(self, k: int, candidates_by_token: dict, weights: dict):
        beam = []
        top_k = top(k, weights)
        # print("top_k:", top_k)
        for c in top_k:
            beam.append(Group(c))

        for Ci in candidates_by_token.values():
            new_beam = []
            for group in beam:
                for c_key in Ci:
                    delta = 0
                    for v_key in group.vertices:
                        delta += self.getDelta(c_key, v_key, weights)
                    cpy_group = copy.deepcopy(group)
                    cpy_group.add_vertex_key(c_key)
                    cpy_group.set_cost(cpy_group.cost + delta)
                    new_beam.append(cpy_group)
            beam = top_groups(k, new_beam)
        return top_groups(1, beam)

    def search(self, query: string, k: int = 2):
        cc = self.graph.get_candidates(query)
        # print("cc:",cc)

        candidates_by_token = aux1(cc)
        # print("candidates_by_token:", candidates_by_token)

        weights = aux2(self.graph.get_score_relevant(cc, query))
        # print("weights:",weights)

        groups = self.generate_subgraph(k, candidates_by_token, weights)
        # for group in groups:
        #     print(group)

        graph = Graph(vertexes={}, edges=set(), abb_dict={}, bow_vertex={}, word_vertex={}, names={}, name='',
                      vertex_vectors={})
        if len(groups) > 0:
            cost = groups[0].cost
            vertices_keys = groups[0].vertices
            # print("cost:", cost, "subgraph:", vertices_keys)
            graph = self.extend_vertex_set_to_connected_subgraph(vertices_keys)
        # print(graph.print())
        return graph

    def extend_vertex_set_to_connected_subgraph(self, vertices_keys):
        Y = vertices_keys
        E = set()
        V = set()
        E = set()
        while (Y.__len__() > 0):
            X = Y.pop()
            v, path = self.findShortestPath(X, Y)
            if path != None:
                for edge in path:
                    E.add(edge)
                    V.add(edge.in_v)
                    V.add(edge.out_v)
        # print("V:", V)
        # print("E:", E)
        graph = self.build_sub_graph(V, E)
        return graph

    def findShortestPath(self, X_key: int, Y: int):
        shortest_path = float('inf')
        path: list = None
        v: int = None

        for goal_key in Y:
            new_path: list = self.graph.bfs(goal_key, X_key)
            if new_path != None and len(new_path) < shortest_path:
                shortest_path = len(new_path)
                path = new_path
                v = goal_key
        # print("v:",v)
        # print("path:", path)
        return v, path

    def build_sub_graph(self, vertices: set, edges: set):
        g = Graph(vertexes={}, edges=set(), abb_dict={}, bow_vertex={}, word_vertex={}, names={}, name='',
                  vertex_vectors={})
        for v_key in vertices:
            g.add_vertex(v_key)
        for e in edges:
            g.add_edge(e)
        return g
