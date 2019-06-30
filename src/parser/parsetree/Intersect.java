package parser.parsetree;

public class Intersect extends Expression {

    Expression e1;
    Expression e2; // todo variadic? or just desugar

    public Intersect(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }
}
