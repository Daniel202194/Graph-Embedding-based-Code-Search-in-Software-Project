import json
import os

import networkx as nx
from graph import Graph
from parserNew import Parse
from Abbreviations import Abbreviations
from searcher.BeamSearch import BeamSearch
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
    x = ''
    # while x != 'stop':
    #     try:
    #         x = input('User gives query:')
    #         parsing_query = p.query_parse(x)
            # graphs_candidates_nodes = {}

            # for graph in graphs_list:
            #     graphs_candidates_nodes[graph] = graph.get_candidates(parsing_query)
            # nodes_score_per_graph = {}
            # for graph in graphs_candidates_nodes:
            #     nodes_score_per_graph[graph] = graph.get_score_relevant(graphs_candidates_nodes[graph], parsing_query)
        #     results = {}
        #     for graph in graphs_list:
        #         searcher = BeamSearch(graph)
        #         result = searcher.search(parsing_query)
        #         results[graph.name] = result
        #         searcher = None
        #         result = None
        #     for graph in results:
        #         print(f'Graph Name: {graph}')
        #         for vertex in results[graph].get_vertices():
        #             print(f'vertex name : {vertex.name} ,vertex key: {vertex.key}')
        # except Exception:

            # print('fail')
            # for graph in graphs_candidates_nodes
            #     print(f'Graph Name: {graph.name}')
            #     for word in parsing_query:
            #         print(f'Candidates for token: {word}')
            #         nodes = graphs_candidates_nodes[graph][word]
            #         for v in nodes:
            #             print(f'\tVertex Name: {v.name}')
            #             print(f'\tVertex Key: {v.key}')
            #             print(f'\tVertex Key: {v.vertex_type}')
            #     print('----------------------------------------')

            #
            #
            # for graph in graphs_list:
            #     print(f'graph name: {graph.name}')
            #     path = graph.bfs(41, 62)
            #     print(f'bfs path {path}')
            #     edges_path = []
            #     for i in range(len(path) - 1):
            #         vertex1 = path[i]
            #         vertex2 = path[i+1]
            #         edge = graph.get_edge(vertex1, vertex2)
            #         edges_path.append(edge)
            #     new_path = []
            #     for edge in edges_path:
            #         a = edge.in_v.key, edge.out_v.key
            #         new_path.append(a)
            #     print(f'edges path : {new_path}')
            #     print("-----------------------------")
    nxGraphs = []
    for graph in graphs_list:
        if "hssf" in graph.name:
            g = nx.Graph()
            list_edges = [(e.in_v.key, e.out_v.key) for e in graph.get_edges()]
            g.add_edges_from(list_edges, weight=1)
            nxGraphs.append(g)
            nx.write_gpickle(g, f'{graph.name}.gpickle')
        else:
            continue
