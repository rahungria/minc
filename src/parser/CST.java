package parser;

import java.util.LinkedList;
import java.util.List;

/**
 * Concrete Syntax Tree (CST) to be used and referenced by the Parser
 * @see Parser
 *
 * @author Raphael Hungria
 * @version 1.0
 */
public class CST {

    public List<CST> children_CST;
    public String alphabet_element;

    /**
     * Creates an CST given the current Focus and its children
     *
     * @param children_CST children tree nodes
     * @param alphabet_element a terminal or non-terminal element from the grammmar
     */
    public CST(List<CST> children_CST, String alphabet_element) {
        this.children_CST = children_CST;
        this.alphabet_element = alphabet_element;
    }

    /**
     * Creates CST given just the alphabet token
     *
     * @param alphabet_element terminal or non-terminal element from the grammar
     */
    public CST(String alphabet_element) {
        this.children_CST = new LinkedList<>();
        this.alphabet_element = alphabet_element;
    }

    public List<CST> getChildren_CST() {
        return children_CST;
    }

    @Override
    public String toString() {
        return "<" + alphabet_element + ">";
    }
}
