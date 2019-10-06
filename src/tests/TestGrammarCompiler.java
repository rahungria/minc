package tests;

import exceptions.InvalidGrammarException;
import language.GrammarCompiler;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class TestGrammarCompiler {

    public static void main(String[] args) {
        GrammarCompiler gc = GrammarCompiler.getInstance("./grammar/minc.grammar");

        while(gc.scanner.hasNextLine()){
            try {
                String nl = gc.scanner.nextLine();
                gc.build(nl);
                System.out.println(nl + '\n');
            }
            catch (InvalidGrammarException e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

        if(gc.getNon_terminals().contains("EXPR")){
            System.out.println("tem EXPR");
        }
        if (gc.getNon_terminals().contains("S")){
            System.out.println("tem S");
        }


//        //do this on non_terminal_rules
//        Stack<String> str = new Stack<>();
//        List<String> list = Arrays.asList("EXPR     MULT_OPR     TERM".split("\\s+"));
//        Stack<String> stack = new Stack<>();
//        stack.addAll(list);
//        int size = stack.size();
//        for(int i =0; i < size; i++){
//            System.out.println("<" + stack.pop() + ">");
//        }


        /*
        System.out.println("\n\n####TYPE 2 GRAMMAR RULES: #####");
        Enumeration<String> ids = ntb.non_terminal_rules.keys();
        List<String> rules = new ArrayList<>();

        while (ids.hasMoreElements()){
            System.out.println(String.format("%-10s:", ids.nextElement()));
            rules = ntb.non_terminal_rules.get(ids.nextElement());
            for (String str : rules){
                System.out.println(str);
            }
        }
        */
    }
}
