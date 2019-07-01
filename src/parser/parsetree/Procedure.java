package parser.parsetree;

public class Procedure {

    Var name;

    String body;

    public Procedure(Var name, String body) {
        this.name = name;
        this.body = body;
    }

    public Var getName() {
        return name;
    }

    public String getBody() {
        return body;
    }
}
