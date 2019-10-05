package token;

import exceptions.ParserException;
import language.Terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to organize and compile all the REGEX prior to input processing
 *
 * @author Raphael Hungria
 * @version 1.0
 */

public class Scanner {

    /**
     * Private nested Class/Struct to hold the regex and terminal values
     *
     * @author Raphael Hungria
     * @version 1.0
     */
    private class TokenInfo {
        public Pattern regex;
        public final Terminal terminal;

        /**
         * Simple constructor to set the values
         *
         * @param regex REGEX string
         * @param terminal Terminal (ENUM) to be associated to the regex
         */
        private TokenInfo(Pattern regex, Terminal terminal)
        {
            this.regex = regex;
            this.terminal = terminal;
        }
    }

    //Singleton Design Pattern (not regex Pattern)
    //Scanner should be a singleton since it only exists to manage the existing Patterns and Tags
    private static Scanner instance = null;
    public static Scanner getInstance()
    {
        if (instance == null)
            instance = new Scanner();
        return instance;
    }


    //List of all available REGEX
    private List<TokenInfo> tokenInfos;
    private List<Token> tokens;

    /**
     * Initializes the Lists for Tokens and TokenInfos
     */
    public Scanner() {
        this.tokenInfos = new ArrayList<>();
        this.tokens = new ArrayList<>();
    }

    /**
     * Function to be called on setup to store all REGEX to be (eventually) read from an external file
     *
     * @param regex
     * @param terminal
     */
    public void add(String regex, Terminal terminal)
    {
        tokenInfos.add(
                new TokenInfo(
                        Pattern.compile("^("+ regex +")"),
                        terminal
                )
        );
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
        String matcher_input_str = new String(input_str);
        Matcher attempter;
        boolean match;

        while (!matcher_input_str.equals(""))
        {
            match = false;

            for (TokenInfo info : tokenInfos)
            {
                attempter = info.regex.matcher(matcher_input_str);

                if (attempter.find()){
                    match = true;

                    //add the Token with the apropriate terminal and lexeme
                    tokens.add(new Token(info.terminal, attempter.group().trim()));

                    matcher_input_str = attempter.replaceFirst("");
                    break;
                }
            }

            if (!match)
                throw new ParserException("Unexpected Character in: " + matcher_input_str);
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
