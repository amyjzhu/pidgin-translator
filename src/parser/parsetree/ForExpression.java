package parser.parsetree;

public class ForExpression extends PrimitiveExpression {
    String literalExpression;

    public ForExpression(String literalExpression) {
        this.literalExpression = literalExpression;
    }

    public String getLiteralExpression() {
        return literalExpression;
    }
}
