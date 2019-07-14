package ast.camflow;

import ast.camflow.meta.PropagateType;

public class PropagateLabel extends Consequence {

    PropagateType type;
    GraphElement attachTo;
    Label theLabel;

    public PropagateLabel(PropagateType type, GraphElement attachTo, Label theLabel) {
        this.type = type;
        this.attachTo = attachTo;
        this.theLabel = theLabel;
    }

    public PropagateType getType() {
        return type;
    }

    public GraphElement getAttachTo() {
        return attachTo;
    }

    public Label getTheLabel() {
        return theLabel;
    }
}
