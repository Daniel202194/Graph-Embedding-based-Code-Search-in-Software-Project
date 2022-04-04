public class TreeCalculator implements Calculator {


     public ExpressionTree tree;


     public TreeCalculator(){
         ExpressionTree tree=new ExpressionTree();
     }
    @Override
    /**
     * this function gets a string and evaluate the real result of the string given,convert it to double and returns it
     */
    public double evaluate(String expr) {
        if(tree==null)
            tree=new ExpressionTree(expr);

        return getRealResult(tree.root);

    }

    /**
     * this function is a help function to the evaluate function that gets a node and returns the amount acording to the node given
     * @param node part of the tree we need to evaluate
     * @return the real value of the tree
     */
    public double getRealResult(TreeNode node){
         double result=0;
         if (node==null)
             return result;
         if(node.data.equals("+")||node.data.equals("-")||node.data.equals("*")||node.data.equals("^")||node.data.equals("/")) {
             double op1 = getRealResult(node.left);
             double op2=getRealResult(node.right);
             if(node.data.equals("-")) {
                 result = op1 - op2;
             }
             if(node.data.equals("+")) {
                 result = op1 + op2;
             }
             if(node.data.equals("^")) {
                 result = Math.pow(op1,op2);
             }
             if(node.data.equals("*")) {
                 result = op1 * op2;
             }
              if(node.data.equals("/")){
                      result =(op1/op2);
                 }


         }
         else{
             result=Double.parseDouble((String)node.data);
         }
         return result;

    }

    /**
     * first inOrder function for get infix
     * @param root is the node in the tree that you want to check his info
     * @param str the string for the recursion
     * @return the infix string in the expiration tree
     */
    public String inOrder1(TreeNode root,String str) {
        String left_side="";
        String rigth_side="";
        if(root.left!=null)
            left_side=" ("+inOrder1(root.left,str);
        if(root.right!=null)
            rigth_side=inOrder1(root.right,str)+" )";
        return (left_side+" "+(String)root.data+rigth_side);

        }
    /**
     * second inOrder function for get postfix
     * @param root is the node in the tree that you want to check his info
     * @param str the string for the recursion
     * @return the infix string in the expiration tree
     */
    public String inOrder2(TreeNode root,String str) {
        String left_side="";
        String rigth_side="";
        if(root.left!=null)
            left_side=" "+inOrder2(root.left,str)+" ";
        if(root.right!=null)
            rigth_side=inOrder2(root.right,str)+" ";
        return (left_side+rigth_side +(String)root.data);

    }
    /**
     * second inOrder function for get prefix
     * @param root is the node in the tree that you want to check his info
     * @param str the string for the recursion
     * @return the infix string in the expiration tree
     */
    public String inOrder3(TreeNode root,String str) {
        String left_side="";
        String rigth_side="";
        if(root.left!=null)
            left_side=" "+inOrder3(root.left,str)+" ";
        if(root.right!=null)
            rigth_side=inOrder3(root.right,str)+" ";
        return ((String)root.data + left_side+rigth_side);

    }

    /**
     * this function returns the infix expiration in the given tree
     * @return the infix string
     */
     public String getInfix(){
        String tempString="";
         String res= inOrder1(tree.root,tempString);
         //res= res.substring(2,res.length()-2);
         tempString=res.substring(0,1);
         if(tempString.equals(" "))
             res=res.substring(1);
         tempString=res.substring(res.length()-1);
         if(tempString.equals(" "))
             res=res.substring(0,res.length()-1);
         return res;

     }

    /**
     * this function returns the prefix expiration in the given tree
     * @return the infix string
     */
     public String getPrefix() {
         String tempString = "";
         boolean whileLoop1 = false;
         boolean whileLoop2 = false;
         String res = inOrder3(tree.root, tempString);
         // tempString=res.substring(0,1);
         //if(tempString.equals("+")||tempString.equals("-")||tempString.equals("^")||tempString.equals("*")||tempString.equals("/")) {
         //  res = res.substring(2);
         //res = res.substring(0, res.length() - 2);
         //res = tempString + res;
         //}
         String[] stringArray = res.split(" ");
         res = "";
         for (int i = 0; i < stringArray.length; i++) {
             if (!stringArray[i].equals(""))
                 res = res + " " + stringArray[i];
         }
         while (!whileLoop1) {
             tempString = res.substring(0, 1);
             if (tempString.equals(" ")) {
                 res = res.substring(1);
             } else {
                 whileLoop1 = true;
             }
         }
         while (!whileLoop2) {
             tempString = res.substring(res.length() - 1);
             if (tempString.equals(" ")) {
                 res = res.substring(res.length() - 1);
             } else {
                 whileLoop2 = true;
             }
         }
         return res;
     }



     public String getPostfix(){
         boolean whileLoop=false;
         String tempString=" ";
         String res= inOrder2(tree.root,tempString);
         String[] stringArray=res.split(" ");
         res="";
         for(int i=0;i<stringArray.length;i++){
             if(!stringArray[i].equals(""))
                    res=res+" "+stringArray[i];
         }

         while(!whileLoop) {
             tempString = res.substring(0, 1);
             if (tempString.equals(" "))
                 res = res.substring(1);
             else{
                 whileLoop=true;
             }
         }
    
         return res;
     }

}
