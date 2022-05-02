def aux1(candidates_by_token):
    res = {}
    for key in candidates_by_token.keys():
        res[key] = set()
        for val in candidates_by_token[key]:
            res[key].add(val.key)
    return res


def aux2(weights):
    res = {}
    for v in weights.keys():
        res[v.key] = weights[v]
    return res
