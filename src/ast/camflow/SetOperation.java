package ast.camflow;

public class SetOperation extends CamflowObject {
    // todo not sure what to do with this
    public enum Operation {
        UNION,
        INTERSECT
    }

    Operation op;
    CamflowObject e1;
    CamflowObject e2;

    // TODO should really just be a check on the labels...
    public SetOperation(Operation op, CamflowObject e1, CamflowObject e2) {
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    public CamflowObject getE1() {
        return e1;
    }

    public CamflowObject getE2() {
        return e2;
    }

    public Operation getOp() {
        return op;
    }

    @Override
    public Label getLabel() {
        return null;
    }
}
