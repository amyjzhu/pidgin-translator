package visitor.translate;

import java.util.*;

import ast.camflow.*;
import ast.camflow.check.*;
import ast.camflow.meta.PropagateType;
import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
import util.NodeType;

public class CamflowVisitor {

    private class ImpossibleException extends RuntimeException {
        public ImpossibleException(String msg) {
            super(msg);
        }

        public ImpossibleException() {
            super("This is an abstract element. You should not be translating it.");
        }
    }

    private class NotImplemented extends ImpossibleException {
        public NotImplemented() {
            super("Not implemented...");
        }
    }

    // TODO unsure what the underlying data structure will look like
    private static final String WARNING = "PROVENANCE_RAISE_WARNING";
    // TODO should become a keyword (but unparseable)
    private static final String REMOVED = "removed";
    private static final Label REMOVED_LABEL = new Label(REMOVED);
    private static final String NODE = "node";
    private static final String IN_EDGE = "in_edge";
    private static final String OUT_EDGE = "out_edge";
    private static final String HAS_LABEL_TEMPLATE = "has_label(%s, %s)";
    private static final String CHECK_TEMPLATE = "if ( %s ) { \\n %s \\n }";

    //private Environment env; // TODO dump in functions later
    private Labels labels;
    private Set<String> labelsForPath = new HashSet<>();

    // TODO generate propagation statements for all labels
    private Set<String> allLabels = new HashSet<>();

    String generate() {
        return "";
        // prelude should be generated at the end so we can declare all labels used
        // remember to generate labels... in the order specified
        // might want to keep a data structure with the different phases
        // phase 1, phase 2, etc. and you add it to each phase

    }

    // TODO add an exception for abstract classes


    String prelude() {
        return "QUERY_NAME(\"\");\n" +
                "QUERY_DESCRIPTION(\"\");\n" +
                "QUERY_AUTHOR(\"\");\n" +
                "QUERY_VERSION(\"\");\n" +
                "QUERY_LICENSE(\"\");\n" +
                "register_query(init, in_edge, out_edge);";
    }

    String postlude() {
        return "";
    }

    // TODO add changes to the parser to reflect new types

    // TODO make it impossible to parse some of the primitives that don't make sense
// omitting cheat for now
    // TODO look at the AST to see what kinds of things are turned into what
    // maybe spit out a parsed version of a sample program
    // add new edge types

    String translate(BooleanCondition node) throws ImpossibleException {
        String label1 = translate(node.getE1());
        String label2 = translate(node.getE2());

        return label1 + node.getRelationString() + label2;
    }

    String translate(CamflowObject node) throws ImpossibleException {
        throw new ImpossibleException();
    }

    String translate(Consequence node) throws ImpossibleException {
        throw new ImpossibleException();
    }

    String translate(Edge node) throws ImpossibleException {
        throw new ImpossibleException();
    }

    String translate(GraphElement node) throws ImpossibleException {
        throw new ImpossibleException();
    }

    String translate(InEdge node) throws ImpossibleException {
        return IN_EDGE;
    }

    String translate(Label node) throws ImpossibleException {
        return node.getText();
    }

    String translate(Node node) throws ImpossibleException {
        return NODE;
    }

    String translate(OutEdge node) throws ImpossibleException {
        return OUT_EDGE;
    }

    String translate(PropagateLabel node) throws ImpossibleException {
        allLabels.add(node.getTheLabel().getText());
        String object = translate(node.getAttachTo());
        return String.format("add_label(%s, %s)", object, node.getTheLabel().getText());
    }

    String translate(SetOperation node) throws ImpossibleException {
        // TODO this is definitely wrong
        return translate(node.getE1()) + node.getOpSym() + translate(node.getE2());
    }

    String translate(Warn node) throws ImpossibleException {
        return "raise " + WARNING;
    }

    String translate(ChainedChecks node) throws ImpossibleException {
        String base = "(%s)";
                // todo unsure if this is what chainedchecks was supposed to be for lmao
        String mid = String.join( "&&", (String[]) node.getChecks().stream().map(this::translate).toArray());
        return String.format(base, mid);
    }

    String translate(Check node) throws ImpossibleException {
        throw new ImpossibleException();
    }

    String translate(EdgeTypeCheck node) throws ImpossibleException {
        String object = node.getEdgeType().getSym();

        throw new NotImplemented();
    }

    String translate(HasLabelCheck node) throws ImpossibleException {
        throw new ImpossibleException();
    }

    String translate(HasLabelEdgeCheck node) throws ImpossibleException {
        String consequence = translate(node.getAction());
        String cond = String.format(HAS_LABEL_TEMPLATE, translate(node.getEdge()), node.getTheLabel());
        return String.format(CHECK_TEMPLATE, cond, consequence);
    }

    String translate(HasLabelNodeCheck node) throws ImpossibleException {
        String consequence = translate(node.getAction());
        String cond = String.format(HAS_LABEL_TEMPLATE, NODE, node.getTheLabel());
        return String.format(CHECK_TEMPLATE, cond, consequence);
    }

