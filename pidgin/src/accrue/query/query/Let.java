package accrue.query.query;

import java.util.Collections;

import accrue.query.expression.Expression;
import accrue.query.util.Closure;
import accrue.query.util.Environment;
import accrue.query.util.Lambda;

/**
 * Let bind an expression to a name
 */
public class Let extends Query {

    /**
     * variable name
     */
    private final String name;
    /**
     * let bound expression
     */
    private final Expression e;
    /**
     * query we are binding in
     */
    private final Query q;

    /**
     * New let binding
     * 
     * @param name
     *            variable name
     * @param e
     *            let bound expression
     * @param q
     *            query we are binding in
     */
    public Let(String name, Expression e, Query q) {
        this.name = name;
        this.e = e;
        this.q = q;
    }

//    @Override
//    public ProgramDependenceGraph evaluate(Environment env) {
//        Closure c = new Closure(e, env);
//        Lambda lam = new Lambda(Collections.<String> emptyList(), c);
//        return q.evaluate(env.extend(name, lam));
//    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Let)) {
            return false;
        }
        Let other = (Let) obj;
        return this.name.equals(other.name) &&
                this.e.equals(other.e) &&
                this.q.equals(other.q);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + e.hashCode() + q.hashCode();
    }
    
    @Override
    public String toString() {
        return "let " + name + " = " + e + " in " + q;
    }
}
