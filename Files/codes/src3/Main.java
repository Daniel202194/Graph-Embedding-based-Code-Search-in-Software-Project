import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
//		test();
		if(args.length >= 7) {
			String instruction = args[0].toLowerCase(),
					key_path="", input_path="", output_path="", msg="", cipher="";

			for(int i=0; i<args.length; i++){
				if(args[i].toLowerCase().equals("-k"))
					key_path = args[i+1];
				else if(args[i].toLowerCase().equals("-i"))
					input_path = args[i+1];
				else if(args[i].toLowerCase().equals("-o"))
					output_path = args[i+1];
				else if(args[i].toLowerCase().equals("-m"))
					msg = args[i+1];
				else if(args[i].toLowerCase().equals("-c"))
					cipher = args[i+1];
			}
			System.out.println();
			switch(instruction.toLowerCase()) {
			case "-e":
				AES2.enc(input_path, key_path, output_path);
				System.out.println("message encripted to: '"+output_path+"'");
				break;
			case "-d":
				AES2.dec(input_path, key_path, output_path);
				System.out.println("cipher decripted to: '"+output_path+"'");
				break;
			case "-b":
				Attack attack = new Attack(msg, cipher);
				attack.breakAES2(output_path);
				System.out.println("AES was broken, you can find the keys on: '"+output_path+"'");
				break;
			default:
				// code block
			}
		}
	}

	public static void test() {
		String message_short = "./files/message_short";
		String message_long = "./files/message_long";
		String cipher_short = "./files/cipher_short";
		String cipher_long = "./files/cipher_long";
		String key_short = "./files/key_short";
		String key_long = "./files/key_long";

		List<Test> tests = new ArrayList<>();
		tests.add(new Test_AES2(key_short, message_short, cipher_short));
		tests.add(new Test_AES2(key_long, message_long, cipher_long));

		for(int i = 0 ; i < tests.size(); i++) {
			boolean ans = tests.get(i).run();
			System.out.println("Test " + (i+1) + " " + ans);
		}

	}
}
