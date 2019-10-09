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
    private GrammarCompiler gc;
    private Set<String> non_terminals;
    private Hashtable<String, List<List<String>>> non_terminal_rules;

    public Parser(Collection<Token> tokens) {
        try {
            this.tokens = tokens;
            scanner = Scanner.getInstance();
            gc = GrammarCompiler.getInstance();

            non_terminals = new HashSet<>(gc.getNon_terminals());
            non_terminal_rules = new Hashtable<>(gc.getNon_terminal_rules());

        } catch (GrammarCompilerNotInstanciatedException e){

            System.out.println(e.getMessage() + "\nfinding alternative at: ./grammar/minc.grammar");
            gc = GrammarCompiler.getInstance("./grammar/minc.grammar");
        }
    }

    public CST parse(){

        CST root = new CST(null, "S");
        Token word = scanner.nextToken();
        List<String> current_rule = new ArrayList<>();

        Stack<RuleObj> non_terminals_stack = new Stack<>();
        Stack<RuleObj> memoryStack = new Stack<>();
        Stack<RuleObj> focus = new Stack<>();

        non_terminals_stack.push(new RuleObj(Terminal.eof.name(),gc));
        focus.push(new RuleObj(root.alphabet_element, gc));

        int loopcount = 0;
        int rule_size = 0;

        while(true){


            //DEBUG CONSOLE LOG
            System.out.println("loop " + loopcount);
            System.out.println("TOKENS:\t" + scanner.getTokens());
            for (int i =0; i < 100; i++)
                System.out.print('-');
            System.out.println();

            System.out.println(String.format("%-20s%80s","Focus:", focus));
            System.out.println(String.format("%-20s%80s", "Stack:", non_terminals_stack));
            System.out.println(String.format("%-20s%80s", "Memory:", memoryStack));
            System.out.println(String.format("%-20s%80s", "Word:", word.terminal.name() + "(" + word.lexeme + ")"));
            System.out.println(String.format("%-20s%80s", "Current Rule:", current_rule));

            for (int i =0; i < 100; i++)
                System.out.print('-');
            System.out.println();
            //END OF DEBUG CONSOLE LOG


            //IF FOCUS IS A NON TERMINAL
            if (non_terminals.contains(focus.peek().alphabet_member)){

                System.out.println("Focus is Non-Terminal...");

                //Choose the next rule:
                non_terminals_stack.push(focus.pop());
                current_rule = non_terminals_stack.peek().pop_rule();

                /*
                //append nodes to focus
                List<CST> focus_children = new ArrayList<>();
                for(String alph_elem : current_rule)
                    focus_children.add(new CST(alph_elem));
                //System.out.println(String.format("%-20s%40s", "Focus CST Children:", focus_children));
                focus.children_CST = focus_children;
                */

                //Pop stack head to memory
                //push rule in reverse order to stack
                //push the head of the stack into focus
                memoryStack.push(non_terminals_stack.pop());

                rule_size = current_rule.size();
                for (int i = rule_size - 1; i >= 0; i--){
                    non_terminals_stack.push(new RuleObj(current_rule.get(i),gc));
                }
                focus.push(non_terminals_stack.pop());

            }

            //IF FOCUS WORD MATCHES FOCUS
            else if(focus.peek().alphabet_member.equals(word.terminal.name())){

                System.out.println("Focus matches the Word! >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println(String.format("Tokens:%-70s", scanner.getTokens()));

                //Advance token
                word = scanner.nextToken();

                //simply pop focus
                focus.pop();

                //push head of stack into memory
                memoryStack.push(non_terminals_stack.pop());

                //define that the head of memory has popped a token
                memoryStack.peek().popped_word = true;
                //current rule becomes the next rule from the head of the memory
                current_rule = memoryStack.peek().pop_rule();

                //push into stack the current rule in reverse order
                rule_size = current_rule.size();
                for (int i = rule_size - 1; i >= 0; i--){
                    non_terminals_stack.push(new RuleObj(current_rule.get(i), gc));
                }
//                System.out.println(String.format("%-20s%80s","Focus:", focus));
//                System.out.println(String.format("%-20s%80s", "Stack:", non_terminals_stack));
//                System.out.println(String.format("%-20s%80s", "Memory:", memoryStack));
//                System.out.println(String.format("%-20s%80s", "Word:", word.terminal.name() + "(" + word.lexeme + ")"));
//                System.out.println(String.format("%-20s%80s", "Current Rule:", current_rule));

                if (non_terminals_stack.size() == 0 && word.terminal.name().equals(Terminal.eof.name()) && memoryStack.peek().alphabet_member.equals(Terminal.eof.name()))
                    break;

                //update focus from head of stack
                focus.push(non_terminals_stack.pop());
            }
            //matches Epsilon element of the language
            //THE ONLY HARD CODE I'LL ALLOW MYSELF
            else if (focus.peek().alphabet_member.equals(Terminal.epsilon.name())){

                System.out.println("EPSILON detected...\nAdvancing Parser without advancing token...");

                //drop the epsilon focus
                focus.pop();

                //new focus becomes the head of the stack
                focus.push(non_terminals_stack.pop());
            }
            //Detect Input Acceptance (actual token = eof and focus is empty)
            else{
                System.out.println("\n!!!!!!!!!!RULE FAILED!!!!!!!!!!\nBACKTRACKING...");

                //return the focus to the stack
                non_terminals_stack.push(focus.pop());

                //drop from the stack all the elements pushed from the past rule
                for (int i = 0; i < rule_size; i++)
                    non_terminals_stack.pop();

                //drop all the elements from the memory that have run out of rules
                while(memoryStack.peek().rules.size() == 0){
                    //If the rule on the memory that was called to backtrack has accepted an input and popped a token,
                    //then restore the token onto the Token Queue
                    if (memoryStack.peek().popped_word)
                        scanner.restore_token();
                    memoryStack.pop();
                }



                //head from memory becomes new focus
                focus.push(memoryStack.pop());

            }
            System.out.println();
            loopcount++;
        }
        System.out.println("Input Parsed!");
        return root;
    }
}
