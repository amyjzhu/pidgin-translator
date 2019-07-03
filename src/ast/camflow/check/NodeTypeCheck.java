package ast.camflow.check;

import ast.camflow.Label;
import ast.camflow.check.Check;
import util.*;

public class NodeTypeCheck extends Check {

    NodeType nodeType;

    public NodeTypeCheck(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    @Override
    public Label getLabel() {
        return null;
    }
}
