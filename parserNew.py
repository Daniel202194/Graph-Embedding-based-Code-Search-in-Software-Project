import nltk
import re

nltk.download('stopwords')
from nltk.corpus import stopwords


class Parse:

    def __init__(self, graphs_list=[]):
        self.stop_words = stopwords.words('english')
        self.stop_words_set = set(self.stop_words)
        self.graphs_list = graphs_list

    def query_parse(self, query):
        string = query
        string = re.sub(r'(\D)(\d+)%', r'\1 \2% ', string)
        string = re.sub(r'(\S)@', r'\1 @', string)
        string = re.sub(r'(\S)#', r'\1 #', string)
        string = re.sub(r'\$', 'S', string)
        string = re.sub(r'(\d+)([a-zA-Z])', r'\1 \2', string)
        string = re.sub(r'(\d),(\d)', r'\1\2', string)
        string = re.sub(r'(\D)\.', r'\1 ', string)
        reg_string = re.split('\s|\b|!|\a|\(|\)|-|\?|~|[^a-zA-Z0-9_@#%]', string)
        text_tokens_without_stopwords = [term.lower() for term in reg_string if
                                         term.lower() not in self.stop_words_set and len(term) > 0]
        return set(text_tokens_without_stopwords)

    def nodes_parse(self):
        for graph in self.graphs_list:
            bow_vertex = {}
            word_vertex = {}
            vertexes_name = {}
            for v in graph.vertexes.values():
                v_sets = set()
                word = ""
                for i, char in enumerate(v.name):
                    if i == 0:
                        word = word + char
                    elif i > 0 and char.islower():
                        word = word + char
                    else:
                        v_sets.add(word.lower())
                        word = char
                if len(word) > 0:
                    if word.lower() in word_vertex:
                        word_vertex[word.lower()].append(v)
                    else:
                        word_vertex[word.lower()] = [v]
                    v_sets.add(word.lower())
                bow_vertex[v] = v_sets
                graph.add_bow_vertex(bow_vertex)
                graph.add_word_to_vertex(word_vertex)
                vertexes_name[v.name.lower()] = v
