package ast.camflow.check;

import ast.camflow.*;
import ast.camflow.check.HasLabelCheck;

public class HasLabelEdgeCheck extends HasLabelCheck {

    Label theLabel;
    Edge edge;


    public HasLabelEdgeCheck(Label theLabel, Consequence action, Boolean isInEdge) {
        this.theLabel = theLabel;
        this.action = action;
        if (isInEdge) {
            edge = new InEdge();
        } else {
            edge = new OutEdge();
        }
    }

    @Override
    public Label getLabel() {
        return null;
    }

    public Label getTheLabel() {
        return theLabel;
    }

    public Edge getEdge() {
        return edge;
    }
}
