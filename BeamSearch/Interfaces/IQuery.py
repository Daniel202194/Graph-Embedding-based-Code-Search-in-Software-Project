from abc import ABC, abstractmethod
from BeamSearch.Interfaces.IGraph import IGraph


class IQuery(ABC):
    @abstractmethod
    def get_tokens(self):
        pass

    @abstractmethod
    def build_graph(self)->IGraph:
        pass

