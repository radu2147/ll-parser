import lexer.Lexer;
import parser.Analyzer;
import parser.Grammar;
import parser.LLParser;
import parser.ProdRule;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Map<String, ProdRule>> table;

        var lexer = new Lexer();
        var el = lexer.analyzeWithoutSpaces("C:\\Users\\RADU\\Desktop\\my_projects\\facultate\\LLParser\\main.txt");
        var grm = new Grammar();
        grm.readGrammarFromFile("C:\\Users\\RADU\\Desktop\\my_projects\\facultate\\LLParser\\test1.txt");
        var startingSymbol = grm.getStartingSymbol();
        var parser = new LLParser(grm);

        try {
            parser.constructTable();
            table = parser.getTable();
            Analyzer analyzer = new Analyzer(table, startingSymbol, List.of("if", "c"));
            analyzer.analyze();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("DONE");
    }
}
