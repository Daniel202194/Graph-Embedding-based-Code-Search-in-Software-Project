import json
import os

from graph import Graph
from parserNew import Parse
from Abbreviations import Abbreviations
graphs_list = []

if __name__ == '__main__':
    Abbreviations_path = os.getcwd() + '\\' + 'Abbreviations.csv'
    abb = Abbreviations(Abbreviations_path)
    for f in os.listdir(os.getcwd() + '\\' + 'graphs_list'):
        file = open(os.getcwd() + '\\' + 'graphs_list\\' + f)
        graphs_list.append(Graph.generate_graph(json.load(file), abb.abb_dict))
        file.close()

    p = Parse(graphs_list)
    p.nodes_parse()
    x = input('User gives query:')
    parsing_query = p.query_parse(x)
    graphs_candidates_nodes = {}
    for graph in graphs_list:
        graphs_candidates_nodes[graph] = graph.get_candidates(parsing_query)
    nodes_score_per_graph = {}
    for graph in graphs_candidates_nodes:
        nodes_score_per_graph[graph] = graph.get_score_relevant(graphs_candidates_nodes[graph], parsing_query)

    for graph in graphs_list:
        graph.bfs(46, 8)
