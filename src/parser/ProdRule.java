package parser;

import java.util.List;

public class ProdRule {

    String left;
    List<String> right;

    public String getLeft() {
        return left;
    }

    public List<String> getRight() {
        return right;
    }

    public ProdRule(String leftMember, List<String> rightMember){
        this.left = leftMember;
        this.right = rightMember;
    }
}
