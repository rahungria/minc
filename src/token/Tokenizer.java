package token;

import java.util.ArrayList;
import java.util.List;
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
        public TokenInfo(Pattern regex, Tag tag)
        {
            this.regex = regex;
            this.tag = tag;
        }
    }

    //List of all available REGEX
    private List<TokenInfo> tokenInfos;

    /**
     * Empty Constructor
     */
    public Tokenizer() {
        this.tokenInfos = new ArrayList<>();
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
}
