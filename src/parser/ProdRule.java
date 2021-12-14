package parser;

public class ProdRule {

    String left;
    String right;

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }

    public ProdRule(String leftMember, String rightMember){
        this.left = leftMember;
        this.right = rightMember;
    }
}
