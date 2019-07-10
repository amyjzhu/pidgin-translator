package parser.parsetree;

public class Var implements Argument {
    String name;

    public Var(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
