import json
import os
import pickle

import networkx as nx
from graph import Graph
from parserNew import Parse
from Abbreviations import Abbreviations
from searcher.BeamSearch import BeamSearch

graphs_list = []


def foo(graph,query):
    # print(graph.name)
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
    # query = "login main"
    # query = p.query_parse(query)
    all_poi_queries = [
        # 'How to create a new workbook',
        # 'How to create a sheet',
        # 'How to create cells',
        # 'How to create date cells',
        # 'Working with different types of cells',
        # 'Iterate over rows and cells',
        # 'Getting the cell contents',
        # 'Text Extraction',
        # 'Files vs InputStreams',
        # 'Aligning cells',
        # 'Working with borders',
        # 'Fills and color',
        # 'Merging cells',
        # 'Working with fonts',
        # 'Custom colors',
        # 'Reading and writing',
        # 'Use newlines in cells.',
        # 'Create user defined data formats',
        # 'Fit Sheet to One Page',
        # 'Set print area for a sheet',
        # 'Set page numbers on the footer of a sheet',
        # 'Shift rows',
        # 'Set a sheet as selected',
        # 'Set the zoom magnification for a sheet',
        # 'Create split and freeze panes',
        # 'Repeating rows and columns',
        # 'Headers and Footers',
        # 'XSSF enhancement for Headers and Footers',
        'Drawing shapes'
        'Shapes and Graphics2d',
        'Outlining',
        'Images',
        'Named Ranges and Named Cells',
        'How to set cell comments',
        'How to adjust column width to fit the contents',
        'Hyperlinks',
        'Data Validations',
        'Embedded Objects',
        'Autofilters',
        'Conditional Formatting',
        'Hiding and Un-Hiding Rows',
        'Setting Cell Properties',
        'Drawing Borders',
        'Create a Pivot Table',
        'Cells with multiple styles'
    ]
    # foo(graphs_list[0])

    for graph in graphs_list:
        for index, q in enumerate(all_poi_queries):
            print(f'------------------------ Query_{index}-------------------------')
            query = p.query_parse(q)
            foo(graph, query)
            print(f'---------------------------------------------------------------')