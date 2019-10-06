package tests;

import parser.BacktrackRuleTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestBacktrackRuleTree {
    public static void main(String[] args) {

        List<String> inner_list1 = Arrays.asList("1", "2", "3");
        List<String> inner_list2 = Arrays.asList("4", "5", "6");
        List<String> inner_list3 = Arrays.asList("7", "8", "9");
        List<String> inner_list4 = Arrays.asList("10", "11", "12");
        List<String> inner_list5 = Arrays.asList("13", "14", "15");
        List<List<String>> list = Arrays.asList(inner_list1,inner_list2,inner_list3);
        List<List<String>> list2 = Arrays.asList(inner_list4,inner_list5);
        //System.out.println(list);

        BacktrackRuleTree<List<String>> brt = new BacktrackRuleTree<>(null,null);
        brt.child = new BacktrackRuleTree<>(brt,null);
        //System.out.println(brt.tree_info());
        brt.child.addRules(list);
        brt.child.child = new BacktrackRuleTree<>(brt.child, null);
        brt.child.child.addRules(list2);
        brt.rule = Arrays.asList("sou o pai xd", "xdxd");

        System.out.println(brt.tree_info());
    }
}
