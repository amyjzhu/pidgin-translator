package parser.parsetree;

public class Assign {

    Var varName;
    Expression and;
    Expression or;

    public Assign(String varName, Expression e1, Expression e2) {
        this.varName = new Var(varName);
        this.and = e1;
        this.or = e2;
    }

}
