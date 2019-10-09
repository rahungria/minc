package tests;

import exceptions.ScannerException;
import language.GrammarCompiler;
import parser.CST;
import parser.Parser;
import token.Scanner;

public class TestParser {

    public static void main(String[] args) {
        Scanner scanner = Scanner.getInstance();

        try {
            System.out.println("Scaning...");

            scanner.scan("(1*2) - 2 * 1 - 1");

        } catch (ScannerException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Scan Complete!");
        System.out.println("Compiling Grammar...");

        GrammarCompiler gc = GrammarCompiler.getInstance("./grammar/minc.grammar");

        System.out.println("Grammar Compiled!");

        Parser parser = new Parser(scanner.getTokens());

        System.out.println("Begin Parsing...");
        System.out.println(scanner.getTokens());

        try {
            CST tree = parser.parse();
        } catch (Exception e) {
            System.out.println("FAILED PARSING!!");
            e.printStackTrace();
            System.out.println("ZOEY MAMA");
        }

        System.out.println("Parsing Ended!");
    }
}
