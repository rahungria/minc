package testdrive;

import token.Tag;
import token.Tokenizer;

public class TokenizerTestDrive {

    public static void main(String[] args) {
        Tokenizer tk = Tokenizer.getInstance();
        tk.add("[\\n|\\ |\\t", Tag.WS); //add whitespace to tokens list
    }
}
