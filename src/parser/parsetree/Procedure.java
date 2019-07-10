package parser.parsetree;

public class Procedure implements Expression {

    // TODO string or var?
    String name;

    Expression body;

    public Procedure(String name, Expression body) {
        this.name = name;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public Expression getBody() {
        return body;
    }
}
