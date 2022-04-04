
public class ValueToken extends CalcToken {
	private double val;
	ValueToken(double val){
		this.val=val;
	}
	public double getValue() {
		return this.val;
	}
	public String toString() {
		return Double.toString(val);
	}
}
