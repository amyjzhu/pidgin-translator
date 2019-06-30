package ast.camflow;

public class HasLabelNodeCheck extends HasLabelCheck {

    Label theLabel;
    Consequence action;

    public HasLabelNodeCheck(Label theLabel, Consequence action) {
        this.theLabel = theLabel;
        this.action = action;
    }

    public Label getTheLabel() {
        return theLabel;
    }

    public void setTheLabel(Label theLabel) {
        this.theLabel = theLabel;
    }

    @Override
    public Label getLabel() {
        return null;
    }
}
