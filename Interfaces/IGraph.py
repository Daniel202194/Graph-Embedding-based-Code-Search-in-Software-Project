from abc import ABC, abstractmethod


class IGraph(ABC):
    @abstractmethod
    def bfs(self, source: int, goal: int) -> list:
        pass

    @abstractmethod
    def get_vertices(self) ->list:
        pass

    @abstractmethod
    def get_edges(self) ->list:
        pass

    @abstractmethod
    def get_edge(self,source_key :int, dest_key :int):
        pass

    @abstractmethod
    def get_vertex(self, key :int):
        pass
