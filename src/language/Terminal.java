package language;

/**
 * ENUM for each Terminal of the language
 *
 * @author Raphael Hungria
 * @version 1.0
 */
public enum Terminal {
    //eof("TEMP1"),//STIL NOT SURE WHAT WOULD FIT
    epsilon("TEMP2"),//ALSO NOT REALLY SURE
    id("TEMP3"),
    semi_collon("\\;"),
    eol("TEMP!!"),
    ws("\\s+"),
    integer("\\d+"),
    //real("-?\\d+\\.\\d+(?:[Ee][+-]?\\d+)?"),
    l_parenthesis("\\("),
    r_parenthesis("\\)"),
    sum_opr("\\+"),
    minus_opr("\\-"),
    mult_opr("\\*"),
    div_opr("\\/");

    public final String regex_str;

    Terminal(String regex) {
        this.regex_str = regex;
    }}
