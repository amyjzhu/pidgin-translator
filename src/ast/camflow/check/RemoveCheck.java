package ast.camflow.check;

import ast.camflow.BooleanCondition;
import ast.camflow.Consequence;
import ast.camflow.Label;
import ast.camflow.check.Check;

public class RemoveCheck extends Check {

    // need to do to nodes and to edges
    BooleanCondition guard;

    public RemoveCheck(BooleanCondition guard, Consequence action) {
        this.guard = guard;
        this.action = action;
    }

    public BooleanCondition getGuard() {
        return guard;
    }
    @Override
    public Label getLabel() {
        return null;
    }
}
