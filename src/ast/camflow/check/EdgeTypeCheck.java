package ast.camflow.check;

import ast.camflow.Label;
import ast.camflow.check.Check;
import util.EdgeType;

public class EdgeTypeCheck extends Check {

    EdgeType edgeType;

    public EdgeTypeCheck(EdgeType edgeType) {
        this.edgeType = edgeType;
    }

    public EdgeType getEdgeType() {
        return edgeType;
    }

    @Override
    public Label getLabel() {
        return null;
    }
}
