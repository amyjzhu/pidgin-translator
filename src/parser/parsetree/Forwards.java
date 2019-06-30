package parser.parsetree;

public class Forwards extends Expression {

    Expression from;

    public Forwards(Expression from) {
        this.from = from;
    }

    public Expression getFrom() {
        return from;
    }
}
