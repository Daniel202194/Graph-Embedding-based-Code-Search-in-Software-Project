from BeamSearch.Interfaces.IResult import IResult
from Interfaces.IEdge import IEdge
from Interfaces.IGraph import IGraph
from Interfaces.IVertex import IVertex


class Result(IResult):

    def __init__(self):
        self.graph = IGraph()
        self.rank = 0.0

    def add_vertex(self, vertex:IVertex, rank=0)->None:
        self.graph.add_vertex(vertex)
        self.rank += rank

    def add_edge(self, edge:IEdge)->None:
        self.graph.add_edge(edge.type, edge.source, edge.to)

    def get_rank(self)->float:
        return self.rank

    def __str__(self):
        s = ''
        for vertex in self.graph.get_vertices():
            s+=str(vertex)+' '
        for edge in self.graph.get_edges():
            s+=str(edge)+' '
        return s


def main():
    res = Result()
    res.get_rank()
    res.add_vertex(Vertex(1,'v1',''))
    res.add_vertex(Vertex(2, 'v2', ''))
    print(res)


if __name__ == '__main__':
    main()
