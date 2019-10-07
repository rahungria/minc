package parser;

import exceptions.GrammarCompilerNotInstanciatedException;
import language.GrammarCompiler;
import language.Terminal;
import token.Scanner;
import token.Token;

import java.util.*;

public class Parser {

    private Collection<Token> tokens;
    private Scanner scanner;
    private GrammarCompiler grammarCompiler;
    private Stack<String> non_terminals_stack;
    private Set<String> non_terminals;
    private Hashtable<String, List<List<String>>> non_terminal_rules;

    public Parser(Collection<Token> tokens) {
        try {
            this.tokens = tokens;
            non_terminals_stack = new Stack<>();

            scanner = Scanner.getInstance();
            grammarCompiler = GrammarCompiler.getInstance();

            non_terminals = new HashSet<>(grammarCompiler.getNon_terminals());
            non_terminal_rules = new Hashtable<>(grammarCompiler.getNon_terminal_rules());

        } catch (GrammarCompilerNotInstanciatedException e){

            System.out.println(e.getMessage() + "\nfinding alternative at: ./grammar/minc.grammar");
            grammarCompiler = GrammarCompiler.getInstance("./grammar/minc.grammar");
        }
    }

    public CST parse(){

        CST root = new CST(null, "S");
        CST focus = root;
        Token word = scanner.nextToken();
        List<String> current_rule = null;
        BacktrackRuleTree<List<String>> brtRoot = new BacktrackRuleTree<>(null,null);
        BacktrackRuleTree<List<String>> focusBRT = brtRoot;
        int ammount_pushed_on_stack = 0;
        int loopcount = 0;

        non_terminals_stack.clear();
        non_terminals_stack.push(null);

        while(loopcount < 50){


            //DEBUG CONSOLE LOG
            System.out.println("loop: " + loopcount);
            System.out.println(String.format("%-20s%40s\n%-20s%40s","Focus:", focus.alphabet_element,"Word:", word.terminal.name() + "(" + word.lexeme + ")"));
            System.out.println(String.format("%-20s%40s", "Current Rule:", current_rule));
            System.out.println(String.format("%-20s%40s", "Stack:", non_terminals_stack));

            //IF FOCUS IS A NON TERMINAL
            if (non_terminals.contains(focus.alphabet_element)){

                System.out.println("Focus is Non-Terminal...");

                //Builds the backtrack tree for the given focus and determines the current rule
                //current_rule = non_terminal_rules.get(focus.alphabet_element)
                if (focusBRT.child == null)
                    focusBRT.add_child(new BacktrackRuleTree<> (focusBRT, null));

                focusBRT = focusBRT.child;

                if (focusBRT.rule == null)
                    focusBRT.addRules(non_terminal_rules.get(focus.alphabet_element));

                current_rule = focusBRT.rule;

                //System.out.println(String.format("%-20s%40s", "Updated Rule:", current_rule));

                //append nodes to focus
                List<CST> focus_children = new ArrayList<>();
                for(String alph_elem : current_rule)
                    focus_children.add(new CST(alph_elem));
                //System.out.println(String.format("%-20s%40s", "Focus CST Children:", focus_children));
                focus.children_CST = focus_children;

                //Add entire rule in reverse order to the stack and update focus and
                //captures the ammount of values pushed on the stack so it is possible
                //to remove the elements from the stack when backtracking
                for (int i = current_rule.size() - 1; i >= 0 ; i--){
                    non_terminals_stack.push(current_rule.get(i));
                }
                ammount_pushed_on_stack = current_rule.size() - 1;

                focus = new CST(non_terminals_stack.pop());

//                System.out.println(String.format("%-20s%40s","Updated Stack:", non_terminals_stack));
//                System.out.println(String.format("%-20s%40s","Updated Focus:", focus.alphabet_element));
            }

            //IF FOCUS WORD MATCHES FOCUS
            else if(focus.alphabet_element.equals(word.terminal.name())){

                System.out.println("Focus matches the Word! >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println(String.format("%-20s%40s","Tokens:",scanner.getTokens()));

                focusBRT = move_up_and_drop_rules(focusBRT);

                word = scanner.nextToken();
                focus = new CST(non_terminals_stack.pop());
                System.out.println(String.format("%-20s%40s","New Tokens:",scanner.getTokens()));
            }
            //matches Epsilon element of the language
            //THE ONLY HARD CODE I'LL ALLOW MYSELF
            else if (focus.alphabet_element.equals(Terminal.epsilon.name())){

                System.out.println("EPSILON detected...\nAdvancing Parser without advancing token...");
//                focusBRT = focusBRT.drop_tree();
                focusBRT = move_up_and_drop_rules(focusBRT);
//                if (focusBRT.rule.size() == 0)
//                    focusBRT = focusBRT.parent;

                String new_focus_rule = non_terminals_stack.pop();
                focus = new CST(new_focus_rule);
                focusBRT.addRules(non_terminal_rules.get(new_focus_rule));
                System.out.println("DEBUG FOCUS CBT:\n" + focusBRT.tree_info());

            }
            //Detect Input Acceptance (actual token = eof and focus is empty)
            else if (word.terminal == Terminal.eof && focus == null){

                System.out.println("ACCEPTED INPUT! POGGERS!!\n");
                return root;
            }
            else{
                System.out.println("\n!!!!!!!!!!RULE FAILED!!!!!!!!!!\nBACKTRACKING...");
                //System.out.println("\n\nPRINTING RULE TREE:\n");
                //System.out.println(brtRoot.tree_info());

                if (focusBRT.nextBrother != null){
                    for (int i = 0; i < ammount_pushed_on_stack; i++)
                        non_terminals_stack.pop();
                }
                focusBRT = focusBRT.pop();
                focus = new CST(focusBRT.rule.get(0));
                //System.out.println(brtRoot.tree_info());
                //break;
                //backtrack
            }
            System.out.println("\n");
            System.out.println("DEBUG RULE TREE:\n");
            System.out.println(brtRoot.tree_info());
            System.out.println("FOCUS RULE:\t" + focusBRT.rule);
            System.out.println();
            loopcount++;
        }
        return null;
    }

    private BacktrackRuleTree move_up_and_drop_rules(BacktrackRuleTree<List<String>> focusBRT){
        focusBRT = focusBRT.drop_tree();
        List<String> temp = new ArrayList<>(focusBRT.rule);
        if (temp.size() > 0)
            temp.remove(0);
        while (focusBRT.rule.size() == 0){
            focusBRT = focusBRT.drop_tree();
        }

        focusBRT.rule = temp;
        return focusBRT;
    }
}
