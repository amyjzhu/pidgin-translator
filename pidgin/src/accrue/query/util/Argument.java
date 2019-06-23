package accrue.query.util;

import accrue.pdg.PDGEdgeType;
import accrue.query.expression.Expression;
import accrue.query.expression.Variable;

/**
 * Actual argument to a QL function
 * 
 * @param <T>
 *            type of argument
 */
public class Argument<T> {
    /**
     * value
     */
    private final T arg;
    /**
     * True if the argument does not exist
     */
    private final boolean absent;

    /**
     * Create a new actual argument
     * 
     * @param arg
     *            value
     */
    public Argument(T arg) {
        this.absent = false;
        this.arg = arg;
    }

    /**
     * Create a new actual argument with no value
     */
    Argument() {
        this.absent = true;
        this.arg = null;
    }

    /**
     * True if this argument is missing
     * 
     * @return True if this argument is missing
     */
    public boolean isAbsent() {
        return absent;
    }

    /**
     * True if this is an actual for a non-variable {@link Expression}
     * 
     * @return True if this is an actual for a non-variable {@link Expression}
     */
    public boolean isExpression() {
        if (!(arg instanceof Expression) || arg instanceof Variable) {
            return false;
        }
        return true;
    }

    /**
     * True if this is an actual for an {@link Variable}
     * 
     * @return True if this is an actual for an {@link Variable}
     */
    public boolean isVariable() {
        return arg instanceof Variable;
    }

    /**
     * Get the actual argument value
     * 
     * @return value
     */
    public T value() {
        return arg;
    }

    /**
     * Get an empty Actual
     * 
     * @param <T>
     *            type of the missing actual
     * @return static empty actual
     */
    public static <T> Argument<T> absent() {
        return new Argument<T>();
    }

    @Override
    public String toString() {
        if (absent) {
            return "absent";
        }
        if (arg instanceof String) {
            return "\"" + arg.toString() + "\"";
        }
        return arg.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (absent ? 1231 : 1237);
        result = prime * result + ((arg == null) ? 0 : arg.hashCode());
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
        @SuppressWarnings("rawtypes")
        Argument other = (Argument) obj;
        if (absent != other.absent)
            return false;
        if (arg == null) {
            if (other.arg != null)
                return false;
        }
        else if (!arg.equals(other.arg))
            return false;
        return true;
    }

    /**
     * Get a string from an argument. It is either a String or a variable that
     * needs to be looked up in env
     * 
     * @param arg
     *            argument to get the string for
     * @param env
     *            current variable environment
     * @return String for arg
     */
    public static String getStringForArg(Argument<?> arg, Environment env) {
        String str;
        if (arg.isAbsent()) {
            return null;
        }
        if (arg.value() instanceof String) {
            str = (String) arg.value();
        } else if (arg.value() instanceof Variable) {
            str = ((Variable) arg.value()).evaluateString(env);
        } else {
            throw new IllegalArgumentException("Argument is the wrong type. Expected: String, got: "
                    + arg.value().getClass().getSimpleName()
                    + ", Value: "
                    + arg.value());
        }
        return str;
    }

    /**
     * Get an edgetype from an argument. It is either a PDGEdgeType or a
     * variable that needs to be looked up in env
     * 
     * @param arg
     *            argument to get the PDGEdgeType for
     * @param env
     *            current variable environment
     * @return PDGEdgeType for arg
     */
    public static PDGEdgeType getEdgeTypeForArg(Argument<?> arg, Environment env) {
        PDGEdgeType e;
        if (arg.isAbsent()) {
            return null;
        }
        if (arg.value() instanceof PDGEdgeType) {
            e = (PDGEdgeType) arg.value();
        } else if (arg.value() instanceof Variable) {
            e = ((Variable) arg.value()).evaluateEdgeType(env);
        } else {
            throw new IllegalArgumentException("Argument is the wrong type. Expected: String, got: "
                    + arg.value().getClass().getSimpleName()
                    + ", Value: "
                    + arg.value());
        }
        return e;
    }
}
