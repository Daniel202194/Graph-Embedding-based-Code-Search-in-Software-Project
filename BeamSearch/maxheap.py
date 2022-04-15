
class MaxHeap:

    def __init__(self, key=None):
        self.arr = list()
        self.pos_map = {}
        self.size = 0
        self.key = key or (lambda x: x)

    def _parent(self, i):
        return int((i - 1) / 2) if i > 0 else None

    def _left(self, i):
        left = int(2 * i + 1)
        return left if 0 < left < self.size else None

    def _right(self, i):
        right = int(2 * i + 2)
        return right if 0 < right < self.size else None

    def _swap(self, i, j):
        self.pos_map[self.arr[i][0]], self.pos_map[self.arr[j][0]] = (
            self.pos_map[self.arr[j][0]],
            self.pos_map[self.arr[i][0]],
        )
        self.arr[i], self.arr[j] = self.arr[j], self.arr[i]

    def _cmp(self, i, j):
        return self.arr[i][1] < self.arr[j][1]

    def _get_valid_parent(self, i):
        left = self._left(i)
        right = self._right(i)
        valid_parent = i

        if left is not None and not self._cmp(left, valid_parent):
            valid_parent = left
        if right is not None and not self._cmp(right, valid_parent):
            valid_parent = right

        return valid_parent

    def _heapify_up(self, index):
        parent = self._parent(index)
        while parent is not None and not self._cmp(index, parent):
            self._swap(index, parent)
            index, parent = parent, self._parent(parent)

    def _heapify_down(self, index):
        valid_parent = self._get_valid_parent(index)
        while valid_parent != index:
            self._swap(index, valid_parent)
            index, valid_parent = valid_parent, self._get_valid_parent(valid_parent)

    def update_item(self, item, item_value):
        if item not in self.pos_map:
            return
        index = self.pos_map[item]
        self.arr[index] = [item, self.key(item_value)]
        self._heapify_up(index)
        self._heapify_down(index)

    def delete_item(self, item):
        if item not in self.pos_map:
            return
        index = self.pos_map[item]
        del self.pos_map[item]
        self.arr[index] = self.arr[self.size - 1]
        self.pos_map[self.arr[self.size - 1][0]] = index
        self.size -= 1
        if self.size > index:
            self._heapify_up(index)
            self._heapify_down(index)

    def push(self, item_value, item):
        arr_len = len(self.arr)
        if arr_len == self.size:
            self.arr.append([item, self.key(item_value)])
        else:
            self.arr[self.size] = [item, self.key(item_value)]
        self.pos_map[item] = self.size
        self.size += 1
        self._heapify_up(self.size - 1)

    def top(self):
        return self.arr[0] if self.size else None

    def pop(self):
        top_item_tuple = self.top()
        if top_item_tuple:
            self.delete_item(top_item_tuple[0])
        if not top_item_tuple:
            return None
        return top_item_tuple

    def __iter__(self):
        return iter(self.arr)



if __name__ == "__main__":
    maxHeap = MaxHeap()
    maxHeap.push(1, Vertex(1, "1", "class"))
    # maxHeap.insert_item(1, Vertex(2, "2", "class"))
    # maxHeap.insert_item(1, Vertex(3, "3", "class"))
    res, rank = maxHeap.pop()
    print(res)
    print(rank)
