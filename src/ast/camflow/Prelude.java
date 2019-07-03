package ast.camflow;

import ast.camflow.check.HasLabelCheck;
import ast.camflow.check.HasLabelEdgeCheck;

import java.util.ArrayList;
import java.util.List;

public class Prelude extends CamflowObject {

    @Override
    public Label getLabel() {
        return null;
    }

    public List<HasLabelCheck> generateLabelPropagationChecks(List<Label> labels) {
        List<HasLabelCheck> checks = new ArrayList<>();

        for (Label l :labels) {
            PropagateLabel actionNode = new PropagateLabel(PropagateType.PROPAGATE, new Node(), l);
            PropagateLabel actionInEdge = new PropagateLabel(PropagateType.PROPAGATE, new InEdge(), l);
            PropagateLabel actionOutEdge = new PropagateLabel(PropagateType.PROPAGATE, new OutEdge(), l);

            new HasLabelEdgeCheck(l, actionNode, true);
            new HasLabelEdgeCheck(l, actionNode, true);
        }

        return
    }
}
