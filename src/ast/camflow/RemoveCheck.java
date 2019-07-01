package ast.camflow;

public class RemoveCheck extends Check {

    // need to do to nodes and to edges
    BooleanCondition guard;
    public static String REMOVE_LABEL = "REMOVE";

    public RemoveCheck(BooleanCondition guard) {
        this.guard = guard;
    }

    public BooleanCondition getGuard() {
        return guard;
    }
    @Override
    public Label getLabel() {
        return null;
    }
}
