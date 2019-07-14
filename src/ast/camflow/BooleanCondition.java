package ast.camflow;

import ast.camflow.check.Check;

public class BooleanCondition {
    public BooleanCondition() {
    }

    static enum Relation {
        EQ("="),
        NE("!="),
        LT("<"),
        LE("<="),
        GE(">="),
        GT(">");

        String sym;

        public String getSym() {
            return sym;
        }

        Relation(String sym) {
            sym = sym;
        }
    }

    Relation relation;
    Check e1;
    Check e2;
    // TODO possibly need to handle an e2 -- depends on arithmetic


    public BooleanCondition(Relation relation, Check e1, Check e2) {
        this.relation = relation;
        this.e1 = e1;
        this.e2 = e2;
    }

    public Relation getRelation() {
        return relation;
    }

    public String getRelationString() {
        return relation.getSym();
    }

    public Check getE1() {
        return e1;
    }

    public Check getE2() {
        return e2;
    }
}