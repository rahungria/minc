package tests;

import exceptions.ScannerException;
import token.Scanner;
import token.Token;

public class TestScanner {

    public static void main(String[] args) {
        Scanner scanner = Scanner.getInstance();
        /*
        scanner.add("\\s", Terminal.ws); //add whitespace to tokens list
        scanner.add("\\(", Terminal.l_parenthesis);
        scanner.add("\\)", Terminal.r_parenthesis);
        scanner.add("[0-9]+", Terminal.integer);
        scanner.add("\\+", Terminal.sum_opr);
        scanner.add("\\-", Terminal.minus_opr);
        scanner.add("\\*", Terminal.mult_opr);
        scanner.add("\\;", Terminal.eol);
        */

        //scanner.build_terminals();


        System.out.println(scanner.terminals);


        try {
            scanner.scan("(20               + 3) * (11 - 1)");

            System.out.println(scanner.nextToken());

            for (Token token : scanner.getTokens()) {
                System.out.println(String.format("%-20s:%25s", token.terminal, token.lexeme));
                //System.out.println(String.format("%-20s%25s\n%-20s%25s\n", "TERMINAL:", token.terminal, "LEXEME:", token.lexeme));
            }
        }
        catch (ScannerException exc){
            System.out.println(exc.getMessage());
            exc.printStackTrace();
        }

    }
}
