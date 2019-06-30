package parser.parsetree;

public class Backwards extends Expression {

    Expression from;

    public Backwards(Expression to) {
        super();
    }

    public Expression getFrom() {
        return from;
    }
}
