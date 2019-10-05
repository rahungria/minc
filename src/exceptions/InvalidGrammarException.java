package exceptions;

import java.io.IOException;

public class InvalidGrammarException extends IOException {

    public InvalidGrammarException(String message) {
        super(message);
    }
}
