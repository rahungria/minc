package tests;

import exceptions.InvalidGrammarException;
import language.GrammarCompiler;

public class TestGrammarCompiler {

    public static void main(String[] args) {
        GrammarCompiler ntb = GrammarCompiler.getInstance("./grammar/minc.grammar");

        while(ntb.scanner.hasNextLine()){
            try {
                String nl = ntb.scanner.nextLine();
                ntb.build(nl);
                System.out.println(nl);
                //System.out.println("Another Rule!");
            }
            catch (InvalidGrammarException e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

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
