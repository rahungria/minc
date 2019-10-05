package testdrive;

import exceptions.ParserException;
import token.Scanner;
import language.Terminal;
import token.Token;

public class ScannerTestDrive {

    public static void main(String[] args) {
        Scanner scanner = Scanner.getInstance();
        scanner.add("[\\n|\\ |\\t]", Terminal.ws); //add whitespace to tokens list
        scanner.add("\\(", Terminal.l_parenthesis);
        scanner.add("\\)", Terminal.r_parenthesis);
        scanner.add("[0-9]+", Terminal.number);
        scanner.add("\\+", Terminal.sum_opr);
        scanner.add("\\-", Terminal.minus_opr);
        scanner.add("\\*", Terminal.mult_opr);
        scanner.add("\\;", Terminal.eol);



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
