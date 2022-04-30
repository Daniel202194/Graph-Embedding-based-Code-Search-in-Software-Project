import json
import os
from graph import Graph
from parserNew import Parse
from Abbreviations import Abbreviations
from searcher.BeamSearch import BeamSearch

graphs_list = []

if __name__ == '__main__':
    Abbreviations_path = os.getcwd() + '\\' + 'Abbreviations.csv'
    abb = Abbreviations(Abbreviations_path)
    graph = Graph.generate_graph(json.load(open('Files/graphs/src1.json')), abb.abb_dict)

    p = Parse([graph])
    p.nodes_parse()

    searcher = BeamSearch(graph)

    x = "list node iterable"
    parsing_query = p.query_parse(x)
    searcher.search(parsing_query)
    #
    # graphs_candidates_nodes = graph.get_candidates(parsing_query)
    # print(graphs_candidates_nodes)
    #
    # nodes_score_per_graph = graph.get_score_relevant(graphs_candidates_nodes, parsing_query)
    # print(nodes_score_per_graph)

