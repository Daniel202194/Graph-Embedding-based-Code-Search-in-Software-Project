
public class DivideOp extends BinaryOp {
	public double operate(double left, double right) {
		return left/right;
	}
	public double getPrecedence() {
		return 2;
	}
	public String toString() {
		return "/";
	}
}
