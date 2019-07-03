package ast.camflow.check;

import ast.camflow.CamflowObject;
import ast.camflow.Consequence;

public abstract class Check extends CamflowObject {
    Consequence action;

    public Consequence getAction() {
        return action;
    }
}
