package token;

import exceptions.ParserException;

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

public class Tokenizer {

    /**
     * Private nested Class/Struct to hold the regex and tag values
     *
     * @author Raphael Hungria
     * @version 1.0
     */
    private class TokenInfo {
        public Pattern regex;
        public final Tag tag;

        /**
         * Simple constructor to set the values
         *
         * @param regex REGEX string
         * @param tag Tag (ENUM) to be associated to the regex
         */
        private TokenInfo(Pattern regex, Tag tag)
        {
            this.regex = regex;
            this.tag = tag;
        }
    }

    //Singleton Design Pattern (not regex Pattern)
    //Tokenizer should be a singleton since it only exists to manage the existing Patterns and Tags
    private static Tokenizer instance = null;
    public static Tokenizer getInstance()
    {
        if (instance == null)
            instance = new Tokenizer();
        return instance;
    }


    //List of all available REGEX
    private List<TokenInfo> tokenInfos;
    private List<Token> tokens;

    /**
     * Initializes the Lists for Tokens and TokenInfos
     */
    public Tokenizer() {
        this.tokenInfos = new ArrayList<>();
        this.tokens = new ArrayList<>();
    }

    /**
     * Function to be called on setup to store all REGEX to be (eventually) read from an external file
     *
     * @param regex
     * @param tag
     */
    public void add(String regex, Tag tag)
    {
        tokenInfos.add(
                new TokenInfo(
                        Pattern.compile("^("+ regex +")"),
                        tag
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
    public void tokenize(String input_str)
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

                    //add the Token with the apropriate tag and lexeme
                    tokens.add(new Token(info.tag, attempter.group().trim()));

                    matcher_input_str = attempter.replaceFirst("");
                    break;
                }
            }

            if (!match)
                throw new ParserException("Unexpected Character in: " + matcher_input_str);
        }
    }

}
