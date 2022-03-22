from result import Result

def search(self, k=1) -> Result:
    Ci: dict = self.generating_candidate_nodes()
    vertex_set = []
    for c in Ci.values():
        vertex_set.append(c[0])
    result: Result = self.extend_vertex_set_to_connected_subgraph(vertex_set)
    return result


def generating_candidate_nodes(self) -> dict:
    candidates_by_token = {}

    return candidates_by_token


def extend_vertex_set_to_connected_subgraph(self, vertex_set: list) -> Result:
    Y = vertex_set
    E = set()
    res = Result()
    while (Y.__len__() > 0):
        X = Y.pop()
        z = self.findShortestPath(X, Y)
        if z != None:
            for vertex in z.get_vertices():
                res.add_vertex(vertex)
            for edge in z.get_edges():
                res.add_edge(edge)
    return res


def findShortestPath(self, X: Vertex, Y: set) -> Graph:
    path_len = float('inf')
    path = None
    for goal in Y:
        new_path: Graph = self.graph.bfs(goal, X)
        if new_path == None:
            new_path = self.graph.bfs(X, goal)
        elif new_path != None and len(new_path) < path_len:
            path = new_path
    return path
    
    
    
def main():
query = Query("class list implements class iterable,class list contains class node")
# query.graph.draw()
graph = CodeParser('../../Files/codes/src1').graph
searcher = BeamSearch(graph, query)
searcher.search()
# searcher.model.db.print_table('src1')
# searcher.model.db.delete_db()


if __name__ == '__main__':
    main()
