package accrue.query.query;

import accrue.query.expression.Expression;
import accrue.query.util.Closure;
import accrue.query.util.Environment;
import accrue.query.util.FunctionDecl;
import accrue.query.util.Lambda;

/**
 * Let bind a function to a name
 */
public class LetFun extends Query {

    /**
     * function and argument names
     */
    private final FunctionDecl fun;
    /**
     * Let bound expression
     */
    private Expression e;
    /**
     * Query this function is bound in
     */
    private Query q;

    /**
     * Let bind a new function to an expression in a query
     * 
     * @param fun
     *            function and argument names
     * @param e
     *            expression to be bound
     * @param q
     *            query to bind in
     */
    public LetFun(FunctionDecl fun, Expression e, Query q) {
        this.fun = fun;
        this.e = e;
        this.q = q;
    }

    @Override
    public Object evaluate(Environment env) {
//        Closure c = new Closure(e, env);
//        Lambda lam = new Lambda(fun.getArgs(), c);
//
//        return q.evaluate(env.extend(fun.getName(), lam));
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LetFun)) {
            return false;
        }
        LetFun other = (LetFun) obj;
        return this.fun.equals(other.fun) &&
                this.e.equals(other.e) &&
                this.q.equals(other.q);
    }

    @Override
    public int hashCode() {
        return fun.hashCode() + e.hashCode() + q.hashCode();
    }
    
    @Override
    public String toString() {
        return "let " + fun.getName() + fun.getArgs() + " = " + e + " in " + q;
    }
}
