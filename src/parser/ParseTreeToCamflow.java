package parser;

import ast.camflow.*;
import parser.parsetree.*;
import parser.parsetree.Label;
import parser.parsetree.Union;

import java.util.*;

public class ParseTreeToCamflow {

    Set<ast.camflow.Label> labels = new HashSet<>();

    // TODO -- generate intermediary, and collect all labels
    // so we can generate label-propagation code
    List<CamflowObject> translate(Expression e) {
        return null;
    }

    List<CamflowObject> translate(Argument e) {
        return null;
    }

    // converts nodes to camflow-style nodes

    List<CamflowObject> translate(Assign e) {
        return null;
    }

    List<CamflowObject> translate(Backwards e) {
        // this should handle all label propagation somehow
        // make sure it gets only one...
        ast.camflow.Label l = translate(e.getFrom()).get(0).getLabel(); // should dispatch
        labels.add(l);
        Consequence propagateToNode = new PropagateLabel(PropagateType.NEW, new Node(), l);
        Consequence propagateToEdge = new PropagateLabel(PropagateType.NEW, new InEdge(), l);
        List<CamflowObject> returnList = new ArrayList<CamflowObject>;
        returnList.add(new HasLabelNodeCheck(new OutEdge(), l, propagateToNode));
    }

    List<CamflowObject> translate(Between e) {
        return null;
    }

    List<CamflowObject> translate(BooleanCondition e) {
        return null;
    }

    List<CamflowObject> translate(EdgeType e) {
        return null;
    }

    List<CamflowObject> translate(ForExpression e) {
        return null;
    }

    List<CamflowObject> translate(ForProcedure e) {
        return null;
    }

    List<CamflowObject> translate(Forwards e) {
        return null;
    }

    List<CamflowObject> translate(Graph e) {
        return null;
    }

    List<CamflowObject> translate(Intersect e) {
        return null;
    }

    List<CamflowObject> translate(IsEmpty e) {
        return null;
    }

    List<CamflowObject> translate(Label e) {
        return null;
    }

    List<CamflowObject> translate(NodeType e) {
        return null;
    }

    List<CamflowObject> translate(Policy e) {
        return null;
    }

    List<CamflowObject> translate(PrimitiveExpression e) {
        return null;
    }

    List<CamflowObject> translate(Procedure e) {
        return null;
    }

    List<CamflowObject> translate(Remove e) {
        return null;
    }

    List<CamflowObject> translate(Union e) {
        return null;
    }

    List<CamflowObject> translate(Var e) {
        return null;
    }
}
