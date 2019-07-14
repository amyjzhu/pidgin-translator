package ast.camflow;

import java.util.List;

public class SetOperation extends CamflowObject {
    // todo not sure what to do with this
    public enum Operation {
        UNION("&&"),
        INTERSECT("||");

        String sym;

        public String getSym() {
            return sym;
        }

        Operation(String sym) {
            sym = sym;
        }
    }

    Operation op;
    CamflowObject e1;
    CamflowObject e2;

    // TODO should really just be a check on the labels...
    public SetOperation(Operation op, CamflowObject e1, CamflowObject e2) {
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    public CamflowObject getE1() {
        return e1;
    }

    public CamflowObject getE2() {
        return e2;
    }

    public Operation getOp() {
        return op;
    }

    public String getOpSym() {
        return op.getSym();
    }

    @Override
    public Label getLabel() {
        return null;
    }

    // TODO need to consolidate SetOperation and ChainedChecks
    public SetOperation(Operation op, List<CamflowObject> checks) {
        switch (checks.size()) {
            case 0: e1 = null; e2 = null;
            case 1: e1 = checks.get(0); e2 = null;
            case 2: e1 = checks.get(0); e2 = checks.get(1); this.op = op;
            default: this.op = op; e1 = checks.get(0); e2 = new SetOperation(op, checks.subList(1, checks.size()-1));
        }
    }
}
