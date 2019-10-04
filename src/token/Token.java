package token;

public class Token
{
    public final Tag tag;
    public final String lexeme;

    public Token(Tag tag, String lexeme) {
        this.tag = tag;
        this.lexeme = lexeme;
    }
}
