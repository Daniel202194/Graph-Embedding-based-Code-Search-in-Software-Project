from abc import ABC, abstractmethod
from BeamSearch.Interfaces.IEdge import IEdge
from BeamSearch.Interfaces.IResult import IResult
from BeamSearch.Interfaces.IVertex import IVertex


class IGraph(ABC):
    @abstractmethod
    def bfs(self, goal: IVertex, start: IVertex):
        pass

    @abstractmethod
    def get_vertices(self) ->list:
        pass

    @abstractmethod
    def get_edges(self) ->list:
        pass

    @abstractmethod
    def add_edge(self,edge:IEdge) -> None:
        pass

    @abstractmethod
    def add_vertex(self, vertex: IVertex) -> None:
        pass
