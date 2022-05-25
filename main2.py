import json
import os
import pickle

import networkx as nx
from graph import Graph
from parserNew import Parse
from Abbreviations import Abbreviations
from searcher.BeamSearch import BeamSearch

graphs_list = []


def foo(graph):
    print(graph.name)
    searcher = BeamSearch(graph)
    result = searcher.search(query)
    # result.print()
    for edge in result.get_edges():
        print(edge)

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
    # graph = graphs_list[0]
    # query = "block swap increase"
    query = "login main"
    query = p.query_parse(query)

    # foo(graphs_list[0])

    for graph in graphs_list:
        foo(graph)