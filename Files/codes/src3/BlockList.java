import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class BlockList {
	private List<Block> blocks;
	private int size;

	//	constructors:
	public BlockList() {
		this.blocks = new ArrayList<>();
		this.size = 0;
	}
	public BlockList(BlockList other) {
		this.blocks = new ArrayList<>();
		this.size = 0;
		for(Block block : other.blocks)
			this.blocks.add(new Block(block));
	}
	public BlockList(String path) {
		this.blocks = new ArrayList<>();
		this.size = 0;

		File file = new File(path);
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(file.toPath());
			Block block = new Block();
			if (bytes != null) {
				this.addBlock(block);
				for (byte b : bytes) {
					if(block.isFull()) {
						block = new Block();
						this.addBlock(block);
					}
					String hexString = Bases.byteToHex(b);
					block.add(hexString);
				}
			}
		}
		catch (IOException e) {}
	}

	//	getters & setters:
	public Block getBlock(int i) {
		if(i >= size) return null;
		return blocks.get(i);
	}
	public int getSize() {
		return this.size;
	}

	// opertaions:
	public void addBlock(Block block) {
		this.blocks.add(block);
		size++;
	}
	public BlockList xor(Block other) {
		BlockList result = new BlockList();
		for(Block block:this.blocks) {
			result.addBlock(block.xor(other));
		}
		return result;
	}
	public void swap() {
		for(Block block : this.blocks) 
			block.swap();
	}
	public boolean isEquals(BlockList other) {
		for(int i=0; i < size; i++) {
			if(!(this.getBlock(i).equals(other.getBlock(i))))
				return false;
		}
		return true;
	}

	//	prints:
	public void print() {
		int i=1;
		for(Block block : this.blocks) {
			System.out.println("Block no."+i+":");
			block.print();
			i++;
		}
	}
	public void printChars() {
		for(int i = 0; i < this.size; i++) {
			byte[] b = this.getBlock(i).blockToBytes();
			Bases.printBytes(b);
		}
	}
	public void printBytesDetails() {
		for(Block b : this.blocks)
			b.printBytesDetails();
	}

	//files:
	public void writeToBytesFile(String path) {
		clearFile(path);
		for(Block b : this.blocks)
			b.writeToBytesFile(path);
	}
	private void clearFile(String path) {
		try (FileOutputStream fos = new FileOutputStream(path)) {
			fos.write("".getBytes());
			fos.close();
		} catch (Exception e) {}
	}

}
