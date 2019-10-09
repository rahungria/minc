package token;

import language.Terminal;

/**
 * Simple base Token class to be thrown to Syntax Analysis
 *
 * @author Raphael Hungria
 * @version 1.0
 */
public class Token
{
    public final Terminal terminal;
    public final String lexeme;

    public Token(Terminal terminal, String lexeme) {
        this.terminal = terminal;
        this.lexeme = lexeme;
    }
    public Token(Token copy) {
        this.terminal = copy.terminal;
        this.lexeme = copy.lexeme;
    }


    @Override
    public String toString() {
        return String.format("(%s,%s)", terminal, lexeme);
    }
}
