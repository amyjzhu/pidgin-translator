package parser;

import ast.camflow.*;
import ast.camflow.check.*;
import parser.parsetree.*;
import parser.parsetree.BooleanCondition;
import parser.parsetree.Label;
import util.NodeType;
import util.EdgeType;

import java.util.*;

public class ParseTreeToCamflow {

    Set<ast.camflow.Label> labels = new HashSet<>();
    Set<Function> functions = new HashSet<>();

    // TODO -- generate intermediary, and collect all labels
    // so we can generate label-propagation code
    CamflowObject translate(Expression e) {
        return null;
    }

    // converts nodes to camflow-style nodes

    CamflowObject translate(Assign e) {
        return null;
    }

    CamflowObject translate(Backwards e) {
        // this should handle all label propagation somehow
        // make sure it gets only one...
        ast.camflow.Label l = translate(e.getFrom()).getLabel(); // should dispatch
        labels.add(l);
        Consequence propagateToNode = new PropagateLabel(PropagateType.NEW, new Node(), l);
        Consequence propagateToEdge = new PropagateLabel(PropagateType.NEW, new InEdge(), l);
        // TODO am I returning one item at a time to chain?
        // TODO if they are backwards we should not propagate them down automatically...
        Check checkNodeToEdge = new HasLabelNodeCheck(l, propagateToEdge);
        Check checkEdgeToNode = new HasLabelEdgeCheck(l, propagateToNode, false); // out edge
        return new ChainedChecks(Arrays.asList(checkEdgeToNode, checkNodeToEdge));
    }

    CamflowObject translate(Between e) {
        Forwards from = new Forwards(e.getFrom());
        Backwards to = new Backwards(e.getTo());
        ChainedChecks fromChecks = (ChainedChecks) translate(from);
        ChainedChecks toChecks = (ChainedChecks) translate(to);
        return new ChainedChecks(fromChecks, toChecks);
    }

    CamflowObject translate(BooleanCondition e) {
        return null;
    }

    CamflowObject translate(EdgeType e) {
        return new EdgeTypeCheck(e);
    }

    CamflowObject translate(ForExpression e) {
        // check needs to invoke a procedure
        // and get the result of the procedure
        // TODO might need to add a new check type
        return null;
    }

    CamflowObject translate(ForProcedure e) {
        // need a label
        // we can just return the checks rather than any action...
        return new ProgrammaticCheck(e.getProcedureName().getName(), null);
    }

    CamflowObject translate(Forwards e) {
        ast.camflow.Label l = translate(e.getFrom()).getLabel(); // should dispatch
        labels.add(l);
        Consequence propagateToNode = new PropagateLabel(PropagateType.NEW, new Node(), l);
        Consequence propagateToEdge = new PropagateLabel(PropagateType.NEW, new OutEdge(), l);
        // TODO am I returning one item at a time to chain?
        Check checkNodeToEdge = new HasLabelNodeCheck(l, propagateToEdge);
        Check checkEdgeToNode = new HasLabelEdgeCheck(l, propagateToNode, true);
        return new ChainedChecks(Arrays.asList(checkEdgeToNode, checkNodeToEdge));
    }

    CamflowObject translate(Graph e) {
        // TODO this is basically meaningless
        return null;
    }

    CamflowObject translate(Intersect e) {
        return new SetOperation(SetOperation.Operation.INTERSECT, translate(e.getE1()), translate(e.getE2()));
    }

    CamflowObject translate(IsEmpty e) {

        List<Check> checks = new ArrayList<>();
        for (Expression ex: e.getToTest()) {
            ast.camflow.Label l = translate(ex).getLabel();
            new HasLabelNodeCheck(l, new Warn());
        }

        return new ChainedChecks(checks);
    }

    CamflowObject translate(Label e) {
        return new ast.camflow.Label(e.getText());
    }

    CamflowObject translate(NodeType e) {
        return new NodeTypeCheck(e);
    }

    CamflowObject translate(Policy e) {
        return null;
    }

    CamflowObject translate(PrimitiveExpression e) {
        return null;
    }

    CamflowObject translate(Procedure e) {
        return new Function(e.getName().getName(), e.getBody());
    }

    CamflowObject translate(Remove e) {
        // remove should do a boolean check
        // add the removed label
        // TODO this might be the wrong approach because which item has which role
        new BooleanCondition();
        new RemoveCheck();
        new PropagateLabel(PropagateType.NEW, new Node(), ast.camflow.Label.REMOVE_LABEL);
        return null;
    }

    CamflowObject translate(Union e) {
        return new SetOperation(SetOperation.Operation.UNION, translate(e.getE1()), translate(e.getE2()));
    }

    CamflowObject translate(Var e) {
        // TODO used this just as a label generator
        // probably need to add to some list...
        ast.camflow.Label label = new ast.camflow.Label(e.getName());
        labels.add(label);
        return label;
    }
}
