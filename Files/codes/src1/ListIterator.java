package List;
import java.util.Iterator;

public class ListIterator<T> implements Iterator<T> {

	Node<T> curr;
	
	public ListIterator(List<T> list) {
		curr = list.getHead();
	}
	
	@Override
	public boolean hasNext() {
		return curr != null;
	}

	@Override
	public T next() {
		T data = curr.getData();
		curr = curr.getNext();
		return data;
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
