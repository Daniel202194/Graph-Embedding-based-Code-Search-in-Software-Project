package List;

import java.util.Iterator;
import java.util.function.Predicate;

public class ListFilterIterator<T> implements Iterator<T> {
	List<T> list;
	Node<T> curr;
	Predicate<T> prd;
	
	public ListFilterIterator(List<T> list, Predicate<T> p) {
		this.prd = p;
		this.list = list;
		Node<T> node = list.getHead();
		while(node!=null && !this.prd.test(node.getData())) {
			node = node.getNext();
		}
		curr = node;
	}
	
	@Override
	public boolean hasNext() {
		return curr != null;
	}

	@Override
	public T next() {
		T data = curr.getData();
		Node<T> node = this.curr.getNext();
		while(node!=null && !this.prd.test(node.getData())) {
			node = node.getNext();
		}
		curr = node;
		return data;
	}

	
}
