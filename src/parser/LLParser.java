package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static parser.Grammar.EPSILON;

public class LLParser {

    Grammar grm;
    Map<String, Map<String,ProdRule>> table;

    public LLParser(Grammar grm){
        this.grm = grm;
        this.table = new HashMap<>();
        for(var key: this.grm.prodRules.keySet()){
            table.put(key, new HashMap<>());
            grm.prodRules.get(key).stream()
                    .map(it -> it.right)
                    .forEach(it -> findTerminals(it).forEach(term -> {
                        if(!table.containsKey(term)){
                            var acc = new HashMap<String, ProdRule>();
                            acc.put(term, new ProdRule("pop", null));
                            table.put(term, acc);
                        }
                    }));
        }
        var acc = new HashMap<String, ProdRule>();
        acc.put("$", new ProdRule("acc", null));
        table.put("$", acc);
    }

    private List<String> findTerminals(String sequence){
        ArrayList<String> rez = new ArrayList<>();
        for(Character ch: sequence.toCharArray()){
            if(!ch.toString().equals(EPSILON) && ch.toString().toLowerCase().equals(ch.toString()))
                rez.add(ch.toString());
        }
        return rez;
    }

    public void constructTable() throws Exception {
        for(var el: this.grm.prodRules.keySet()){
            for(var rule: this.grm.prodRules.get(el)){
                var first = grm.first(String.valueOf(rule.right.charAt(0)));
                for(var terminal: first){
                    if(!terminal.equals(EPSILON)){
                        if(table.get(rule.left).containsKey(terminal)){
                            throw new Exception("Grammar is not LL");
                        }
                        table.get(rule.left).put(terminal, rule);
                    }
                    else{
                        var follow = grm.follow(rule.left);
                        for(var fl: follow){
                            if(table.get(rule.left).containsKey(fl)){
                                throw new Exception("Grammar is not LL");
                            }
                            table.get(rule.left).put(fl, rule);
                        }
                    }
                }
            }
        }
    }

}
