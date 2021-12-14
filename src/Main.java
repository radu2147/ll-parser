import parser.Grammar;
import parser.LLParser;

public class Main {
    public static void main(String[] args) {
        var grm = new Grammar();
        grm.readGrammarFromFile("C:\\Users\\RADU\\Desktop\\my_projects\\facultate\\LLParser\\test1.txt");
        System.out.println(grm.follow("A"));
        var parser = new LLParser(grm);
        try {
            parser.constructTable();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("DONE");

    }
}
