import parser.Grammar;

public class Main {
    public static void main(String[] args) {
        var grm = new Grammar();
        grm.readGrammarFromFile("C:\\Users\\RADU\\Desktop\\my_projects\\facultate\\LLParser\\test1.txt");
        grm.follow("A").forEach(System.out::println);
    }
}
