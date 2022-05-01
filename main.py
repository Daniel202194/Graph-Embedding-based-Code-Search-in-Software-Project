import json
import os
import pickle

import networkx as nx
from graph import Graph
from parserNew import Parse
from Abbreviations import Abbreviations

graphs_list = []

if __name__ == '__main__':
    Abbreviations_path = os.getcwd() + '\\' + 'Abbreviations.csv'
    abb = Abbreviations(Abbreviations_path)
    for f in os.listdir(os.getcwd() + '\\' + 'graphs_list'):
        file = open(os.getcwd() + '\\' + 'graphs_list\\' + f)
        try:
            vertex_vector = nx.read_gpickle(os.getcwd() + f"\\nxGraphs\\{f[:len(f) - 5]}.pkl")
            graphs_list.append(Graph.generate_graph(json.load(file), abb.abb_dict, f[:len(f) - 5], vertex_vector))
        except IOError as e:
            print(f'file {f[:len(f) - 5]}.pkl not found')
            graphs_list.append(Graph.generate_graph(json.load(file), abb.abb_dict, f[:len(f) - 5], {}))

        file.close()
    p = Parse(graphs_list)
    p.nodes_parse()
    x = input('User gives query:')
    parsing_query = p.query_parse(x)
    graphs_candidates_nodes = {}
    for graph in graphs_list:
        graphs_candidates_nodes[graph] = graph.get_candidates(parsing_query)
    nodes_score_per_graph = {}
    nxGraphs = []

    for graph in graphs_candidates_nodes:
        nodes_score_per_graph[graph] = graph.get_score_relevant(graphs_candidates_nodes[graph], parsing_query)

    # for graph in graphs_list:
    #     g = nx.Graph()
    #     list_edges = [(e.in_v.key, e.out_v.key) for e in graph.get_edges()]
    #     g.add_edges_from(list_edges, weight=1)
    #     nxGraphs.append(g)
    #     nx.write_gpickle(g, f'{graph.name}.gpickle')
