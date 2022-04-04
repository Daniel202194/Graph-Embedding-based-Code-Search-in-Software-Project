
public class Attack {
	private Block m;
	private Block c;
	private Block a;
	private BlockList k;
	private Block k1;
	private Block k2;

	// constructor
	public Attack(String msg_path, String cipher_path) {
		BlockList full_msg = new BlockList(msg_path);
		BlockList full_cipher = new BlockList(cipher_path);
		this.m = full_msg.getBlock(0);
		this.c = full_cipher.getBlock(0);
		k = new BlockList();
		this.a = c.xor(m);
	}

	// methods
	public BlockList breakAES2(String output_path) {
		// build a fake key1:
		this.k1 = new Block();
		byte b = 0;
		String hex = Bases.byteToHex(b);
		while(!k1.isFull())
			k1.add(hex);

		// calculate k2:
		this.k2 = a.xor(k1);

		this.k.addBlock(this.k1);
		this.k.addBlock(this.k2);

		k.writeToBytesFile(output_path);
		return this.k;
	}
	public BlockList enc(String msg_path, String output_path) {
		BlockList msg = new BlockList(msg_path);
		BlockList enc = AES2.enc(msg, this.k1, this.k2);
		enc.writeToBytesFile(output_path);
		return enc;
	}	
	public BlockList dec(String cipher_path, String output_path) {
		BlockList cipher = new BlockList(cipher_path);
		BlockList dec = AES2.dec(cipher, this.k1, this.k2);
		dec.writeToBytesFile(output_path);
		return dec;
	}

}


