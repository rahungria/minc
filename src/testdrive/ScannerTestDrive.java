package testdrive;

import exceptions.ParserException;
import token.Scanner;
import language.Terminal;
import token.Token;

public class ScannerTestDrive {

    public static void main(String[] args) {
        Scanner scanner = Scanner.getInstance();
        scanner.add("[\\n|\\ |\\t]", Terminal.WS); //add whitespace to tokens list
        scanner.add("\\(", Terminal.L_PARENTHESIS);
        scanner.add("\\)", Terminal.R_PARENTHESIS);
        scanner.add("[0-9]+", Terminal.NUMBER);
        scanner.add("\\+", Terminal.SUM_OPERATOR);
        scanner.add("\\-", Terminal.MINUS_OPERATOR);
        scanner.add("\\*", Terminal.MULT_OPERATOR);



        try {
            scanner.scan("(2 + 3) * (11 - 1)");

            for (Token token : scanner.getTokens()) {
                System.out.println(String.format("%-20s%-5s", token.terminal, token.lexeme));
            }
        }
        catch (ParserException exc){
            System.out.println(exc.getMessage());
            exc.printStackTrace();
        }
    }
}
