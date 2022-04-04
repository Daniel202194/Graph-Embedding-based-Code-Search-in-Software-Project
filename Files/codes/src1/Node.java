package List;

public class Node<T> {
	T data;
	Node<T> next;
	
	public Node(T data, Node<T> next) {
		this.data = data;
		this.next = next;
	}

	
	public void setNext(Node<T> next) {
		this.next = next;
	}
	
	public Node<T> getNext(){
		return this.next;
	}
	
	public T getData() {
		return this.data;
	}
}
