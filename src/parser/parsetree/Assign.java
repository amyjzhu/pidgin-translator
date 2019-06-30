package parser.parsetree;

public class Assign {

    Var varName;
    Expression e1;
    Expression e2;

    public Assign(String varName, Expression e1, Expression e2) {
        this.varName = new Var(varName);
        this.e1 = e1;
        this.e2 = e2;
    }

    public Assign(Var varName, Expression e1, Expression e2) {
        this.varName = varName;
        this.e1 = e1;
        this.e2 = e2;
    }
}
