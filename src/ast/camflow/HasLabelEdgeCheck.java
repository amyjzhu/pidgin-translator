package ast.camflow;

public class HasLabelEdgeCheck extends HasLabelCheck {

    Label theLabel;
    Consequence action;

    public HasLabelEdgeCheck(Label theLabel, Consequence action) {
        this.theLabel = theLabel;
        this.action = action;
    }

    @Override
    public Label getLabel() {
        return null;
    }
}
