package token;

import exceptions.ParserException;
import language.Terminal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to organize and compile and generate Terminal set of all the REGEX and input processing
 * (maybe separate between 2 different classes)
 *
 * @author Raphael Hungria
 * @version 1.0
 */

public class Scanner {

    /**
     * Private nested Class/Struct to hold the regex_str and terminal values
     *
     * @author Raphael Hungria
     * @version 1.0
     */
    private class TokenInfo {
        Pattern regex;
        final Terminal terminal;

        /**
         * Simple constructor to set the Terminal
         *
         * @param terminal Terminal (ENUM) to be associated to the regex_str
         */
        private TokenInfo(/*Pattern regex_str, */Terminal terminal)
        {
            this.terminal = terminal;
            this.regex = Pattern.compile("^(?:" + this.terminal.regex_str + ")");
        }

        @Override
        public String toString() {
            return String.format("TokenInfo{ <regex> = %-25s, <terminal> = %-15s }\n", regex, terminal);
        }
    }

    //Singleton Design Pattern (not regex_str Pattern)
    //Scanner should be a singleton since it only exists to manage the existing Patterns and Tags
    private static Scanner instance = null;
    public static Scanner getInstance()
    {
        if (instance == null)
            instance = new Scanner();
        return instance;
    }


    //List of all available REGEX
    public Set<TokenInfo> terminals;
    private List<Token> tokens;

    /**
     * Initializes the Lists for Tokens and TokenInfos
     */
    private Scanner() {
        this.terminals = new HashSet<>();
        this.tokens = new ArrayList<>();
    }

    /*
    public void add(Terminal terminal)
    {
        terminals.add(
                new TokenInfo(
                        terminal
                )
        );
    }
    */

    /**
     * Automatically goes through Terminal ENUM and build the set for it (eventually will read from file)
     */
    public void build_terminals(){
        for (Terminal terminal : Terminal.values()){
            terminals.add(
                    new TokenInfo(
                    terminal
                    )
            );
        }
    }

    /**
     * Receives a input String and matches and creates Tokens for all matches according to the defined REGEX in tokeninfos.
     *
     * @param input_str input string to be matched
     * @throws  ParserException throws if it finds and unrecognized expression
     * @see ParserException
     */
    public void scan(String input_str) throws ParserException
    {
        String matcher_input_str = input_str;
        Matcher attempter;
        boolean match;

        while (!matcher_input_str.equals(""))
        {
            match = false;

            for (TokenInfo terminal : terminals)
            {
                attempter = terminal.regex.matcher(matcher_input_str);

                if (attempter.find()){
                    match = true;

                    //add the Token with the apropriate terminal and lexeme
                    tokens.add(new Token(terminal.terminal, attempter.group().trim()));

                    matcher_input_str = attempter.replaceFirst("");
                    break;
                }
            }

            if (!match)
                throw new ParserException("Unexpected Character in: ->" + matcher_input_str);
        }
    }

    /**
     * Getter for all the tokens stored, should be called after every scan, since it clears the list
     * @return tokens
     */
    public List<Token> getTokens() {
        return tokens;
    }
}
