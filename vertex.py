

class Vertex:

    def __init__(self, name, vertex_type,key):
        self.name = name
        self.vertex_type = vertex_type
        self.key = key

    def __str__(self):
        return "[{},{},{}]".format(self.key, self.vertex_type, self.name)