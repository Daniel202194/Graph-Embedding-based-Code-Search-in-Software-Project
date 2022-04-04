
public class Test_attack implements Test {
	static int i = 0;
	static String attack_path_general = "./files/output/attack";
	
	private String attack_path;
	private Attack attack;
//	private BlockList keys;
	
	public Test_attack(String msg_path, String cipher_path){
		i++;
		this.attack_path = attack_path_general + i;
		this.attack = new Attack(msg_path, cipher_path);
		attack.breakAES2(attack_path);
//		this.keys = new BlockList(attack_path);
	}

	@Override
	public boolean run() {
		
		return true;
	}
}
