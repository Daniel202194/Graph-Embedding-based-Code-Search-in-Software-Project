from abc import ABC, abstractmethod


class IQuery(ABC):
    @abstractmethod
    def get_tokens(self):
        pass

    @abstractmethod
    def build_graph(self)->Graph:
        pass
