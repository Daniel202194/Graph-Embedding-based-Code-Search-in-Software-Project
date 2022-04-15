from abc import ABC, abstractmethod
from BeamSearch.Interfaces.IGraph import IGraph


class ISearcher(ABC):
    @abstractmethod
    def search(self,nodes_score_per_graph: dict) -> IGraph:
        pass
