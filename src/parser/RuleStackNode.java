package parser;

import exceptions.GrammarCompilerNotInstanciatedException;
import language.GrammarCompiler;

import java.util.List;

public class RuleStackNode{

    String alphabet_member;
    List<List<String>> posible_rules;
    RuleStackNode pop_until;


    public RuleStackNode(String alphabet_member){
        try {
            this.alphabet_member = alphabet_member;
            this.pop_until = null;

            if(this.alphabet_member != null)
                if (GrammarCompiler.getInstance().getNon_terminals().contains(this.alphabet_member))
                    this.posible_rules = GrammarCompiler.getInstance().getNon_terminal_rules().get(alphabet_member);
                else
                    System.out.println("Terminal Element first introduced on stack");
        }
        catch (GrammarCompilerNotInstanciatedException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    public RuleStackNode(String alphabet_member, RuleStackNode pop_until){
        try {
            this.alphabet_member = alphabet_member;
            this.pop_until = pop_until;
            if (GrammarCompiler.getInstance().getNon_terminals().contains(this.alphabet_member))
                this.posible_rules = GrammarCompiler.getInstance().getNon_terminal_rules().get(alphabet_member);
            else
                System.out.println("Terminal Element first introduced on stack");
        }
        catch (GrammarCompilerNotInstanciatedException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public RuleStackNode(RuleStackNode copy) {
        this.alphabet_member = copy.alphabet_member;
        this.posible_rules = copy.posible_rules;
        this.pop_until = copy.pop_until;
    }

    @Override
    public String toString() {
        return '(' + String.valueOf(alphabet_member) + ", PopUntil: "
                + pop_until.alphabet_member + ",rules: "
                + (posible_rules != null ? posible_rules.size() : null) + ")" ;
    }
}
