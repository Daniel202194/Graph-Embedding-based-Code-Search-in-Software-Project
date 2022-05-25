

class Edge:

    def __init__(self, relation, in_v, out_v):
        self.relation = relation
        self.in_v = in_v # from
        self.out_v = out_v # out

    def __str__(self):
        return "({}-{}->{})".format(self.in_v,self.relation, self.out_v)