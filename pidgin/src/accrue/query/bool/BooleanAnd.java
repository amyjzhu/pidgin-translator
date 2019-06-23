package accrue.query.bool;

/**
 * The conjunction of two boolean expressions
 */
public class BooleanAnd implements BooleanExpression {
    
    /**
     * First conjunct
     */
    private final BooleanExpression e1;
    /**
     * Second conjunct
     */
    private final BooleanExpression e2;

    /**
     * e1 && e2
     * 
     * @param e1
     *            first expression
     * @param e2
     *            second expression
     */
    public BooleanAnd(BooleanExpression e1, BooleanExpression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public BoolType getType() {
        return BoolType.AND;
    }
    
    public BooleanExpression getB1() {
        return e1;
    }
    
    public BooleanExpression getB2() {
        return e2;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        if (e1 instanceof BooleanVariable || e1 instanceof BooleanNot) {
            s.append(e1.toString());
        } else {
            s.append("[" + e1 + "]");
        }
        s.append(" && ");
        if (e2 instanceof BooleanVariable || e2 instanceof BooleanNot) {
            s.append(e2.toString());
        } else {
            s.append("[" + e2 + "]");
        }
        return s.toString();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
        result = prime * result + ((e2 == null) ? 0 : e2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BooleanAnd other = (BooleanAnd) obj;
        if (e1 == null) {
            if (other.e1 != null)
                return false;
        } else if (!e1.equals(other.e1))
            return false;
        if (e2 == null) {
            if (other.e2 != null)
                return false;
        } else if (!e2.equals(other.e2))
            return false;
        return true;
    }    
}
