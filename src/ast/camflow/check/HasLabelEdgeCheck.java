package ast.camflow.check;

import ast.camflow.Consequence;
import ast.camflow.Label;
import ast.camflow.check.HasLabelCheck;

public class HasLabelEdgeCheck extends HasLabelCheck {

    Label theLabel;
    Boolean isInEdge;


    public HasLabelEdgeCheck(Label theLabel, Consequence action, Boolean isInEdge) {
        this.theLabel = theLabel;
        this.action = action;
        this.isInEdge = isInEdge;
    }

    @Override
    public Label getLabel() {
        return null;
    }
}
