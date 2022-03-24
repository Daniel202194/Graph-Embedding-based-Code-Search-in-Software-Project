from abc import ABC, abstractmethod


class IResult(ABC):
    @abstractmethod
    def add_vertex(self, vertex:Vertex, rank=0)->None:
        pass

    @abstractmethod
    def add_edge(self, edge:Edge)->None:
        pass

    @abstractmethod
    def get_rank(self)->float:
        pass