from abc import ABC

from BeamSearch.Interfaces.IEdge import IEdge
from BeamSearch.Interfaces.IResult import IResult
from BeamSearch.Interfaces.IVertex import IVertex


class IGraph(ABC):
    def search(self) -> IResult:
        pass

    def bfs(self, goal, start):
        pass

    def get_vertices(self) ->list:
        pass

    def get_edges(self) ->list:
        pass

    def add_edge(self,edge:IEdge) -> None:
        pass

    def add_vertex(self, vertex: IVertex) -> None:
        pass
