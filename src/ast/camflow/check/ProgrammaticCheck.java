package ast.camflow.check;

import ast.camflow.Consequence;
import ast.camflow.Label;

public class ProgrammaticCheck extends Check {

    String text;

    // forProcudure, forExpression, etc
    public ProgrammaticCheck(String text, Consequence action) {
        this.text = text;
        this.action = action;
    }

    public String getText() {
        return text;
    }

    @Override
    public Label getLabel() {
        return null;
    }
}
