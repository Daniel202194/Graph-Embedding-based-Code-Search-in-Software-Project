import java.io.FileOutputStream;

public class Block {
	static final int ROWS = 4;
	static final int COLS = 4;
	private String[][] values;
	private int lastRow;
	private int lastCol;

	//	constructors:
	public Block() {
		this.values = new String[ROWS][COLS];
		this.lastRow = 0; this.lastCol = 0;
	}
	public Block(Block other) {
		this.values = new String[ROWS][COLS];
		this.lastRow = 0; this.lastCol = 0;
		for(int row=0; row < ROWS; row++) {
			for(int col=0; col < COLS; col++) {
				this.values[row][col] = other.getValue(row, col);
			}
		}
	}

	// getters & setters:
	public int getRows() {
		return ROWS;
	}
	public int getCols() {
		return COLS;
	}
	public int getSize() {
		return ROWS * COLS;
	}

	//	operations:
	public boolean add(String input) {
		if(!this.isFull()) {
			this.values[this.lastRow][this.lastCol] = input;
			this.increaseIndex();
			return true;
		}
		return false;
	}
	public void swap(){
		for(int row = 0; row < ROWS; row++) {
			for(int col = row + 1; col < COLS; col++) {
				swapValues(row, col);
			}
		}
	}
	public Block xor(Block other) {
		Block result = new Block();
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < COLS; col++) {
				String val1 = this.getValue(row, col);
				String val2 = other.getValue(row, col);
				result.add(Bases.xorHexes(val1,val2));
			}
		}
		return result;
	}

	//	status:
	public boolean isFull() {
		return (lastRow >= ROWS) || (lastCol >= COLS);
	}
	public boolean equals(Block other) {
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < COLS; col++) {
				String val1 = this.getValue(row, col);
				String val2 = other.getValue(row, col);
				if(!(val1.equals(val2))) return false;
			}
		}
		return true;
	}

	// Files:
	public void writeToBytesFile(String path) {
		byte[] data = this.blockToBytes();
		try (FileOutputStream fos = new FileOutputStream(path, true)) {
			fos.write(data);
			fos.close();
		} catch (Exception e) {}
	}

	// prints:
	public void print() {
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < COLS; col++) {
				System.out.print(this.values[row][col]);
				if(col < COLS) System.out.print(" ");
			}
			System.out.println();
		}
	}
	public void printBytesDetails() {
		byte[] b = this.blockToBytes();
		Bases.printBytesDetails(b);
	}

	//	********* Additional Functions ********* //
	private String getValue(int row, int col) {
		return this.values[row][col];
	}
	private void swapValues(int i, int j) {
		String temp = this.values[i][j];
		this.values[i][j] = this.values[j][i];
		this.values[j][i] = temp;
	}
	private void increaseIndex() {
		if(this.lastRow < ROWS) {
			if(this.lastCol == ROWS - 1) {
				this.lastCol = 0;
				this.lastRow++;
			}
			else if(lastCol < COLS - 1) {
				this.lastCol++;
			}
		}
	}
	public byte[] blockToBytes() {
		byte[] res = new byte[this.getSize()];
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < COLS; col++) {
				String hex = this.getValue(row, col);
				byte b = Bases.hexToByte(hex);
				res[row * ROWS + col] = b;
			}
		}
		return res;
	}

}
