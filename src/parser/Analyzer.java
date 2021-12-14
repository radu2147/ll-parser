package parser;
import java.util.Map;
import java.util.Stack;

import static parser.Grammar.EPSILON;

public class Analyzer {
    Stack<String> inputBand = new Stack<>();
    Stack<String> workingStack = new Stack<>();
    Map<String, Map<String,ProdRule>> table;
    String sequence;
    String startingSymbol;

    public Analyzer(Map<String, Map<String,ProdRule>> table, String startingSymbol, String sequence) {
        this.table = table;
        this.startingSymbol = startingSymbol;
        this.sequence = sequence;
    }

    public void analyze() {
        inputBand.push("$");
        workingStack.push("$");

        for (int i = sequence.length() - 1; i >= 0; i--) {
            char c = sequence.charAt(i);
            String str = String.valueOf(c);
            inputBand.push(str);
        }

        workingStack.push(startingSymbol);

        boolean ended = false;

        while (!ended) {
            String line;
            String column;

            line = workingStack.peek();
            column = inputBand.peek();

            ProdRule tableCell = null;
            if(table.containsKey(line)) {
                if (table.get(line).containsKey(column)) {
                    tableCell = table.get(line).get(column);
                }
            }

            if (tableCell != null) {
                String prodRight = tableCell.getRight();

                if (prodRight != null) {
                    workingStack.pop();
                    if(!prodRight.equals(EPSILON)) {
                        for (int i = prodRight.length() - 1; i >= 0; i--) {
                            char c = prodRight.charAt(i);
                            String str = String.valueOf(c);
                            workingStack.push(str);
                        }
                    }
                }
                else {
                    String prodLeft = tableCell.getLeft();

                    if (prodLeft.equals("pop")) {
                        inputBand.pop();
                        workingStack.pop();
                    }
                    else if (prodLeft.equals("acc")) {
                        System.out.println("Secventa apartine limbajului generat de gramatica!");
                        ended = true;
                    }
                }
            }
            else {
                System.out.println("Secventa nu apartine limbajului generat de gramatica!");
                ended = true;
            }
        }
    }
}
