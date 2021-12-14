package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    Map<String, List<ProdRule>> prodRules = new HashMap<>();

    static final String SEPARATOR = "->";
    static final String EPSILON = "#";

    Map<String, Set<String>> first = new HashMap<>();
    Map<String, Set<String>> follow = new HashMap<>();

    String startingSymbol = "";

    public void readGrammarFromFile(String filename){
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {

                String[] data = myReader.nextLine().split(SEPARATOR);
                if(startingSymbol.equals("")){
                    startingSymbol = data[0];
                }
                if(prodRules.containsKey(data[0]))
                    prodRules.get(data[0]).add(new ProdRule(data[0], data[1]));
                else{
                    var mutList = new ArrayList<ProdRule>();
                    mutList.add(new ProdRule(data[0], data[1]));
                    prodRules.put(data[0], mutList);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public Set<String> first(String symbol){
        if(first.containsKey(symbol)){
            return first.get(symbol);
        }
        if(symbol.equals(EPSILON)){
            return Set.of(EPSILON);
        }
        if(symbol.toLowerCase().equals(symbol)){
            return Set.of(symbol);
        }
        if(prodRules.containsKey(symbol)){
            Set<String> fin = new HashSet<>();
            for(var rule: prodRules.get(symbol)) {
                String rightMember = rule.right;
                for (Character ch : rightMember.toCharArray()) {
                    Set<String> rez = first(ch.toString());
                    if(rez.size() == 1 && rez.contains(EPSILON)){
                        fin.add(EPSILON);
                    }
                    else {
                        fin.addAll(rez.stream().filter(it -> !it.equals(EPSILON)).collect(Collectors.toList()));
                        if (!rez.contains(EPSILON)) {
                            break;
                        }
                    }
                }
            }
            first.put(symbol, fin);
            return fin;
        }
        System.err.println("Unknown symbol");
        return Set.of();
    }

    public Set<String> follow(String nonTerminal){
        Set<String> rez = new HashSet<>();
        for(var val: prodRules.entrySet()){
            for(var right: val.getValue()) {
                for (int i = 0; i < right.right.length(); i++) {
                    if (right.right.charAt(i) == nonTerminal.charAt(0)) {
                        if (i == right.right.length() - 1) {
                            rez.addAll(follow(val.getKey()));
                        } else {
                            var frst = first(String.valueOf(right.right.charAt(i + 1)));
                            if (frst.contains(EPSILON)) {
                                rez.addAll(frst.stream().filter(it -> !it.equals(EPSILON)).collect(Collectors.toList()));
                                rez.addAll(follow(String.valueOf(right.right.charAt(i + 1))));
                            } else {
                                rez.addAll(frst);
                            }
                        }
                    }
                }
            }
        }
        if(nonTerminal.equals(startingSymbol)){
            rez.add("$");
        }
        return rez;
    }
}
