package accrue.camflow;

import java.util.HashSet;
import java.util.Set;

import accrue.query.bool.*;
import accrue.query.expression.*;
import accrue.query.policy.*;
import accrue.query.primitive.*;
import accrue.query.query.*;
import accrue.query.cheat.*;
import accrue.query.util.*;

public class TranslateVisitor {
    
    private static final String WARNING = "PROVENANCE_RAISE_WARNING";
    private Set<String> labelsForPath = new HashSet<>();

    String prelude() {
        return "";
    }

    String postlode() {
        return "";
    }
    
    // TODO add changes to the parser to reflect new types 

    // TODO make it impossible to parse some of the primitives that don't make sense
// omitting cheat for now
    // TODO look at the AST to see what kinds of things are turned into what
    // maybe spit out a parsed version of a sample program
    // add new edge types

    String translate(BooleanAnd node) {
        String label1 = translate(node.getB1());
        String label2 = translate(node.getB2());
                
        String composed = label1 + " && " + label2;
        return composed;
    }

    String translate(BooleanExpression node) {
        return "";
    }

    String translate(BooleanNot node) {
        String expr = translate(node.getNegated());
        return "!" + expr;
    }

    String translate(BooleanOr node) {
        String label1 = translate(node.getB1());
        String label2 = translate(node.getB2());
                
        // TODO possibly worth abstracting
        String composed = label1 + " || " + label2;
        return composed;
    }

    String translate(BooleanVariable node) {
        return "";
    }

    String translate(BoolType node) {
        return "";
    }

    String translate(Expression node) {
        // TODO unsure what to do here....
        return "";
    }

    String translate(FunctionApplication node) {
        // TODO find an example of the function
        // TODO probably institute some getters and setters also
        return "";
    }

    String translate(Intersection node) {
        // an intersection means
        // that two types of labels must hold
        
        // label elements with the correct label if they
        // have both labels
        // basically create a BooleanAnd with the two halves
        return "";
    }

    String translate(OriginalGraph node) {
        return "";
    }

    String translate(PrimitiveApplication node) {
        return "";
    }

    String translate(Union node) {
        return "";
    }

    String translate(Variable node) {
        return "";
    }

    // there could be some weird edge cases with mutability possibly...
    // gotta think about it more 
    String translate(IsEmpty node) {
        // how do I track what the pdg is currently?
        // let policy1 = pdg.removeGuardedByBool(isAuthor && !subPast) and addPaper in
        // policy1 is empty
        Set<String> checks = new HashSet<>();
        String equalityCheckTemplate = "has_label(node, %s)";
        for (String label : labelsForPath) { 
            checks.add(String.format(equalityCheckTemplate, label));
        }
        
        String checkExpr = "(" + String.join(" || ", checks) + ")";
        
        String ifExpr = "if $s { return %s; }";
        return String.format(ifExpr, checkExpr, WARNING);
        
    }

    String translate(Policy node) {
        return "";
    }

    String translate(BackwardSlice node) {
        // really just means like
        // what's the label?
        // if node has label, propagate to in_edge
        // if out_edge has label, propagate to node
        return "";
    }

    String translate(ForExpression node) {
        // dump the expression directly into 
        // the file
        
        return "";
    }

    String translate(ForProcedure node) {
        // dump procedure
        // find it in the environment and dump it by name
        return "";
    }

    String translate(ForwardSlice node) {
        return "";
    }

    String translate(PrimitiveExpression node) {
        return "";
    }

    String translate(PrimitiveNodeMatchingExpression node) {
        return "";
    }

    String translate(RemoveEdges node) {
        // if it has this label add the removed label
        // AFTER all labels have been processed 
        return "";
    }

    String translate(RemoveGuardedBy node) {
        // if fits criteria and is guarded by
        return "";
    }

    String translate(RemoveGuardedByBool node) {
        // if does not have...
        // needs to be put in the context of some other thing though 
        // maybe add the word "removed" as a label
        // and when checking empty, make sure not removed
        // and then make sure removed is a keyword
        return "";
    }

    String translate(RemoveGuardedByMultiThread node) {
        return "";
    }

    String translate(RemoveGuardedByPC node) {
        return "";
    }

    String translate(RemoveNodes node) {
        // if has this label, add removed label
        return "";
    }

    String translate(SelectEdges node) {
        // what does this do? assign for a let?
        return "";
    }

    String translate(SelectNodes node) {
        // start by grabbing the nodes with a certain property
        // select nodes is for a node type though
        return "";
    }

    String translate(ShortestPath node) {
        // should not have impl
        return "";
    }

    String translate(ExpressionQuery node) {
        return "";
    }

    // grab label
    String translate(Let node) {
        return "";
    }

    String translate(LetFun node) {
        return "";
    }

    String translate(Query node) {
        return "";
    }

    String translate(Argument node) {
        return "";
    }

    String translate(Closure node) {
        return "";
    }

    String translate(Environment node) {
        return "";
    }

    String translate(FunctionDecl node) {
        return "";
    }

    String translate(Lambda node) {
        return "";
    }

    String translate(StringMatcher node) {
        return "";
    }
}
