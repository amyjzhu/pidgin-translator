package parser.parsetree;

public class Intersect extends Expression {

    Expression e1;
    Expression e2; // todo variadic? or just desugar

    public Intersect(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Expression getE1() {
        return e1;
    }

    public Expression getE2() {
        return e2;
    }
}
