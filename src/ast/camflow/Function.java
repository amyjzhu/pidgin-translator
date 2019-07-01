package ast.camflow;

public class Function extends CamflowObject {
    String name;
    String body;

    public Function(String name, String body) {
        this.name = name;
        this.body = body;
    }

    @Override
    public Label getLabel() {
        return null;
    }
}
