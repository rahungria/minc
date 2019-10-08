package language;

import exceptions.GrammarCompilerNotInstanciatedException;
import exceptions.InvalidGrammarException;
import exceptions.InvalidGrammarFileExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * READ-ME
 *
 * @author Raphael Hungria
 * @version 1.0
 */
public class GrammarCompiler {

    private File grammar_file;
    public java.util.Scanner scanner;
    private Pattern id_ptrn;
    private Pattern rule_ptrn;
    //Singleton pattern
    private static GrammarCompiler instance = null;
    public static GrammarCompiler getInstance(String grammar_file_path){
        if (instance == null)
            instance = new GrammarCompiler(grammar_file_path);
        return instance;
    }
    public static GrammarCompiler getInstance() throws GrammarCompilerNotInstanciatedException{
        if (instance == null)
            throw new GrammarCompilerNotInstanciatedException("Singleton doesn't yet exist, use the other function signature with the appropriate path");
        return instance;
    }

    /**
     * Hashtable matching each non-terminal with a list of stacks of
     */
    private Hashtable<String, List<List<String>>> non_terminal_rules;
    private Set<String> non_terminals;

    /**
     * Constructor recieves the path to the file containing the type 2 grammar defined with EBNF syntax
     * @param grammar_file_path path to the grammar file (must have .grammar extension)
     */
    private GrammarCompiler(String grammar_file_path) {
        try {

            if (!grammar_file_path.endsWith(".grammar"))
                throw new InvalidGrammarFileExtension("Grammar File mustt have .grammar extension");

            non_terminal_rules = new Hashtable<>();
            non_terminals= new LinkedHashSet<>();
            grammar_file = new File(grammar_file_path);
            scanner = new java.util.Scanner(grammar_file);

            //Patterns to recognize the Non-Terminals and their rules acording to EBNF syntax
            id_ptrn = Pattern.compile("(?<id>[A-Z'_]+)(?:\\s*::=\\s*)");
            rule_ptrn = Pattern.compile("(?:\\|\\s*)?(?<rule>[a-zA-Z][a-zA-Z_'\\s]*)");

            compile_grammar();

//            //DEBUG CONSOLE OUTPUT
//            System.out.println("DEBUG RULES");
//            System.out.println(non_terminals);
//            System.out.println(non_terminal_rules);
//            for(String nt : non_terminals){
//                System.out.println(nt + ":");
//                System.out.println("\t" + non_terminal_rules.get(nt) + non_terminal_rules.get(nt).size());
//                for(Stack<String> stack : non_terminal_rules.get(nt)){
//                    for(String str3 : stack){
//                        System.out.println(String.format("%10s:%20","Rule:",str3));
//                    }
//                }
//            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Grammar File Not Found!");
            e.printStackTrace();
        }
        catch (InvalidGrammarFileExtension e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        catch (PatternSyntaxException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *Analyses a single line from the grammar file and builds and updates the Rules and Non-Terminals
     *
     * @param input One line from the .grammar file
     * @throws InvalidGrammarException the grammar isn't recognized by the defined regexes
     */
    public void build(String input) throws InvalidGrammarException{

        String matcher_input_string = new String(input);
        boolean matched = false;
        Matcher matcher;
        String id;
        List<List<String>> rules = new ArrayList<>();

        while (!matcher_input_string.equals("")){

            matched = false;
            matcher = id_ptrn.matcher(matcher_input_string);

            if(matcher.find()){
                id = matcher.group("id").trim();
                matcher_input_string = matcher.replaceFirst("");

                matcher = rule_ptrn.matcher(matcher_input_string);

                while (matcher.find()){
                    List<String> individual_rules = Arrays.asList(matcher.group("rule").trim().split("\\s+"));
                    rules.add(individual_rules);

//                    //DEBUG ON GRAMMAR GENERATION ON CONSOLE
//
//                    System.out.println("id:\t" + id);
//                    for (String str : individual_rules)
//                        System.out.println(String.format("%9s%20s", "rule:", '<' + str + '>'));

                    //Progress matcher entry
                    matcher_input_string = matcher.replaceFirst("");
                    matcher = rule_ptrn.matcher(matcher_input_string);
                }

                matched = true;

                non_terminal_rules.put(id, rules);

                //rules.clear(); IDIOTAAAAA PQ VC POS ISSOOO AAAAAH VO DEIXA SO PRA VC NAO ESQUECER O QUAO JAO VC EHH
            }

            if (!matched){
                throw new InvalidGrammarException("Grammar EBNF Invalid Syntax!");
            }
        }
        non_terminals = non_terminal_rules.keySet();
    }

    //VALIDATE GRAMMAR / EVERY NON-TERMINAL AFTER BUILDING!! (WIP)

    public final Set<String> getNon_terminals() {
        return non_terminals;
    }
    public final Hashtable<String, List<List<String>>> getNon_terminal_rules() {
        return Collections.unmodifiableMap(non_terminal_rules);
    }

    private void compile_grammar(){
        while(scanner.hasNextLine()){
            try {
                String nl = scanner.nextLine();
                build(nl);
                System.out.println(nl + "\n");
            }
            catch (InvalidGrammarException e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
