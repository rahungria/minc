package parser;

import java.util.List;

/**
 * Abstract Syntax Tree (AST) to be used and referenced by the Parser
 * @see Parser
 *
 * @author Raphael Hungria
 * @version 1.0
 */
public class AST {

    private List<AST>  children_AST;

    /**
     * Creates an AST given a list of children AST
     *
     * @param children_AST children tree nodes
     */
    public AST(List<AST> children_AST) {
        this.children_AST = children_AST;
    }

    public List<AST> getChildren_AST() {
        return children_AST;
    }
}
