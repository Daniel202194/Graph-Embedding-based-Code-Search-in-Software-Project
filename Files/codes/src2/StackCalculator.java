
public class StackCalculator implements Calculator {
	/**
	 * This function converts infix expression to postfix one.
	 * @param string of infix expression
	 * @return string of infix expression
	 */
	public String infixToPostfix(String string) {
		String str="";
		StackAsArray stack= new StackAsArray();
		ExpTokenizer expr=new ExpTokenizer(string);

		while(expr.hasMoreElements()) {
			Object token=expr.nextElement();
			if (token instanceof OpeningBracket) {
				stack.push(token);
			}
			else if (token instanceof ClosingBracket) {
				Object temp=stack.pop();
				while (!(temp instanceof OpeningBracket)) {
					str=str+" "+temp.toString();
					temp=stack.pop();
				}
			}
			else if (token instanceof BinaryOp) {
				boolean ans = !stack.isEmpty();
				while(ans) {
					Object temp=stack.pop();
					if(temp instanceof BinaryOp) {
						boolean ans2=((BinaryOp)temp).getPrecedence()>= ((BinaryOp)token).getPrecedence();
						if(ans2) {
							str=str+" "+temp;
							ans = !stack.isEmpty();
						}
						else {
							stack.push(temp);
							ans=false;
						}

					}
					else if(temp instanceof ValueToken) {
						str=str+" "+temp;
						ans = !stack.isEmpty();
					}
					else {
						stack.push(temp);
						ans=false;
					}
				}
				stack.push(token);
			}
			else if(token instanceof ValueToken) {
				if(str=="") {
					str=((ValueToken)token).toString();
				}
				else {
					str=str+" "+((ValueToken)token).toString();
				}
			}
		}	
		while(!(stack.isEmpty())) {
			Object temp=stack.pop();
			str=str+" "+temp.toString();
		}
		return str;
	}

	/**
	 * This function calculates the result of a postfix expression.
	 * @param a postfix expression
	 * @return a double
	 */
	public double evaluate(String expr) {
		StackAsArray stack = new StackAsArray();
		ExpTokenizer exp = new ExpTokenizer(expr);
		while(exp.hasMoreElements()) {
			Object token = exp.nextElement();
			if(token instanceof BinaryOp) {
				ValueToken right= (ValueToken)stack.pop();
				ValueToken left= (ValueToken)stack.pop();
				double result= ((BinaryOp) token).operate(left.getValue(), right.getValue());
				ValueToken ron= new ValueToken(result);
				stack.push(ron);
			}
			else {
				stack.push(token);
			}
		}
		double ans = ((ValueToken)stack.pop()).getValue();
		return ans;
	}

}