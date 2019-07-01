package ast.camflow;

import parser.parsetree.Expression;

public class BooleanCondition {
    static enum Relation {
        EQ,
        NE,
        LT,
        LE,
        GE,
        GT
    }

    Relation relation;
    Check e1;
    Check e2;
    // TODO possibly need to handle an e2 -- depends on arithmetic

}