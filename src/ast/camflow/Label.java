package ast.camflow;

public class Label extends CamflowObject {

    public static Label REMOVE_LABEL = new Label("REMOVE");

    String text;

    public Label(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public Label getLabel() {
        return null;
    }
}
