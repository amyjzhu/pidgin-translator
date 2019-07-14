package parser.parsetree;

import java.util.List;

public class Assign implements Expression {

    Var varName;
    List<Expression> and;
    List<Expression> or;

    public Assign(Var varName, List<Expression> and, List<Expression> or) {
        this.varName = varName;
        this.and = and;
        this.or = or;
    }

    public Var getVarName() {
        return varName;
    }

    public List<Expression> getAnd() {
        return and;
    }

    public List<Expression> getOr() {
        return or;
    }
}
