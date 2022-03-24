from abc import ABC, abstractmethod

from BeamSearch.Interfaces.IResult import IResult


class ISearcher(ABC):
    @abstractmethod
    def search(self) -> IResult:
        pass