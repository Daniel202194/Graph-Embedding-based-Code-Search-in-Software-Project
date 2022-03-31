import csv
import os
import pandas as pd

# Abbreviations_path = os.getcwd() + '\\' + 'Abbreviations.csv'


class Abbreviations:
    def __init__(self, Abbreviations_path):
        self.abb_dict = {}
        self.Abbreviations_path = Abbreviations_path
        lines = pd.read_csv(self.Abbreviations_path, header=None)
        for line in lines.iterrows():
            print(line)
            line = list(line[1])

            if line[0][-1] == '.':
                line[0] = line[0][0:len(line[0]) - 1]
            if ',' in line[1]:
                keys = line[1].split(',')
                for key in keys:
                    index = key.find("(")
                    if index != -1:
                        key = key[:index]
                    key = key.strip()
                    self.abb_dict[key] = line[0]
            else:
                index = line[1].find("(")
                if index != -1:
                    line[1] = line[1][:index]
                line[1] = line[1].strip()
                self.abb_dict[line[1]] = line[0]
