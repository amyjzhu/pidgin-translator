package parser.parsetree;

public class Remove extends PrimitiveExpression {
    // remove condition ???

    Boolean isRemoveNode;
    Expression toRemove;

    public Remove(Boolean isRemoveNode, Expression toRemove) {
        this.isRemoveNode = isRemoveNode;
        this.toRemove = toRemove;
    }
}
