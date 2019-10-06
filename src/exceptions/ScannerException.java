package exceptions;

import token.Scanner;

/**
 * Exception for unrecognized expression while parsing an input.
 * Called by Scanner
 * @see Scanner
 *
 * @author Raphael Hungria
 * @version 1.0
 */
public class ParserException extends Exception {

    public ParserException(String message) {
        super(message);
    }
}
