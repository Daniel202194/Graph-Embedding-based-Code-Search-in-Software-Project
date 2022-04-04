public class ExpressionTree {

    public TreeNode root;
    private StackAsArray stack;

    public ExpressionTree() {
        root = new TreeNode("", null, null);
        stack = new StackAsArray();
    }

    public ExpressionTree(String postfix) {
        stack = new StackAsArray();
        if (root != null)
            BuildExpressionTree(postfix);
        else {
            root = new TreeNode("", null, null);
            BuildExpressionTree(postfix);

        }

    }

    void BuildExpressionTree(String postfixExp) {
        TreeNode tempLeft=null;
        TreeNode tempRigth=null;
    if (postfixExp==null)
        return;

     String[] string_array=postfixExp.split(" ");
     int length = string_array.length;
     for(int i =0;i<length;i++){
         if(string_array[i].matches(".*\\d+.*")){
             double temp1=Double.parseDouble(string_array[i]);
             String tempitemp=Double.toString(temp1);
             TreeNode temp=new TreeNode(tempitemp);
             stack.push(temp);

         }
         else if(string_array[i].equals("+")||string_array[i].equals("-")||string_array[i].equals("^")||string_array[i].equals("*")||string_array[i].equals("/")){
             String left="";
             String rigth="";
             if(!stack.isEmpty()) {
                  tempRigth = (TreeNode) stack.pop();
                 rigth = (String) tempRigth.data;
             }
             TreeNode rigthNode=new TreeNode(rigth);
             if(!stack.isEmpty()) {
                  tempLeft = (TreeNode) stack.pop();
                  left = (String) tempLeft.data;
             }

             TreeNode leftNode=new TreeNode(left);
             TreeNode newNode=new TreeNode(string_array[i]);
             newNode.left=tempLeft;
             newNode.right=tempRigth;
             stack.push(newNode);
         }

     }
     root=(TreeNode)stack.pop();
    }
}

