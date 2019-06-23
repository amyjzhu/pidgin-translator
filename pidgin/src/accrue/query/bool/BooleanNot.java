package accrue.query.bool;

/**
 * Negation of a boolean expression
 */
public class BooleanNot implements BooleanExpression {

    /**
     * Expression this is the negation for
     */
    private final BooleanExpression b;

    /**
     * !b boolean expression
     * 
     * @param b
     *            expression to negate
     */
    public BooleanNot(BooleanExpression b) {
        this.b = b;
    }

    /**
     * Get the expression being negated
     * 
     * @return expression being negated
     */
    public BooleanExpression getNegated() {
        return b;
    }

    @Override
    public BoolType getType() {
        return BoolType.NOT;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("!");
        if (b instanceof BooleanVariable || b instanceof BooleanNot) {
            s.append(b.toString());
        } else {
            s.append("[" + b + "]");
        }
        return s.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((b == null) ? 0 : b.hashCode());
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
        BooleanNot other = (BooleanNot) obj;
        if (b == null) {
            if (other.b != null)
                return false;
        } else if (!b.equals(other.b))
            return false;
        return true;
    }
}
