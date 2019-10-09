package parser;

import language.GrammarCompiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuleObj {

    public String alphabet_member;
    public List<List<String>> rules;
    public boolean popped_word;

    public RuleObj(String alphabet_member, GrammarCompiler instance) {
        this.alphabet_member = alphabet_member;
        this.popped_word = false;
        if(instance.getNon_terminals().contains(this.alphabet_member))
            this.rules = new ArrayList<>(instance.getNon_terminal_rules().get(this.alphabet_member));
        else
            this.rules = Collections.emptyList();
    }
    public RuleObj(String alphabet_member, boolean popped_word, GrammarCompiler instance) {
        this.alphabet_member = alphabet_member;
        this.popped_word = popped_word;
        if(instance.getNon_terminals().contains(this.alphabet_member))
            this.rules = new ArrayList<>(instance.getNon_terminal_rules().get(this.alphabet_member));
        else
            this.rules = Collections.emptyList();
    }


    public List<String> pop_rule(){
        if (rules.size() > 0)
            return new ArrayList<>(rules.remove(0));
        else
            return Collections.emptyList();
    }

    @Override
    public String toString() {
        return '(' + alphabet_member + ')';// + ',' + "rules:" + rules + ')';
    }
}

