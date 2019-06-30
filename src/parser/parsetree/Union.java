package parser.parsetree;

public class Union extends Expression {

    Expression e1;
    Expression e2;

    public Union(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }
}
