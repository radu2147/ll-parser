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

    public Grammar(){
    }

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
                    prodRules.get(data[0]).add(new ProdRule(data[0], List.of(data[1].split("\s"))));
                else{
                    var mutList = new ArrayList<ProdRule>();
                    mutList.add(new ProdRule(data[0], List.of(data[1].split("\s"))));
                    prodRules.put(data[0], mutList);
                    follow.put(data[0], new HashSet<>());
                }
            }
            var lst = new HashSet<String>();
            lst.add("$");
            follow.put(startingSymbol, lst);
            computeFollow();
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private boolean isNonterminal(String chr){
        return ((Character)chr.charAt(0)).toString().matches("[A-Z]");
    }

    private void computeFollow(){
        var changed = true;
        while(changed){
            changed = false;
            for(var val: prodRules.entrySet()){
                for(var right: val.getValue()) {
                    for (int i = 0; i < right.right.size(); i++) {
                        if (isNonterminal(right.right.get(i))) {
                            if (i == right.right.size() - 1) {
                                if(!follow.get(String.valueOf(right.right.get(i))).containsAll(follow.get(val.getKey()))){
                                    changed = true;
                                }
                                follow.get(String.valueOf(right.right.get(i))).addAll(follow.get(val.getKey()));
                            } else {
                                var frst = first(String.valueOf(right.right.get(i + 1)));
                                if (frst.contains(EPSILON)) {
                                    if(!follow.get(String.valueOf(right.right.get(i))).containsAll(follow.get(val.getKey())) && !follow.get(String.valueOf(right.right.get(i))).containsAll(frst.stream().filter(it -> !it.equals(EPSILON)).collect(Collectors.toList()))){
                                        changed = true;
                                    }
                                    follow.get(String.valueOf(right.right.get(i))).addAll(frst.stream().filter(it -> !it.equals(EPSILON)).collect(Collectors.toList()));
                                    if(follow.containsKey(String.valueOf(right.right.get(i)))) {
                                        follow.get(String.valueOf(right.right.get(i))).addAll(follow.get(val.getKey()));
                                    }
                                } else {
                                    if(!follow.get(String.valueOf(right.right.get(i))).containsAll(frst)){
                                        changed = true;
                                    }
                                    follow.get(String.valueOf(right.right.get(i))).addAll(frst);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Set<String> firstNerec(String sym){
        var st = new Stack<String>();
        st.push(sym);
        while(!st.empty()){
            var symbol = st.pop();
            if(first.containsKey(symbol)){
                return first.get(symbol);
            }
            if(symbol.equals(EPSILON) || symbol.toLowerCase().equals(symbol)){
                first.put(symbol, Set.of(symbol));
            }
            if(prodRules.containsKey(symbol)){
                Set<String> fin = new HashSet<>();
                var stop = false;
                for(var rule: prodRules.get(symbol)) {
                    List<String> rightMember = rule.right;
                    for (String ch : rightMember) {
                        if(first.containsKey(ch)) {
                            Set<String> rez = first.get(ch);
                            if (rez.size() == 1 && rez.contains(EPSILON)) {
                                fin.add(EPSILON);
                            } else {
                                fin.addAll(rez.stream().filter(it -> !it.equals(EPSILON)).collect(Collectors.toList()));
                                if (!rez.contains(EPSILON)) {
                                    break;
                                }
                            }
                        }
                        else{
                            st.push(symbol);
                            st.push(ch.toString());
                            stop = true;
                            break;
                        }
                    }
                    if(stop){
                        break;
                    }
                }
                first.put(symbol, fin);
            }
        }
        return first.get(sym);
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
                List<String> rightMember = rule.right;
                for (String ch : rightMember) {
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
        return follow.get(nonTerminal);
    }

    public String getStartingSymbol() {
        return startingSymbol;
    }
}
