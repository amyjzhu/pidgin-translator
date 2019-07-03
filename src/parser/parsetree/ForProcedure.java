package parser.parsetree;

public class ForProcedure extends PrimitiveExpression {

    Var procedureName;

    public ForProcedure(Var procedureName) {
        this.procedureName = procedureName;
    }

    public Var getProcedureName() {
        return procedureName;
    }
}
