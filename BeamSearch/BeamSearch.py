from Interfaces import IVertex
from Interfaces.IGraph import IGraph
from Interfaces.IModel import IModel
from Interfaces.IQuery import IQuery
from Interfaces.IRanker import IRanker
from Interfaces.IResult import IResult
from Interfaces.ISearcher import ISearcher


class BeamSearch(ISearcher):
    def __init__(self, graph: IGraph, query: IQuery):
        self.graph :IGraph = graph
        self.query :IQuery = query
        self.model :IModel = IModel
        self.ranker = IRanker(self.model)


    def search(self, k=1) -> IResult:
        Ci: dict = self.generating_candidate_nodes()
        vertex_set = []
        for c in Ci.values():
            vertex_set.append(c[0])
        result: IResult = self.extend_vertex_set_to_connected_subgraph(vertex_set)
        return result


    def generating_candidate_nodes(self) -> dict:
        pass


    def extend_vertex_set_to_connected_subgraph(self, vertex_set: list) -> IResult:
        Y = vertex_set
        E = set()
        res = IResult()
        while (Y.__len__() > 0):
            X = Y.pop()
            z :IGraph = self.findShortestPath(X, Y)
            if z != None:
                for vertex in z.get_vertices():
                    res.add_vertex(vertex)
                for edge in z.get_edges():
                    res.add_edge(edge)
        return res


    def findShortestPath(self, X: IVertex, Y: set) -> IGraph:
        path_len = float('inf')
        path = None
        for goal in Y:
            new_path: IGraph = self.graph.bfs(goal, X)
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
