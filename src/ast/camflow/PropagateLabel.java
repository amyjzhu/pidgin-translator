package ast.camflow;

public class PropagateLabel extends Consequence {

    PropagateType type;
    GraphElement attachTo;
    Label theLabel;

    public PropagateLabel(PropagateType type, GraphElement attachTo, Label theLabel) {
        this.type = type;
        this.attachTo = attachTo;
        this.theLabel = theLabel;
    }

}
