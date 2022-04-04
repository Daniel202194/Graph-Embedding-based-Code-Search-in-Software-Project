
public class AES1 {

	public static BlockList enc(BlockList msg, Block key) {
		msg.swap();
		return addRoundKey(msg, key);
	}
	public static BlockList dec(BlockList msg, Block key) {
		BlockList blocks = addRoundKey(msg, key);
		blocks.swap();
		return blocks;
	}

	//	Additional Functions:
	private static BlockList addRoundKey(BlockList msg, Block key) {
		return msg.xor(key);
	}

}
