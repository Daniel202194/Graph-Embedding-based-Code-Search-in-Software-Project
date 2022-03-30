import json
import os

from graph import Graph
from parser import Parse
graphs_list = []

if __name__ == '__main__':
    for cur_graph in os.listdir(os.getcwd() + '\\' + 'graphs_list'):
        f = open(cur_graph)
        graphs_list.append(Graph.generate_graph(json.load(f)))
        f.close()

    p = Parse(graphs_list)
    p.nodes_parse()
    x = input('User gives query:')
    parsing_query = p.query_parse(x)
    graphs_candidates_nodes = {}
    for graph in graphs_list:
        graphs_candidates_nodes[graph] = graph.get_candidates(parsing_query)
    nodes_score_per_graph = {}
    for graph in graphs_candidates_nodes:
        graph.get_score_relevant(graphs_candidates_nodes[graph], parsing_query)


