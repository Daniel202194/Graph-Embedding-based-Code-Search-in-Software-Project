
from stemmer import Stemmer
from vertex import Vertex
from edge import Edge
import torch
import torchtext


class Graph:
    def __init__(self, vertexes={}, edges={}, bow_vertex={}, word_vertex={}, names={}):
        self.vertexes = vertexes
        self.edges = edges
        self.bow_vertex = bow_vertex
        self.word_vertex = word_vertex
        self.vertexes_name = names
        self.stem = Stemmer()
        self.treshold_sim = 0.8
        self.abbreviation = {} ##create  shortening word for example for word Number it will be NUM
        self.glove = torchtext.vocab.GloVe(name="6B", # trained on Wikipedia 2014 corpus of 6 billion words
                              dim=50)   # embedding size = 100

    def add_word_to_vertex(self, word_vertex):
        self.word_vertex = word_vertex

    def add_bow_vertex(self, bow_vertex):
        self.bow_vertex = bow_vertex

    def get_bow_vertex(self):
        return self.bow_vertex

    def get_word_vertex(self):
        return self.word_vertex

    @staticmethod
    def generate_graph(data):
        set_vertexes = {}
        set_edges = {}
        for v in data['vertices']:
            set_vertexes[v['key']] = (Vertex(v['name'], v['type'], v['key']))

        for e in data['edges']:
            set_edges.add(Edge(e['type'], set_vertexes[e['from']], set_vertexes[e['to']]))
        return Graph(set_vertexes, set_edges)

    def get_score_relevant(self, candidates, query):
        scores = {}
        for v in candidates:
            #---------- simple rule 1 ---------#
            instrac_1 = len(query.intersection(self.bow_vertex[v]))
            score_1_r = instrac_1/(len(query))
            score_1_ir = instrac_1/(len(self.bow_vertex[v]))
            # ---------- simple rule 1 ---------#

            # ---------- stemming rule  ---------#
            new_bow_2 = {v: set()}
            for w in self.bow_vertex[v]:
                new_bow_2[v] = new_bow_2[v].add(self.stem.stem_term(w))
            new_query_2 = set()
            for t in query:
                new_query_2.add(self.stem.stem_term(t))
            instrac_2 = len(new_query_2.intersection(new_bow_2[v]))
            score_2_r = instrac_2/len(new_query_2)
            score_2_ir = instrac_2/(len(new_bow_2[v]))
            # ---------- stemming rule  ---------#

            # ---------- abbreviation rule  ---------#
            new_bow_3 = {v: set()}
            for w in self.bow_vertex[v]:
                try:
                    new_bow_3[v] = new_bow_3[v].add(self.abbreviation[w])
                except:
                    new_bow_3[v] = new_bow_3[v].add(w)
            new_query_3 = set()
            for t in query:
                try:
                    new_query_3.add(self.abbreviation[t])
                except:
                    new_query_3.add(t)
            instrac_3 = len(new_query_3.intersection(new_bow_3[v]))
            score_3_r = instrac_3/(len(new_query_3))
            score_3_ir = instrac_3/(len(new_bow_3[v]))
            # ---------- abbreviation rule  ---------#

            # ---------- glove rule  ---------#

            sim_lst = []
            not_in_q_bow = set(self.bow_vertex[v]) - query.intersection(self.bow_vertex[v])
            for q in query:
                for not_q in not_in_q_bow:
                    sim = torch.cosine_similarity(q.unsqueeze(0), not_q.unsqueeze(0))
                    sim_lst.append(sim)
            max_sim = max(sim_lst)
            score_4_r = max_sim/(len(query))
            score_4_ir = max_sim/(len(self.bow_vertex[v]))
            # ---------- glove rule  ---------#
            score_r = max(score_1_r, score_2_r, score_3_r, score_4_r)
            score_ir = max(score_1_ir, score_2_ir, score_3_ir, score_4_ir)
            scores[v] = 2 * score_r * score_ir / ( score_r + score_ir)
        return scores

    def get_candidates(self, query_set):
        candidates = []
        for word in query_set:
            # for vertex in self.vertexes:

            stem_word = self.stem.stem_term(word)
            if word in self.abbreviation:
                ab_word = self.abbreviation[word]
            else:
                ab_word = ''
            if word.lower() == self.vertexes_name.lower():
                candidates.append(self.vertexes_name[word])
            if word in self.word_vertex:
                candidates.extend(self.word_vertex[word])
                # if len(self.word_vertex[word]) == 1:
                #     candidates.extend(self.word_vertex[word])
                # elif len(self.word_vertex[word]) > 1:
                #     candidates.append(self.word_vertex[word][0])
            # elif stem_word in self.word_vertex:
            for key in self.word_vertex:
                if self.stem.stem_term(key) == stem_word:
                    candidates.extend(self.word_vertex[word])
                # if len(self.word_vertex[stem_word]) == 1:
                #     candidates.extend(self.word_vertex[stem_word])
                # elif len(self.word_vertex[stem_word]) > 1:
                #     candidates.append(self.word_vertex[stem_word][0])
            if ab_word is not '' and ab_word in self.word_vertex:
                if len(self.word_vertex[ab_word]) == 1:
                    candidates.extend(self.word_vertex[ab_word])
                elif len(self.word_vertex[ab_word]) > 1:
                    candidates.append(self.word_vertex[ab_word][0])
            ## add glove wikipedia pre trained model --> for word delete to see as remove
            return set(candidates)
