package ast.camflow;

public class HasLabelNodeCheck extends HasLabelCheck {
    Edge against;
    Label theLabel;
    Consequence action;

    public HasLabelNodeCheck(Edge against, Label theLabel, Consequence action) {
        this.against = against;
        this.theLabel = theLabel;
        this.action = action;
    }

    public Edge getAgainst() {
        return against;
    }

    public Label getTheLabel() {
        return theLabel;
    }

    public void setAgainst(Edge against) {
        this.against = against;
    }

    public void setTheLabel(Label theLabel) {
        this.theLabel = theLabel;
    }

    @Override
    public Label getLabel() {
        return null;
    }
}
