package language;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;

/**
 * READ-ME
 *
 * @author Raphael Hungria
 * @version 1.0
 */
public class Non_Terminal_Builder{

    private Dictionary<String, List<String>> non_terminals;

    public Non_Terminal_Builder() {
        non_terminals = new Hashtable<>();
    }

    public void add(String ebnf_regex){

        String matcher_input_string = new String(ebnf_regex);
        boolean matched = false;
        Matcher matcher;

        
    }
}
