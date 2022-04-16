from abc import ABC, abstractmethod
from Interfaces.IGraph import IGraph
from Interfaces.IQuery import IQuery


class ISearcher(ABC):
    @abstractmethod
    def search(self, query: IQuery, k: int) -> IGraph:
        pass
