package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree that tracks the used and unused rules to attempt to implement a backtracking mechanism
 *
 * @param <T> Type of the Rule -> List<String> in all cases.
 * @deprecated no longer suported, replaced by RuleStack
 * @see RuleStack
 */
public class BacktrackRuleTree<T> {

    public BacktrackRuleTree<T> parent;
    public BacktrackRuleTree<T> child;
    public BacktrackRuleTree<T> nextBrother;

    public T rule;

    public BacktrackRuleTree(BacktrackRuleTree<T> parent,T rule) {
        this.parent = parent;
        this.child = null;
        this.nextBrother = null;
        this.rule = rule;
    }
    public BacktrackRuleTree(BacktrackRuleTree<T> copy) {
        this.parent = copy.parent;
        this.child = copy.child;
        this.nextBrother = copy.nextBrother;
        this.rule = copy.rule;
    }

    public void addRules(List<T> rules){
        this.rule = rules.get(0);
        BacktrackRuleTree<T> index = this;
        for (int i = 1; i< rules.size(); i++){
            index.nextBrother = new BacktrackRuleTree<>(this.parent, rules.get(i));
            index = index.nextBrother;
        }
    }

    /**
     * Drops the current tree node and the next brother becomes the first child.
     * returns the value requires for the parser to continue working properly
     *
     * @return the parent of the node
     */
    public BacktrackRuleTree<T> pop(){

        if (parent != null){
            this.parent.child = this.nextBrother;
            if(this.nextBrother != null)
                this.nextBrother.child = this.child;
        }
        return this.parent;
    }

    /**
     * Drops the Entire tree calling this method and return the parent
     * @return parent of the tree that called
     */
    public BacktrackRuleTree<T> drop_tree() {
        this.parent.child = null;
        return this.parent;
    }

    public void add_child(BacktrackRuleTree<T> brt){

        if (this.child == null)
            child = brt;
        else{
            BacktrackRuleTree<T> index = this.child;

            while (index.nextBrother != null){
                index = index.nextBrother;
            }
            index.nextBrother = brt;
        }
    }

    public String tree_info(){
        String parent_info = null;
        if (parent != null)
            parent_info = "Parent Rule = " + this.parent.rule + '\n';
        String this_info = "This Rule = " + this.rule + '\n';
        List<BacktrackRuleTree<T>> brothers = new ArrayList<>();
        List<String> brothers_info = new ArrayList<>();

        int brother_count = 0;
        BacktrackRuleTree<T> index = this.nextBrother;
        while (index != null){
            brothers.add(index);
            index = index.nextBrother;
            brother_count++;
        }
        for (BacktrackRuleTree brother : brothers){
            brothers_info.add("Brother: = " + brother.tree_info() + "\n");
        }

        if (child != null)
            return this_info + "Brothers Ammount:\t(" + brother_count + ")\n{" + brothers_info + "}\nChild Info:\t{\n\t" + child.tree_info() + "}\n";
        else
            return this_info + "Brothers Ammount:\t(" + brother_count + ")\n{" + brothers_info + "}\nNo Children!";

    }
}
