from abc import ABC, abstractmethod
from BeamSearch.Interfaces.IGraph import IGraph
from BeamSearch.Interfaces.IQuery import IQuery


class ISearcher(ABC):
    @abstractmethod
    def search(self, query: IQuery, k: int) -> IGraph:
        pass
