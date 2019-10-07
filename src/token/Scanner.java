package token;

import exceptions.ScannerException;
import language.Terminal;

import java.util.*;
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
    private Queue<Token> tokens;

    /**
     * Initializes the Lists for Tokens and TokenInfos
     */
    private Scanner() {
        this.terminals = new HashSet<>();
        this.tokens = new ArrayDeque<>();
        build_terminals();
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
    private void build_terminals(){
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
     * @throws ScannerException throws if it finds and unrecognized expression
     * @see ScannerException
     */
    public void scan(String input_str) throws ScannerException
    {
        tokens.clear();
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
                throw new ScannerException("Unexpected Character in: ->" + matcher_input_str);
        }

        tokens.add(new Token(Terminal.eof, ""));
        //drop all whitespaces
        dropIgnoredTokens(Arrays.asList(Terminal.ws));
    }

    /**
     * Getter for all the tokens stored, should be called after every scan, since it clears the list
     * @return tokens
     */
    public Queue<Token> getTokens() {
        return tokens;
    }

    /**
     * function to get the next token to be analised by the Parser, currently work left-to-right (queue), right-to-left implemention would require Stack behaviour
     * @return the next token since the last scan (FIFO)
     */
    public Token nextToken(){
        return tokens.remove();
    }

    private void dropIgnoredTokens(List<Terminal> terminals){
        Iterator<Token> iter = tokens.iterator();

        while (iter.hasNext()){
            for (Terminal term : terminals){
                if (iter.next().terminal == term){
                    iter.remove();
                }
            }
        }
    }
}
