from stemmer import Stemmer
from vertex import Vertex
from edge import Edge
import gensim.downloader as api
import numpy as np

glove = api.load("glove-twitter-25")  # load glove vectors


class Graph:
    def __init__(self, vertexes={}, edges={}, abb_dict={}, bow_vertex={}, word_vertex={}, names={}, name='',
                 vertex_vectors={}):
        global glove
        self.name = name
        self.vertexes = vertexes
        self.edges = edges
        self.bow_vertex = bow_vertex
        self.word_vertex = word_vertex
        self.vertexes_name = names
        self.stem = Stemmer()
        self.vertexes_dsitances = {}
        self.treshold_sim = 0.8
        self.abbreviation = abb_dict
        self.glove = glove
        self.neighbours = {}
        self.create_neighbours()
        self.vertexes_vectors = vertex_vectors

    def create_neighbours(self):
        for vertex in self.vertexes:
            neighbours = []
            for edge in self.edges:
                if edge.in_v.key == vertex:
                    neighbours.append(edge.out_v.key)
                elif edge.out_v.key == vertex:
                    neighbours.append(edge.in_v.key)
            self.neighbours[vertex] = list(set(neighbours))

    def get_vertex(self, key):
        return self.vertexes[key]

    def get_vector(self, key):
        return self.vertexes_vectors[key]

    def vertexes_distance(self, key1, key2):
        dist = np.linalg.norm(self.get_vector(key1) - self.get_vector(key2))
        return dist

    def get_edge(self, from_key, to_key):
        for edge in self.edges:
            if edge.in_v.key == from_key and edge.out_v.key == to_key:
                return edge
            elif edge.in_v.key == to_key and edge.out_v.key == from_key:
                return edge

    def bfs(self, goal, start):
        visited = []
        pred = {}
        dist = {start: 0}
        queue = []
        visited.append(start)
        queue.append(start)
        found = False
        while queue and not found:
            node = queue.pop(0)
            if node in self.neighbours:
                for neighbour in self.neighbours[node]:
                    if neighbour not in visited:
                        visited.append(neighbour)
                        dist[neighbour] = dist[node] + 1
                        pred[neighbour] = node
                        queue.append(neighbour)
                        if neighbour == goal:
                            found = True
                            break
        if not found:
            print("There is no path from source node to destination node")
            return []
        else:
            crawl = goal
            path = [crawl]
            while crawl in pred:
                path.append(pred[crawl])
                crawl = pred[crawl]
            path.reverse()
            new_path = []
            for i in range(len(path) - 1):
                new_path.append(self.get_edge(path[i], path[i + 1]))
            return new_path

    def get_vertices(self):
        return list(self.vertexes.values())

    def get_edges(self):
        return list(self.edges)

    def add_edge(self, edge):
        if edge in self.edges:
            raise Exception(f'edge already exists')
        else:
            self.edges.add(edge)

    def add_vertex(self, vertex):
        if vertex.key in self.vertexes:
            raise Exception(f'vertex with key {vertex.key} already exists')
        else:
            self.vertexes[vertex.key] = vertex

    def add_word_to_vertex(self, word_vertex):
        self.word_vertex = word_vertex

    def add_bow_vertex(self, bow_vertex):
        self.bow_vertex = bow_vertex

    def get_bow_vertex(self):
        return self.bow_vertex

    def get_word_vertex(self):
        return self.word_vertex

    @staticmethod
    def generate_graph(data, abb_dict, name, vertex_vectors):
        set_vertexes = {}
        set_edges = set()
        names = dict()
        for v in data['vertices']:
            set_vertexes[v['key']] = (Vertex(v['name'], v['type'], v['key']))
            names[v['name']] = v['name']
        for e in data['edges']:
            set_edges.add(Edge(e['type'], set_vertexes[e['from']], set_vertexes[e['to']]))
        return Graph(set_vertexes, set_edges, abb_dict, names=names, name=name, vertex_vectors=vertex_vectors)

    def get_score_relevant(self, candidates, query):
        scores = {}
        for key in candidates:
            for v in candidates[key]:
                # ------    ---- simple rule 1 ---------#
                instrac_1 = len(query.intersection(self.bow_vertex[v]))
                score_1_r = instrac_1 / (len(query))
                score_1_ir = instrac_1 / (len(self.bow_vertex[v]))
                # ---------- simple rule 1 ---------#

                # ---------- stemming rule  ---------#
                new_bow_2 = {v: set()}
                for w in self.bow_vertex[v]:
                    new_bow_2[v].add(self.stem.stem_term(w))
                new_query_2 = set()
                for t in query:
                    new_query_2.add(self.stem.stem_term(t))
                instrac_2 = len(new_query_2.intersection(new_bow_2[v]))
                score_2_r = instrac_2 / len(new_query_2)
                score_2_ir = instrac_2 / (len(new_bow_2[v]))
                # ---------- stemming rule  ---------#

                # ---------- abbreviation rule  ---------#
                new_bow_3 = {v: set()}
                for w in self.bow_vertex[v]:
                    try:
                        new_bow_3[v].add(self.abbreviation[w])
                    except:
                        new_bow_3[v].add(w)
                new_query_3 = set()
                for t in query:
                    try:
                        new_query_3.add(self.abbreviation[t])
                    except:
                        new_query_3.add(t)
                instrac_3 = len(new_query_3.intersection(new_bow_3[v]))
                score_3_r = instrac_3 / (len(new_query_3))
                score_3_ir = instrac_3 / (len(new_bow_3[v]))
                # ---------- abbreviation rule  ---------#

                # ---------- glove rule  ---------#

                new_bow_4 = {v: set()}
                glove_dict = {}
                new_query_4 = set()
                for q in query:
                    try:
                        glove_dict[q] = set([x[0] for x in glove.most_similar(q)[:3]])
                        # new_query_4 |= set([x[0] for x in glove.most_similar(q)[:3]])
                    except:
                        new_query_4.add(q)
                        print(f"Not found for in glove for word : {q}")
                for value in glove_dict.values():
                    new_query_4 |= value
                instrac_glove_list = []
                instrac_glove_list.append(len(new_query_4.intersection(self.bow_vertex[v])))
                score_r_glove = instrac_glove_list[0] / (len(new_query_4))
                score_ir_glove = instrac_glove_list[0] / (len(self.bow_vertex[v]))
                new_query_stem_glove = set()
                for t in new_query_4:
                    new_query_stem_glove.add(self.stem.stem_term(t))
                instrac_glove_list.append(len(new_query_stem_glove.intersection(new_bow_2[v])))
                score_r_glove += instrac_glove_list[1] / len(new_query_stem_glove)
                score_ir_glove += instrac_glove_list[1] / (len(new_bow_2[v]))
                new_query_abb = set()
                for t in new_query_4:
                    try:
                        new_query_abb.add(self.abbreviation[t])
                    except:
                        new_query_abb.add(t)
                instrac_glove_list.append(len(new_query_abb.intersection(new_bow_3[v])))
                score_r_glove += instrac_glove_list[2] / (len(new_query_abb))
                score_ir_glove += instrac_glove_list[2] / (len(new_bow_3[v]))

                # ---------- glove rule  ---------#
                score_r = max(score_1_r, score_2_r, score_3_r, score_r_glove / 3)
                score_ir = max(score_1_ir, score_2_ir, score_3_ir, score_ir_glove / 3)
                # score_r = max(score_1_r, score_2_r, score_3_r)
                # score_ir = max(score_1_ir, score_2_ir, score_3_ir)

                if v in scores:
                    try:
                        scores[v] = scores[v] + (2 * score_r * score_ir / (score_r + score_ir))
                    except:
                        scores[v] = scores[v]
                else:
                    try:
                        scores[v] = 2 * score_r * score_ir / (score_r + score_ir)
                    except:
                        scores[v] = 0
        return scores

    def get_candidates(self, query_set):
        glove_dict = {}
        candidates = dict()
        for q_word in query_set:
            try:
                glove_dict[q_word] = set([x[0] for x in glove.most_similar(q_word)[:3]])
            except:
                print(f"Not found for in glove for word : {q_word}")
        for word in query_set:
            if word not in candidates:
                candidates[word] = set()
            stem_word = self.stem.stem_term(word)
            if stem_word in self.word_vertex:
                candidates[word] |= set(self.word_vertex[stem_word])
            if word in self.abbreviation:
                ab_word = self.abbreviation[word]
            else:
                ab_word = ''
            if ab_word != '' and ab_word in self.word_vertex:
                candidates[word] |= set(self.word_vertex[ab_word])
            if word in self.word_vertex:
                candidates[word] |= set(self.word_vertex[word])
            if word in glove_dict:
                for w in glove_dict[word]:
                    if w in self.word_vertex:
                        candidates[word] |= set(self.word_vertex[w])
                        curStem = self.stem.stem_term(w)
                        if curStem in self.word_vertex:
                            candidates[word] |= set(self.word_vertex[w])
                    if w in self.abbreviation:
                        cur_ab_word = self.abbreviation[w]
                    else:
                        cur_ab_word = ''
                    if cur_ab_word != '' and cur_ab_word in self.word_vertex:
                        candidates[word] |= set(self.word_vertex[cur_ab_word])
        return candidates
