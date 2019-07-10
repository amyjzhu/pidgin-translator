package parser.parsetree;

import java.util.List;

public class Program {
    List<Expression> exprs;
    Policy p;

    public Program(List<Expression> exprs, Policy p) {
        this.exprs = exprs;
        this.p = p;
    }

    public List<Expression> getExprs() {
        return exprs;
    }

    public Policy getP() {
        return p;
    }
}
