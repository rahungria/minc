package parser;

import token.Token;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private List<Token> tokens;
    private Token look_ahead;

    public void parse(List<Token> tokens){
        this.tokens = new ArrayList<>(tokens);
        this.look_ahead = this.tokens.get(0);
    }
}
