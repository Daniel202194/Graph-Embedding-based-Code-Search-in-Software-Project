from abc import ABC, abstractmethod
from BeamSearch.Interfaces.IEdge import IEdge
from BeamSearch.Interfaces.IVertex import IVertex


class IResult(ABC):
    @abstractmethod
    def add_vertex(self, vertex:IVertex, rank=0)->None:
        pass

    @abstractmethod
    def add_edge(self, edge:IEdge)->None:
        pass

    @abstractmethod
    def get_rank(self)->float:
        pass