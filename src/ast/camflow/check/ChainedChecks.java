package ast.camflow.check;

import ast.camflow.CamflowObject;
import ast.camflow.Label;

import java.util.List;

public class ChainedChecks extends CamflowObject {

    // TODO should this be one consequence... or is this an object of multiplictyb
    List<Check> checks;

    public ChainedChecks(ChainedChecks c1, ChainedChecks c2) {
        checks = c1.checks; // TODO a reference but we should dicsard anyway
        checks.addAll(c2.checks);
    }

    public ChainedChecks(List<Check> checks) {
        this.checks = checks;
    }

    public List<Check> getChecks() {
        return checks;
    }

    @Override
    public Label getLabel() {
        return null;
    }
}
