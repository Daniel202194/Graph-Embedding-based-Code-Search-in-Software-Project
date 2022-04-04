
import java.net.StandardSocketOptions;
import java.util.EmptyStackException;

/**
 * This is a testing framework. Use it extensively to verify that your code is working
 * properly.
 */
public class Tester {

	private static boolean testPassed = true;
	private static int testNum = 0;

	/**
	 * This entry function will test all classes created in this assignment.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {

		/* TODO - write a function for each class */
		// Each function here should test a different class.

		//BinaryOp
		testAddOp();	
		testSubtractOp();
		testMultiplyOp();
		testDivideOp();
		testPowOp();
		//Brackets
		testOpeningBracket();	
		testClosingBracket();
		//ValueToken
		testValueToken();
		//Calculators
		testStackCalculator();
		testTreeCalculator();

		// Notifying the user that the code have passed all tests. 
		if (testPassed) {
			System.out.println("All " + testNum + " tests passed!");
		}
	}

	/**
	 * This utility function will count the number of times it was invoked. 
	 * In addition, if a test fails the function will print the error message.  
	 * @param exp The actual test condition
	 * @param msg An error message, will be printed to the screen in case the test fails.
	 */
	private static void test(boolean exp, String msg) {
		testNum++;

		if (!exp) {
			testPassed = false;
			System.out.println("Test " + testNum + " failed: "  + msg);
		}
	}

	/**
	 * Checks the AddOp class.
	 */
	private static void testAddOp() {
		AddOp op = new AddOp();
		test((op.getPrecedence() == 1.0), "The answer should be 1.0 .");
		test((op.toString().equals("+")), "The string + should be printed.");
		test((op.operate(1.0 , 2.5) == 3.5), "The answer should be 3.5 .");
	}
	/**
	 * Checks the SubtrackOp class.
	 */
	private static void testSubtractOp() {
		SubtractOp op = new SubtractOp();
		test((op.getPrecedence() == 1.0), "The answer should be 1.0 .");
		test((op.toString().equals("-")), "The string * should be printed.");
		test((op.operate(5.0 , 2.0) == 3.0), "The answer should be 3.0 .");
	}
	/**
	 * Checks the MultiplyOp class.
	 */
	private static void testMultiplyOp() {
		MultiplyOp op = new MultiplyOp();
		test((op.getPrecedence() == 2.0), "The answer should be 2.0 .");
		test((op.toString().equals("*")), "The string * should be printed.");
		test((op.operate(5.0 , 2.0) == 10.0), "The answer should be 10.0 .");
	}
	/**
	 * Checks the DivideOp class.
	 */
	private static void testDivideOp() {
		DivideOp op = new DivideOp();
		test((op.getPrecedence() == 2.0), "The answer should be 1.0 .");
		test((op.toString().equals("/")), "The string / should be printed.");
		test((op.operate(10.0 , 2.0) == 5), "The answer should be 5 .");
	}
	/**
	 * Checks the PowOp class.
	 */
	private static void testPowOp() {
		PowOp op = new PowOp();
		test((op.getPrecedence() == 3.0), "The answer should be 3.0 .");
		test((op.toString().equals("^")), "The string ^ should be printed.");
		test((op.operate(2.0 , 3.0) == 8.0), "The answer should be 8.0 .");
	}
	/**
	 * Checks the OpeningBracket class.
	 */
	private static void testOpeningBracket() {
		OpeningBracket op = new OpeningBracket();
		test((op.toString().equals("(")), "The string ( should be printed.");
	}
	/**
	 * Checks the ClosingBracket class.
	 */
	private static void testClosingBracket() {
		ClosingBracket op = new ClosingBracket();
		test((op.toString().equals(")")), "The string ) should be printed.");
	}
	/**
	 * Checks the ValueToken class.
	 */
	private static void testValueToken() {
		ValueToken val1= new ValueToken(8);
		test(val1.getValue() ==  8.0, "The output of \"8\" should be  8.0");
		test((val1.toString().equals("8.0")), "The string 8.0 should be printed.");

		ValueToken val2= new ValueToken(-2.568);
		test(val2.getValue() ==  -2.568, "The output of \"-2.568\" should be  -2.568");
		test((val2.toString().equals("-2.568")), "The string -2.568 should be printed.");
	}
	/**
	 * Checks the StackCalculator class.
	 */
	private static void testStackCalculator() {
		StackCalculator pc = new StackCalculator();
		String postExp = pc.infixToPostfix("2 + 3");
		test(postExp.equals("2.0 3.0 +") , "The output of \"2 3 -\" should be  2.0 3.0 +");
		double result = pc.evaluate(postExp);
		test(result ==  5.0, "The output of \"2 3 +\" should be  5.0");

		StackCalculator pc1 = new StackCalculator();
		String postExp1 = pc1.infixToPostfix("( ( ( ( 1 * ( 2 + 3 ) ) - 3 ) + 4 ) * 5 )");
		test(postExp1.equals("1.0 2.0 3.0 + * 3.0 - 4.0 + 5.0 *") , "The output of \"( ( ( ( 1 * ( 2 + 3 ) ) - 3 ) + 4 ) * 5 )\" should be 1.0 2.0 3.0 + * 3.0 - 4.0 + 5.0 *");
		double result1 = pc1.evaluate(postExp1);
		test(result1 ==  30.0, "The output of \"2 3 +\" should be  5.0");

		StackCalculator pc2 = new StackCalculator();
		String postExp2 = pc2.infixToPostfix("( 7 + 3 ) * ( 18 - 2 )");
		test(postExp2.equals("7.0 3.0 + 18.0 2.0 - *") , "The output of \"( 7 + 3 ) * ( 18 - 2 ) -\" should be  7.0 3.0 + 18.0 2.0 - *");
		double result2 = pc2.evaluate(postExp2);
		test(result2 ==  160, "The output of \"( 7 + 3 ) * ( 18 - 2 )\" should be  160");

		StackCalculator pc3 = new StackCalculator();
		String postExp3 = pc3.infixToPostfix("( ( 6 - ( 2 + 3 ) ) * ( 3 + 8 / 2 ) ) ^ 2 + 3");
		test(postExp3.equals("6.0 2.0 3.0 + - 3.0 8.0 2.0 / + * 2.0 ^ 3.0 +") , "The output of \"( ( 6 - ( 2 + 3 ) ) * ( 3 + 8 / 2 ) ) ^ 2 + 3\" should be  6.0 2.0 3.0 + - 3.0 8.0 2.0 / + * 2.0 ^ 3.0 +");
		double result3 = pc3.evaluate(postExp3);
		test(result3 ==  52, "The output of \"6 2 3 + - 3 8 2 / + * 2 ^ 3 +\" should be  52");

		StackCalculator pc4 = new StackCalculator();
		String postExp4 = pc4.infixToPostfix("( ( 4 + ( 3 * 7 ) ) - ( 5 / ( 3 + 4 ) ) ) + 6");
		test(postExp4.equals("4.0 3.0 7.0 * + 5.0 3.0 4.0 + / - 6.0 +") , "The output of \"( ( 4 + ( 3 * 7 ) ) - ( 5 / ( 3 + 4 ) ) ) + 6 -\" should be  4.0 3.0 7.0 * + 5.0 3.0 4.0 + / - 6.0 +");
		double result4 = pc4.evaluate(postExp4);
		test(result4 == 30.285714285714285, "The output of \"( ( 4 + ( 3 * 7 ) ) - ( 5 / ( 3 + 4 ) ) ) + 6\" should be 30.285714285714285");

		StackCalculator pc5 = new StackCalculator();
		String postExp5 = pc5.infixToPostfix("( ( 2 ^ 3 ) / ( 4 * 2 ) ) - 7");
		test(postExp5.equals("2.0 3.0 ^ 4.0 2.0 * / 7.0 -") , "The output of \"( ( 4 + ( 3 * 7 ) ) - ( 5 / ( 3 + 4 ) ) ) + 6 -\" should be 2.0 3.0 ^ 4.0 2.0 * / 7.0 -");
		double result5 = pc5.evaluate(postExp5);
		test(result5 ==  -6.0, "The output of \"2 3 +\" should be  5.0");
	}

	private static void testTreeCalculator(){
		TreeCalculator tc = new TreeCalculator();
		double result = tc.evaluate("2 3 +");
		test(result == 5.0, "The output of \"5.0\" should be  5.0");
		String in = tc.getInfix();
		test(in.equals("( 2.0 + 3.0 )") , "The output should be  \"( 2.0 + 3.0 )\"");
		String post = tc.getPostfix();
		test(post.equals("2.0 3.0 +") , "The output should be  \"2.0 3.0 +\"");
		String pre =tc.getPrefix();
		test(pre.equals("+ 2.0 3.0") , "The output should be  \"+ 2.0 3.0\"");

		TreeCalculator tc1 = new TreeCalculator();
		double result1 = tc1.evaluate("1.0 2.0 3.0 + * 3.0 - 4.0 + 5.0 *");
		test(result1 == 30.0, "The output of \"1.0 2.0 3.0 + * 3.0 - 4.0 + 5.0 *\" should be  30.0");
		String in1 = tc1.getInfix();
		test(in1.equals("( ( ( ( 1.0 * ( 2.0 + 3.0 ) ) - 3.0 ) + 4.0 ) * 5.0 )") , "The output should be  \"( ( ( ( 1.0 * ( 2.0 + 3.0 ) ) - 3.0 ) + 4.0 ) * 5.0 )\"");
		String post1 = tc1.getPostfix();
		test(post1.equals("1.0 2.0 3.0 + * 3.0 - 4.0 + 5.0 *") , "The output should be  \"1.0 2.0 3.0 + * 3.0 - 4.0 + 5.0 *\"");
		String pre1 =tc1.getPrefix();
		test(pre1.equals("* + - * 1.0 + 2.0 3.0 3.0 4.0 5.0") , "The output should be  \"* + - * 1.0 + 2.0 3.0 3.0 4.0 5.0\"");

		TreeCalculator tc2 = new TreeCalculator();
		double result2 = tc2.evaluate("7 3 + 18 2 - *");
		test(result2 == 160.0, "The output of \"7 3 + 18 2 - *\" should be 160");
		String in2 = tc2.getInfix();
		test(in2.equals("( ( 7.0 + 3.0 ) * ( 18.0 - 2.0 ) )") , "The output should be  \"( ( 7.0 + 3.0 ) * ( 18.0 - 2.0 ) )\"");
		String post2 = tc2.getPostfix();
		test(post2.equals("7.0 3.0 + 18.0 2.0 - *") , "The output should be  \"7.0 3.0 + 18.0 2.0 - *\"");
		String pre2 =tc2.getPrefix();
		test(pre2.equals("* + 7.0 3.0 - 18.0 2.0") , "The output should be  \"* + 7.0 3.0 - 18.0 2.0\"");

		TreeCalculator tc3 = new TreeCalculator();
		double result3 = tc3.evaluate("6.0 2.0 3.0 + - 3.0 8.0 2.0 / + * 2.0 ^ 3.0 +");
		test(result3 == 52.0, "The output of \"\" should be 52.0");
		String in3 = tc3.getInfix();
		test(in3.equals("( ( ( ( 6.0 - ( 2.0 + 3.0 ) ) * ( 3.0 + ( 8.0 / 2.0 ) ) ) ^ 2.0 ) + 3.0 )") , "The output should be  \"( ( ( ( 6.0 - ( 2.0 + 3.0 ) ) * ( 3.0 + ( 8.0 / 2.0 ) ) ) ^ 2.0 ) + 3.0 )\"");
		String post3 = tc3.getPostfix();
		test(post3.equals("6.0 2.0 3.0 + - 3.0 8.0 2.0 / + * 2.0 ^ 3.0 +") , "The output should be  \"6.0 2.0 3.0 + - 3.0 8.0 2.0 / + * 2.0 ^ 3.0 +\"");
		String pre3 =tc3.getPrefix();
		test(pre3.equals("+ ^ * - 6.0 + 2.0 3.0 + 3.0 / 8.0 2.0 2.0 3.0") , "The output should be  \"+ ^ * - 6.0 + 2.0 3.0 + 3.0 / 8.0 2.0 2.0 3.0\"");

		TreeCalculator tc4 = new TreeCalculator();
		double result4 = tc4.evaluate("4.0 3.0 7.0 * + 5.0 3.0 4.0 + / - 6.0 +");
		test(result4 == 30.285714285714285, "The output of \"\" should be 30.285714285714285");
		String in4 = tc4.getInfix();
		test(in4.equals("( ( ( 4.0 + ( 3.0 * 7.0 ) ) - ( 5.0 / ( 3.0 + 4.0 ) ) ) + 6.0 )") , "The output should be  \"( ( ( 4.0 + ( 3.0 * 7.0 ) ) - ( 5.0 / ( 3.0 + 4.0 ) ) ) + 6.0 )\"");
		String post4 = tc4.getPostfix();
		test(post4.equals("4.0 3.0 7.0 * + 5.0 3.0 4.0 + / - 6.0 +") , "The output should be  \"4.0 3.0 7.0 * + 5.0 3.0 4.0 + / - 6.0 +\"");
		String pre4 =tc4.getPrefix();
		test(pre4.equals("+ - + 4.0 * 3.0 7.0 / 5.0 + 3.0 4.0 6.0") , "The output should be  \"+ - + 4.0 * 3.0 7.0 / 5.0 + 3.0 4.0 6.0\"");

		TreeCalculator tc5 = new TreeCalculator();
		double result5 = tc5.evaluate("2.0 3.0 ^ 4.0 2.0 * / 7.0 -");
		test(result5 == -6.0, "The output of \"\" should be -6.0");
		String in5 = tc5.getInfix();
		test(in5.equals("( ( ( 2.0 ^ 3.0 ) / ( 4.0 * 2.0 ) ) - 7.0 )") , "The output should be  \"( ( ( 2.0 ^ 3.0 ) / ( 4.0 * 2.0 ) ) - 7.0 )\"");
		String post5 = tc5.getPostfix();
		test(post5.equals("2.0 3.0 ^ 4.0 2.0 * / 7.0 -") , "The output should be  \"2.0 3.0 ^ 4.0 2.0 * / 7.0 -\"");
		String pre5 =tc5.getPrefix();
		test(pre5.equals("- / ^ 2.0 3.0 * 4.0 2.0 7.0") , "The output should be  \"- / ^ 2.0 3.0 * 4.0 2.0 7.0\"");
	}
}