package parser;

import language.GrammarCompiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuleObj {

    public String alphabet_member;
    public List<List<String>> rules;
    private int popped_words;
    List<String> last_used_rule;

    public RuleObj(String alphabet_member, GrammarCompiler instance) {
        this.alphabet_member = alphabet_member;
        this.popped_words = 0;
        last_used_rule = new ArrayList<>();
        if(instance.getNon_terminals().contains(this.alphabet_member))
            this.rules = new ArrayList<>(instance.getNon_terminal_rules().get(this.alphabet_member));
        else
            this.rules = Collections.emptyList();
    }
    public RuleObj(String alphabet_member, int popped_words, GrammarCompiler instance) {
        this.alphabet_member = alphabet_member;
        this.popped_words = popped_words;
        last_used_rule = new ArrayList<>();
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

    public void increment_popped_words(){
        this.popped_words++;
    }
    public void reset_popped_words(){
        this.popped_words = 0;
    }
    public int getPopped_words(){
        return this.popped_words;
    }

    @Override
    public String toString() {
        return '(' + alphabet_member + ')';// + ',' + "rules:" + rules + ')';
    }
}

