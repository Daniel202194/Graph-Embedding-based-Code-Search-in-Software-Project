package List;
import java.util.Iterator;

public class List<T> implements Iterable<T>{
	Node<T> head;
	Node<T> tail;
	
	public List() {
		this.head = null;
		this.tail = null;
	}

	@Override
	public Iterator<T> iterator() {
		return new ListFilterIterator<T>(this, data->((int)data)%3==0);
	}
	
	public void add(T data) {
		Node<T> node = new Node<>(data, null);
		if(head==null)
			this.head = this.tail = node;
		else {
			tail.setNext(node);
			this.tail = node;
		}
	}
	
	public Node<T> getHead(){
		return this.head;
	}
	
	public Node<T> getTail(){
		return this.tail;
	}

//	@Override
//	public boolean test(T t) {
//		return (Integer)t % 3 == 0;
//	}


	

}