    String translate(NodeTypeCheck node) throws ImpossibleException {
        throw new NotImplemented();
    }

    String translate(ProgrammaticCheck node) throws ImpossibleException {
        // dump directly into the program
        return String.format(CHECK_TEMPLATE, node.getText(), translate(node.getAction()));
    }

    String translate(RemoveCheck node) throws ImpossibleException {
        String check = translate(node.getGuard());
        String basic = String.format(CHECK_TEMPLATE, check);
        GraphElement[] types = new GraphElement[]{new Node(), new InEdge(), new OutEdge()};
        allLabels.add(REMOVED);

        String[] actions = (String[]) Arrays.stream(types).map(s -> String.format(basic, translate(new PropagateLabel(PropagateType.NEW, s, REMOVED_LABEL)))).toArray();
        return String.join("\\n", actions);
    }




    ///
/*
    String translate(Expression node) {
        // TODO unsure what to do here....
        // except that we need it for compilation argh
        // may want to restructure the fields inside each AST class
        return "";
    }

    String translate(Function node) {
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
        for (String label : labelsForPath) {
            checks.add(String.format(HAS_LABEL_NODE_TEMPLATE, label));
        }

        String checkExpr = "(" + String.join(" || ", checks) + ")";

        String ifExpr = "if $s { return %s; }";
        return String.format(ifExpr, checkExpr, WARNING);

    }

    String translate(BackwardSlice node) {
        // really just means like
        // what's the label?
        // if node has label, propagate to in_edge
        // if out_edge has label, propagate to node
        return "";
    }


    String translate(ForwardSlice node) {
        // TOOD move to template
        String forwardTemplateOut = "if (" + HAS_LABEL_NODE_TEMPLATE + ") { add_label(out_edge, %s); }";
        String forwardTemplateIn = "if (" + HAS_LABEL_IN_EDGE_TEMPLATE + ") { add_label(node, %s); }";
        // TODO we can only use labels here, right?
        String label = node.getLabel();
        String out = String.format(forwardTemplateOut, label);
        String in = String.format(forwardTemplateIn, label);
        labels.addToPropagateChecks(out);
        labels.addToPropagateChecks(in);
        allLabels.add(label);
        // TODO we might want to have
         // some kind of data structure that
         // will give us the relevant label used

        return out + in;
    }

    String translate(RemoveEdges node) {
        // if it has this label add the removed label
        // AFTER all labels have been processed

        String identifier = node.getLabel();
        String remove = String.format(IF_ADD_LABEL_TO_NODE_TEMPLATE, identifier);

        labels.addToAssertChecks(remove);
        allLabels.add(REMOVED);
        return remove;
    }

    String translate(RemoveGuardedBy node) {
        // if fits criteria and is guarded by
        String guard = translate(node.getExpression());

        String result = String.format(IF_ADD_LABEL_TO_NODE_TEMPLATE, guard, REMOVED);
        // TODO abstract this behaviour
        labels.addToAssertChecks(result);
        allLabels.add(REMOVED);

        return result;
    }

    String translate(RemoveGuardedByBool node) {
        // if does not have...
        // needs to be put in the context of some other thing though
        // maybe add the word "removed" as a label
        // and when checking empty, make sure not removed
        // and then make sure removed is a keyword
        // TODO will this actually work to get a hasLabel or !hasLabel string?
        String guard = translate(node.getExpression());

        String result = String.format(IF_ADD_LABEL_TO_NODE_TEMPLATE, guard, REMOVED);

        labels.addToAssertChecks(result);
        allLabels.add(REMOVED);

        return result;
    }

    String translate(RemoveGuardedByMultiThread node) {
        // TODO - not sure necessary
        return "";
    }

    String translate(RemoveGuardedByPC node) {
        // TODO - not sure necessary
        return "";
    }

    String translate(RemoveNodes node) {
        // if has this label, add removed label
        //String label = node.getLabel();
        String nodeType = translate(node.getExpression());
        // TODO not exactly the same, the original camquery had a switch/case
        String result = String.format(IF_ADD_LABEL_TO_NODE_TEMPLATE, HAS_LABEL_NODE_TEMPLATE, nodeType);

        allLabels.add(nodeType);
        labels.addToAssertChecks(result);

        return "";
    }

    String translate(SelectEdges node) {
        // what does this do? assign for a let?
        // Ultimately the same isn't it? Except with types
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

*/

    private static class Labels {
        // first
        List<String> newChecks = new ArrayList<>();
        // second
        List<String> propagateChecks = new ArrayList<>();
        // third
        List<String> assertChecks = new ArrayList<>();

        public void addToNewChecks(String label) {
            newChecks.add(label);
        }

        public void addToPropagateChecks(String label) { propagateChecks.add(label); }

        public void addToAssertChecks(String label) { assertChecks.add(label); }

        public List<String> getNewChecks() {
            return newChecks;
        }

        public List<String> getPropagateChecks() {
            return propagateChecks;
        }

        public List<String> getAssertChecks() {
            return assertChecks;
        }
    }
}
