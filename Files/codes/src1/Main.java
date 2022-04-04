package List;


public class Main {

	public static void main(String[] args) {
		List<Integer> list = new List<Integer>();
		for(int i=0; i<=20; i++)
			list.add(i);

		for(Integer i: list)
			System.out.print(i+" ");
	}

}
