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
public class ScannerException extends Exception {

    public ScannerException(String message) {
        super(message);
    }
}
