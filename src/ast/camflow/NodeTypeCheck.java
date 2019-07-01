package ast.camflow;

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
