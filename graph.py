from stemmer import Stemmer
from vertex import Vertex
from edge import Edge
import torch
import torchtext
import gensim.downloader as api
# glove = torchtext.vocab.GloVe(name="6B", dim=50)
glove = api.load("glove-twitter-25")  # load glove vectors

class Graph:
    def __init__(self, vertexes={}, edges={}, abb_dict={}, bow_vertex={}, word_vertex={}, names={}):
        global glove
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

    def create_neighbours(self):
        for vertex in self.vertexes:
            neighbours = []
            for edge in self.edges:
                if edge.in_v.key == vertex:
                    neighbours.append(edge.out_v.key)
            self.neighbours[vertex] = neighbours

    def bfs(self, goal, start):
        visited = []
        pred = {}
        dist = {start: 0}
        queue = []
        set_edges = set()
        visited.append(start)
        queue.append(start)
        found = False
        while queue and not found:
            node = queue.pop()
            for neighbour in self.neighbours[node]:
                if neighbour not in visited:
                    visited.append(neighbour)
                    dist[neighbour] = dist[node] + 1
                    pred[neighbour] = node
                    queue.append(neighbour)
                    if neighbour.key == goal.key:
                        found = True
                        break
        if not found:
            print("There is no path from source node to destination node")
        else:
            crawl = goal
            path = [crawl]
            while crawl in pred:
                path.append(pred[crawl])
                crawl = pred[crawl]
            path.reverse()
            return path

    def get_vertices(self):
        return list(self.vertexes.values())

    def get_edges(self):
        return list(self.edges.values())

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
    def generate_graph(data, abb_dict):
        set_vertexes = {}
        set_edges = set()
        names = dict()
        for v in data['vertices']:
            set_vertexes[v['key']] = (Vertex(v['name'], v['type'], v['key']))
            names[v['name']] = v['name']
        for e in data['edges']:
            set_edges.add(Edge(e['type'], set_vertexes[e['from']], set_vertexes[e['to']]))
        return Graph(set_vertexes, set_edges, abb_dict)

    # def set_distances_between_vertecies(self):
    #     dist = {}
    #     for vertex in self.vertexes:
    #         for edge in self.edges:
    #             if edge.in_v is vertex:
    #


    def calculate_score(self,v,query,scores):
        # ---------- simple rule 1 ---------#
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

        sim_lst = []
        not_in_q_bow = set(self.bow_vertex[v]) - query.intersection(self.bow_vertex[v])
        for q in query:
            for not_q in not_in_q_bow:
                sim = torch.cosine_similarity(q.unsqueeze(0), not_q.unsqueeze(0))
                sim_lst.append(sim)
        max_sim = max(sim_lst)
        score_4_r = max_sim / (len(query))
        score_4_ir = max_sim / (len(self.bow_vertex[v]))
        # ---------- glove rule  ---------#
        score_r = max(score_1_r, score_2_r, score_3_r, score_4_r)
        score_ir = max(score_1_ir, score_2_ir, score_3_ir, score_4_ir)
        scores[v] = 2 * score_r * score_ir / (score_r + score_ir)

    def get_score_relevant(self, candidates, query):
        scores = {}
        for c_dict in candidates.values():
            for v in c_dict:
                self.calculate_score(v, query, scores)
        return scores

    def get_candidates(self, query_set):
        candidates = []
        query_candidates = {}
        glove_dict = {}
        glove_set = set()
        glove_set |= query_set
        for q_word in query_set:
            glove_dict[q_word] = set([x[0] for x in glove.most_similar(q_word)[:3]])
            glove_set |= set([x[0] for x in glove.most_similar(q_word)[:3]])

        # for word in query_set:
        for word in glove_set:
            query_candidates[word] = set()
            # for vertex in self.vertexes:

            stem_word = self.stem.stem_term(word)
            if word in self.abbreviation:
                ab_word = self.abbreviation[word]
                query_candidates[word].add(ab_word)
            else:
                ab_word = ''
            if word.lower() in self.vertexes_name:
                candidates.append(self.vertexes_name[word])
                query_candidates[word].add(self.vertexes_name[word])
            if word in self.word_vertex:
                candidates.extend(self.word_vertex[word])
                query_candidates[word] |= self.word_vertex[word]
                # if len(self.word_vertex[word]) == 1:
                #     candidates.extend(self.word_vertex[word])
                # elif len(self.word_vertex[word]) > 1:
                #     candidates.append(self.word_vertex[word][0])
            # elif stem_word in self.word_vertex:
            for key in self.word_vertex:
                if self.stem.stem_term(key) == stem_word:
                    candidates.extend(self.word_vertex[word])
                    query_candidates[word] |= self.word_vertex[word]
                # if len(self.word_vertex[stem_word]) == 1:
                #     candidates.extend(self.word_vertex[stem_word])
                # elif len(self.word_vertex[stem_word]) > 1:
                #     candidates.append(self.word_vertex[stem_word][0])
            if ab_word is not '' and ab_word in self.word_vertex:
                if len(self.word_vertex[ab_word]) == 1:
                    candidates.extend(self.word_vertex[ab_word])
                    query_candidates[word] |= self.word_vertex[ab_word]
                elif len(self.word_vertex[ab_word]) > 1:
                    candidates.append(self.word_vertex[ab_word][0])
                    query_candidates[word].add(self.word_vertex[ab_word][0])

            ## add glove wikipedia pre trained model --> for word delete to see as remove
        adjusted_to_query = {}

        glove_relevant_set = glove_set - query_set
        for g in glove_relevant_set:
            for d in glove_dict:
                if g in glove_dict[d]:
                    query_candidates[d].add(g)
        for q in query_set:
            adjusted_to_query[q] = query_candidates[q]
        return adjusted_to_query
