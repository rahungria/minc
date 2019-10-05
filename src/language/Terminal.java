package language;

/**
 * ENUM for each token
 *
 * @author Raphael Hungria
 * @version 1.0
 */
public enum Terminal {
    eof,
    epsilon,
    eol,
    ws,
    number,
    l_parenthesis,
    r_parenthesis,
    sum_opr,
    minus_opr,
    mult_opr,
    div_opr
}
