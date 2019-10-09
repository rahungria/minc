package parser;

import exceptions.GrammarCompilerNotInstanciatedException;
import language.GrammarCompiler;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class RuleStack {

//    public class RuleStackNode{
//
//        String alphabet_member;
//        List<List<String>> posible_rules;
//        int on_backtrack_pop_this_many;
//
//
//        public RuleStackNode(String alphabet_member){
//            try {
//                this.alphabet_member = alphabet_member;
//                this.on_backtrack_pop_this_many = 0;
//
//                if(this.alphabet_member != null)
//                    if (GrammarCompiler.getInstance().getNon_terminals().contains(this.alphabet_member))
//                        this.posible_rules = non_terminal_rules.get(alphabet_member);
//                    else
//                        System.out.println("Terminal Element first introduced on stack");
//            }
//            catch (GrammarCompilerNotInstanciatedException e){
//                System.out.println(e.getMessage());
//                e.printStackTrace();
//            }
//        }
//        public RuleStackNode(String alphabet_member, int on_backtrack_pop_this_many){
//            try {
//                this.alphabet_member = alphabet_member;
//                this.on_backtrack_pop_this_many = on_backtrack_pop_this_many;
//                if (GrammarCompiler.getInstance().getNon_terminals().contains(this.alphabet_member))
//                    this.posible_rules = non_terminal_rules.get(alphabet_member);
//                else
//                    System.out.println("Terminal Element first introduced on stack");
//            }
//            catch (GrammarCompilerNotInstanciatedException e){
//                System.out.println(e.getMessage());
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public String toString() {
//            return '(' + alphabet_member +"," + on_backtrack_pop_this_many + ')';
//        }
//    }

    private Stack<RuleStackNode> stack;
    private Map<String, List<List<String>>> non_terminal_rules;

    protected RuleStack(String start_element){
        try {
            non_terminal_rules = GrammarCompiler.getInstance().getNon_terminal_rules();
            stack = new Stack<>();
            stack.push(new RuleStackNode(start_element));
        }
        catch (GrammarCompilerNotInstanciatedException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    protected List<String> pop_next_rule(){

        List<String> returnable = null;
        if (stack.size() > 0){

            System.out.println("Peeking: " + stack.peek());
            System.out.println("Possible Rules:\t" + stack.peek().posible_rules);
            if(stack.peek().posible_rules.size() > 0) {
                returnable = stack.peek().posible_rules.remove(0);
                System.out.println("New Possible Rules:\t" + stack.peek().posible_rules);
                return returnable;
            }
            else{
                System.out.println("Ran Out of rules mate! Poping Element from Stack!");
                stack.pop();
            }
        }
        else{
            System.out.println("Stack is empty mate!");
        }
        return returnable;
    }

    public RuleStackNode pop_Node(){

        if (stack.size() > 0){
            return stack.pop();
        }
        else{
            System.out.println("Stack Is Empty oops!");
            return  null;
        }
    }

    protected void push_inversed(List<String> list){
        RuleStackNode target  = new RuleStackNode(list.get(0));
        for (int i = list.size() - 1; i >= 0; i--){
            stack.push(new RuleStackNode(list.get(i), target));
//            check_if_head_has_rules();
        }
    }

    public void pop_for_another_stack(RuleStack other_stack){
        other_stack.push_node(this.stack.pop());
        other_stack.check_if_head_has_rules();
    }

    public void push_node(RuleStackNode node){
        stack.push(node);
//        check_if_head_has_rules();
    }

    public void check_if_head_has_rules(){
        if(stack.size() > 0){
            if (stack.peek().posible_rules != null){
                if (stack.peek().posible_rules.size() <= 0){
                    System.out.println("No Rules left for: " + stack.peek() + " \tdropping node!");
                    stack.pop();
                }
            }
            else
                System.out.println("No rules found here... (null)");

        }
        System.out.println("Stack is Empty...");
    }

    public String peek_alphabet_member(){
        if (stack.size() > 0)
            return stack.peek().alphabet_member;
        else {
            System.out.println("Peeking empty stack...");
            return null;
        }
    }

    public String pop_alphabet_member(){
        if (stack.size() > 0){
            return stack.pop().alphabet_member;
        }
        else{
            System.out.println("Empty stack huh...");
            return null;
        }
    }

    public int size(){
        return stack.size();
    }

    public void backtrack_on_head(){
        String head_element = stack.peek().alphabet_member;

        do{
            stack.pop();
        }while(stack.peek().pop_until.alphabet_member.equals(head_element));
        //check_if_head_has_rules();
    }

    public RuleStackNode peek(){
        if (stack.size() > 0)
            return stack.peek();
        return null;
    }

    @Override
    public String toString() {
        return stack.toString();
    }
}
