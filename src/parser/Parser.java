package parser;

import com.sun.javafx.css.Rule;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
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
    private Set<String> non_terminals;
    private Hashtable<String, List<List<String>>> non_terminal_rules;

    public Parser(Collection<Token> tokens) {
        try {
            this.tokens = tokens;
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
        RuleStack new_focus = new RuleStack(root.alphabet_element);
        Token word = scanner.nextToken();
        List<String> current_rule = null;

        RuleStack non_terminals_stack = new RuleStack(root.alphabet_element);
        RuleStack memoryStack = new RuleStack(null);

        int loopcount = 0;

        //non_terminals_stack.push(null);


        while(loopcount < 50){


            //DEBUG CONSOLE LOG
            for (int i =0; i < 70; i++)
                System.out.print('-');

            System.out.println("loop: " + loopcount);
            System.out.println(String.format("%-20s%50s\n%-20s%50s","Focus:", new_focus,"Word:", word.terminal.name() + "(" + word.lexeme + ")"));
            System.out.println(String.format("%-20s%50s", "Current Rule:", current_rule));
            System.out.println(String.format("%-20s%50s", "Stack:", non_terminals_stack));
            System.out.println(String.format("%-20s%50s", "Memory:", memoryStack));

            for (int i =0; i < 70; i++)
                System.out.print('-');
            System.out.println();

            //END OF DEBUG CONSOLE LOG


            //IF FOCUS IS A NON TERMINAL
            if (non_terminals.contains(new_focus.peek_alphabet_member())){

                System.out.println("Focus is Non-Terminal...");

                //current_rule = non_terminal_rules.get(focus.alphabet_element)
                //Choose the next rule:
//                current_rule = focus.pop_next_rule();
//                current_rule = non_terminals_stack.pop_next_rule();
                current_rule = new_focus.pop_next_rule();

                /*
                //append nodes to focus
                List<CST> focus_children = new ArrayList<>();
                for(String alph_elem : current_rule)
                    focus_children.add(new CST(alph_elem));
                //System.out.println(String.format("%-20s%40s", "Focus CST Children:", focus_children));
                focus.children_CST = focus_children;
                */
                //Add entire rule in reverse order to the stack and update focus and
                //captures the ammount of values pushed on the stack so it is possible
                //to remove the elements from the stack when backtracking
                non_terminals_stack.push_inversed(current_rule);

                //Add rule Stack Logic here

//                focus = new CST(non_terminals_stack.peek_alphabet_member());
                new_focus.push_node(non_terminals_stack.peek());
                non_terminals_stack.pop_for_another_stack(memoryStack);
            }

            //IF FOCUS WORD MATCHES FOCUS
//            else if(focus.alphabet_element.equals(word.terminal.name())){
            else if(new_focus.peek_alphabet_member().equals(word.terminal.name())){

                System.out.println("Focus matches the Word! >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println(String.format("Tokens:%70s", scanner.getTokens()));

                word = scanner.nextToken();
//                focus = new CST(non_terminals_stack.pop_alphabet_member());
                new_focus.push_node(non_terminals_stack.pop_Node());
                memoryStack.pop_Node();
                System.out.println(String.format("%-20s%50s","New Tokens:",scanner.getTokens()));
            }
            //matches Epsilon element of the language
            //THE ONLY HARD CODE I'LL ALLOW MYSELF
//            else if (focus.alphabet_element.equals(Terminal.epsilon.name())){
            else if (new_focus.peek_alphabet_member().equals(Terminal.epsilon.name())){

                System.out.println("EPSILON detected...\nAdvancing Parser without advancing token...");

//                focus = new CST(non_terminals_stack.pop_alphabet_member());
                new_focus.push_node(non_terminals_stack.pop_Node());
                memoryStack.pop_Node();
            }
            //Detect Input Acceptance (actual token = eof and focus is empty)
            else if (word.terminal == Terminal.eof && non_terminals_stack.size() == 0){

                System.out.println("ACCEPTED INPUT! POGGERS!!\n");
                return root;
            }
            else{
                System.out.println("\n!!!!!!!!!!RULE FAILED!!!!!!!!!!\nBACKTRACKING...");

                non_terminals_stack.backtrack_on_head();
                non_terminals_stack.pop_Node();
                memoryStack.pop_for_another_stack(non_terminals_stack);
            }
            System.out.println();
            loopcount++;
        }
        System.out.println("Failed????");
        return null;
    }
}
