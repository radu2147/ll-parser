package parser;

import java.util.HashMap;
import java.util.Map;

public class LLParser {

    Grammar grm;
    Map<String, Map<String,ProdRule>> table;

    public LLParser(Grammar grm){
        this.grm = grm;
        this.table = new HashMap<>();
        for(var key: this.grm.prodRules.keySet()){
            table.put(key, new HashMap<>());
        }
    }

    public void constructTable() throws Exception {
        for(var el: this.grm.prodRules.keySet()){
            for(var rule: this.grm.prodRules.get(el)){
                var first = grm.first(String.valueOf(rule.right.charAt(0)));
                for(var terminal: first){
                    if(!terminal.equals(Grammar.EPSILON)){
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
