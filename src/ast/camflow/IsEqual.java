package ast.camflow;

import ast.camflow.check.Check;

public class IsEqual extends BooleanCondition {

    // TODO
    public CamflowObject isEqualTo;

    public IsEqual(CamflowObject isEqualTo) {
        this.isEqualTo = isEqualTo;
    }

    public CamflowObject getIsEqualTo() {
        return isEqualTo;
    }
}
