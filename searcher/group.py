
class Group:
    
    def __init__(self, vertex_key :int):
        self.vertices = set()
        self.vertices.add(vertex_key)
        self.cost = 0

    def __len__(self):
        return len(self.vertices)

    def add_vertex_key(self, key :int) ->None:
        self.vertices.add(key)

    def set_cost(self, val :float) ->None:
        self.cost = val

    def __str__(self):
        return str(self.cost) + str(self.vertices)