package language;

import exceptions.GrammarCompilerNotInstanciatedException;
import exceptions.InvalidGrammarException;
import exceptions.InvalidGrammarFileExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * Hashtable with each Non-Terminal and respective List of rules
     */
    private Hashtable<String, List<String>> non_terminal_rules;
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
            non_terminals= new HashSet<>();
            grammar_file = new File(grammar_file_path);
            scanner = new java.util.Scanner(grammar_file);
        }
        catch (FileNotFoundException e) {
            System.out.println("Grammar File Not Found!");
            e.printStackTrace();
        }
        catch (InvalidGrammarFileExtension e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        //Patterns to recognize the Non-Terminals and their rules acording to EBNF syntax
        id_ptrn = Pattern.compile("(?<id>[A-Z'_]+)(?:\\s*::=\\s*)");
        rule_ptrn = Pattern.compile("(?:\\|\\s*)?(?<rule>[a-zA-Z][a-zA-Z_'\\s]*);?");
    }

    public void build(String input) throws InvalidGrammarException{

        String matcher_input_string = new String(input);
        boolean matched = false;
        Matcher matcher;
        String id;
        List<String> rules = new ArrayList<>();

        while (!matcher_input_string.equals("")){
            matched = false;
            matcher = id_ptrn.matcher(matcher_input_string);

            //debug print (uncomment to see the language being defined
            if(matcher.find()){
                id = matcher.group("id").trim();
                //System.out.println(String.format("id:%15s", id));
                matcher_input_string = matcher.replaceFirst("");

                matcher = rule_ptrn.matcher(matcher_input_string);

                while (matcher.find()){
                    rules.add(matcher.group("rule").trim());
                    //System.out.println(String.format("%20s%40s", "rule:", "<"+matcher.group("rule").trim()+">"));
                    matcher_input_string = matcher.replaceFirst("");
                    matcher = rule_ptrn.matcher(matcher_input_string);
                }
                //System.out.println();
                matched = true;

                non_terminal_rules.put(id, rules);

                //id = "";
                rules.clear();
            }

            if (!matched){
                throw new InvalidGrammarException("Grammar EBNF Invalid Syntax!");
            }
        }
        non_terminals = non_terminal_rules.keySet();
    }

    public Set<String> getNon_terminals() {
        return non_terminals;
    }
    public Hashtable<String, List<String>> getNon_terminal_rules() {
        return non_terminal_rules;
    }
}
