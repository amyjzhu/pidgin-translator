package parser.parsetree;

public class Between implements Expression {

    Expression from;
    Expression to;

    public Between(Expression from, Expression to) {
        this.from = from;
        this.to = to;
    }

    public Expression getFrom() {
        return from;
    }

    public Expression getTo() {
        return to;
    }
}
