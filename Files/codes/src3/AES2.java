
public class AES2 {
	
	public static BlockList enc(String path_msg, String path_key, String path_result) {
		BlockList msg = new BlockList(path_msg);
		BlockList key = new BlockList(path_key);
		Block key1 = key.getBlock(0);
		Block key2 = key.getBlock(1);
		
		BlockList result = AES1.enc(AES1.enc(msg, key1),key2);
		result.writeToBytesFile(path_result);
		return result;
	}
	public static BlockList dec(String path_cipher, String path_key, String path_result) {
		BlockList cipher = new BlockList(path_cipher);
		BlockList key = new BlockList(path_key);
		Block key1 = key.getBlock(0);
		Block key2 = key.getBlock(1);

		BlockList result = AES1.dec(AES1.dec(cipher, key2),key1);
		result.writeToBytesFile(path_result);
		return result;
	}

	public static BlockList enc(BlockList msg, Block key1, Block key2) {
		return AES1.enc(AES1.enc(msg, key1),key2);
	}
	public static BlockList dec(BlockList cipher, Block key1, Block key2) {
		return AES1.dec(AES1.dec(cipher, key2),key1);
	}
	
}
