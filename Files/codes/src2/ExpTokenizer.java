import java.util.Enumeration;

public class ExpTokenizer extends Object implements Enumeration<Object>  {
    //fields
    private String [] result;
    private int index;

    /** constructor
     * @param exp the expression to tokenize
     */
    public ExpTokenizer(String exp) {
        this.result = exp.split(" ");
        this.index = 0;
    }

    /** returns the next element
     * @return the next element
     */
    public Object nextElement() { //we create for each token the suitable object. there are no other
        CalcToken resultToken = null;
        String token =  nextToken();
        if (token.equals("+"))
            resultToken =  new AddOp();
        else if (token.equals("*"))
            resultToken =  new MultiplyOp();
        else if (token.equals("-"))
            resultToken= new SubtractOp();
        else if (token.equals("/"))
            resultToken= new DivideOp();
        else if (token.equals("^"))
            resultToken= new PowOp();
        else if (token.equals("("))
            resultToken= new OpeningBracket();
        else if (token.equals(")"))
            resultToken= new ClosingBracket();
        else // if the token is none of the above, it has to be a number. therefore, we would create valueToken.
            resultToken = new ValueToken(Double.parseDouble(token));

        return resultToken;
    }

    /** returns if there are any more elements
     * @return true or false
     */
    @Override
    public boolean hasMoreElements() {
        return (this.index != this.result.length);
    }

    /**
     * returns the next token without increasing the index..
     * @return the next token without increasing the index..
     */
    public String peek(){
        String res="";
        res=result[this.index];
        return res;
    }

    /** returns the next token
     * @return the next token
     */
    public String nextToken() {
        String ret;
        ret= this.result[this.index];
        this.index++;
        return ret;
    }

    /** returns if there are any more tokens
     * @return true or false
     */
    public boolean hasMoreTokens() {
        return hasMoreElements();
    }

    /** returns how many tokens have been created
     * @return number of tokens created
     */
    public int countTokens() {
        return (this.result.length - this.index);
    }

}