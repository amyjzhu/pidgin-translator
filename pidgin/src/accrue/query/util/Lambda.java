package accrue.query.util;

import java.util.List;

import accrue.query.expression.Expression;

/**
 * Lambda waiting to be applied
 */
public class Lambda {

    /**
     * Closure
     */
    private final Closure c;
    /**
     * argument names
     */
    private final List<String> formals;

    /**
     * Lambda function
     * 
     * @param args
     *            argument names
     * @param c
     *            closure
     */
    public Lambda(List<String> args, Closure c) {
        this.formals = args;
        this.c = c;
    }

    /**
     * A thunk is a function with no arguments
     * 
     * @return true if this function has no arguments
     */
    public boolean isThunk() {
        return formals.isEmpty();
    }

    /**
     * Apply this function passing in actual arguments
     * 
     * @param actuals
     *            actual arguments
     * @return Results of applying the function to the given actuals
     */
    public Object apply(List<Object> actuals) {
        if (formals.size() != actuals.size()) {
            throw new RuntimeException(
                    "Applying function to: "
                            + actuals.size()
                            + ", expected: "
                            + formals.size()
                            + " Why did the parser let this through?");
        }
        Environment bodyEnv = c.getEnvironment();
        Expression body = c.getFunctionBody();
        for (int i = 0; i < actuals.size(); i++) {
            bodyEnv = bodyEnv.extend(formals.get(i), actuals.get(i));
        }
       // return body.evaluate(bodyEnv);
        // TODO
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Lambda)) {
            return false;
        }

        Lambda other = (Lambda) obj;
        return other.c.equals(this.c) && other.formals.equals(other.formals);
    }

    @Override
    public int hashCode() {
        return 31 * c.hashCode() + 23 * formals.hashCode();
    }

    @Override
    public String toString() {
        String f = "";
        f += (formals.size() > 1 ? "(" : "");
        for (String ff : formals) {
            f += ff + " ";
        }
        f = f.trim();
        f += (formals.size() > 1 ? ")" : "");

        return "\u03BB" + f.trim() + "." + c;
    }
}
