import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Test_AES2 implements Test {
	private String keys_path;
	private String expected_msg_path;
	private String expected_cipher_path;

	private BlockList expected_msg;
	private BlockList expected_cipher;
	
	private String enc_result_path;
	private String dec_result_path;

	public Test_AES2(String key_path, String expected_msg_path, String expected_cipher_path) {
		this.keys_path = key_path;
		this.expected_msg_path = expected_msg_path;
		this.expected_cipher_path = expected_cipher_path;

		this.expected_msg = new BlockList(expected_msg_path);
		this.expected_cipher = new BlockList(expected_cipher_path);
		
		this.enc_result_path = "./files/output/enc_result";
		this.dec_result_path = "./files/output/dec_result";

	}

	@Override
	public boolean run() {
		AES2.enc(this.expected_msg_path, this.keys_path, this.enc_result_path);
		boolean enc_test = isEquals(this.expected_cipher_path, this.enc_result_path);
		
		AES2.dec(this.expected_cipher_path, this.keys_path, this.dec_result_path);
		boolean dec_test = isEquals(this.expected_msg_path, dec_result_path);
		
		return enc_test && dec_test;
	}

	private boolean isEquals(String block1_path, String block2_path) {
		BlockList b1 = new BlockList(block1_path);
		BlockList b2 = new BlockList(block2_path);
		return b1.isEquals(b2);
	}

	// prints:
	public void printEnc(BlockList result) {
		System.out.print("original cipher:  ");
		expected_cipher.printChars();

		System.out.print("this cipher:      ");
		result.printChars();
	}
	public void printDec(BlockList result) {
		System.out.print("original msg:  ");
		expected_msg.printChars();

		System.out.print("this msg:      ");
		result.printChars();
	}

	//	Additional Functions:
	public void read(String path) {
		Reader bytestream;
		try {
			bytestream = new BufferedReader(new InputStreamReader(
					new FileInputStream(path), "ISO-8859-1"));
			int unsignedByte;
			while((unsignedByte = bytestream.read()) != -1)
				System.out.print((char)unsignedByte);
			System.out.println();
		}
		catch (UnsupportedEncodingException | FileNotFoundException e) {}
		catch (IOException e) {}

	}
	public void printFile(String path) {
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(Paths.get(path));
			//			Bases.printCharsByBytesFile(bytes);
			Bases.printBytesDetails(bytes);
		}catch (Exception e) {}
	}

}