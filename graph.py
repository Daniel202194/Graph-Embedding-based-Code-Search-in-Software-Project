
from stemmer import Stemmer
from vertex import Vertex
from edge import Edge


class Graph:
    def __init__(self, vertexes={}, edges={}, bow_vertex={}, word_vertex={}, names={}):
        self.vertexes = vertexes
        self.edges = edges
        self.bow_vertex = bow_vertex
        self.word_vertex = word_vertex
        self.vertexes_name = names
        self.stem = Stemmer()
        self.abbreviation = {} ##create  shortening word for example for word Number it will be NUM

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
            score_1 = instrac_1/(len(self.bow_vertex[v]) - instrac_1)
            # ---------- simple rule 1 ---------#

            # ---------- stemming rule  ---------#
            new_bow_2 = {v: set()}
            for w in self.bow_vertex[v]:
                new_bow_2[v] = new_bow_2[v].add(self.stem.stem_term(w))
            new_query_2 = set()
            for t in query:
                new_query_2.add(self.stem.stem_term(t))
            instrac_2 = len(new_query_2.intersection(new_bow_2[v]))
            score_2 = instrac_2/(len(new_bow_2[v]) - instrac_2)
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
            score_3 = instrac_3/(len(new_bow_3[v]) - instrac_3)
            # ---------- abbreviation rule  ---------#

            # ---------- glove rule  ---------#
            new_bow_4 = {v: set()}
            for w in self.bow_vertex[v]:
                pass  #### add similar words from glove wikipedia
            new_query_4 = set()
            for t in query:
                pass #### add similar words from glove wikipedia

            instrac_4 = len(new_query_4.intersection(new_bow_4[v]))
            score_4 = instrac_4/(len(new_bow_4[v]) - instrac_4)
            # ---------- glove rule  ---------#
            # query_size = len(query)
            score = max(score_1, score_2, score_3, score_4)
            scores[v] = score
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
