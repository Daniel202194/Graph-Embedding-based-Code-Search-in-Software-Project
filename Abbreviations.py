import csv
class Abbreviations:
    def __int__(self,path):
        abb_dict = {}
        with open(path, newline='') as csvfile:
            lines = csv.reader(csvfile, delimiter=' ', quotechar='|')
            for line in lines:
                val = line[0]
                if line[0][:-1] == '.':
                    val = line[0][0:len(line[0]) - 1]
                if ',' in line[1]:
                    keys = line[1].split(',')
                    for key in keys:
                        abb_dict[key] = val
                else:
                    abb_dict[line[1]] = val