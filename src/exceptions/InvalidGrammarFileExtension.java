package exceptions;

import java.io.IOException;

public class InvalidGrammarFileExtension extends IOException {
    public InvalidGrammarFileExtension(String message) {
        super(message);
    }
}
