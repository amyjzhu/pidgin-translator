package ast.camflow;

import java.util.List;

public class ChainedChecks extends CamflowObject {

    List<Check> checks;

    public ChainedChecks(ChainedChecks c1, ChainedChecks c2) {
        checks = c1.checks; // TODO a reference but we should dicsard anyway
        checks.addAll(c2.checks);
    }

    public ChainedChecks(List<Check> checks) {
        this.checks = checks;
    }

    @Override
    public Label getLabel() {
        return null;
    }
}
