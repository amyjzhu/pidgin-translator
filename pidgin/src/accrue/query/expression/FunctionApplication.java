package accrue.query.expression;

import java.util.LinkedList;
import java.util.List;

import accrue.pdg.ProgramDependenceGraph;
import accrue.query.util.Argument;
import accrue.query.util.Environment;
import accrue.query.util.Lambda;

/**
 * Function application expression
 */
public class FunctionApplication extends Expression {

    /**
     * Function name
     */
    private String name;
    /**
     * Actual arguments
     */
    private List<Argument<?>> actuals;

    /**
     * New function application expression
     * 
     * @param name
     *            function name
     * @param actuals
     *            actual arguments
     */
    public FunctionApplication(String name, List<Argument<?>> actuals) {
        this.name = name;
        this.actuals = actuals;
    }

    @Override
    public ProgramDependenceGraph evaluate(Environment env) {
        // First evaluate the arguments
        List<Object> args = evaluateArgs(env);
        
        if (!(env.lookup(name) instanceof Lambda)) {
            throw new RuntimeException("Trying to apply a non-function: " + name);
        }
        Lambda lam = (Lambda) env.lookup(name);
        long start = System.currentTimeMillis();
//        OrderedPair<List<Object>, Lambda> cacheKey = new OrderedPair<List<Object>, Lambda>(args, lam);
//        ProgramDependenceGraph cacheResults;
//        if ((cacheResults = getCachedResults(this.getClass(), cacheKey)) != null) {
//            System.err.println(this.toString() + ": " + (System.currentTimeMillis() - start) + " (cached)");
//            return cacheResults;
//        }
        
        ProgramDependenceGraph res = lam.apply(args);
//        storeCachedResults(this.getClass(), res, cacheKey);
        System.err.println(this.toString() + ": " + (System.currentTimeMillis() - start) + " (never cached)");
        return res;
    }
    
    /**
     * Evaluate the arguments in the given environment
     * 
     * @param env current environment
     * @return List of actual arguments
     */
    private List<Object> evaluateArgs(Environment env) {
        List<Object> args = new LinkedList<Object>();
        for (Argument<?> a : actuals) {
            if(a.isExpression()) {
                args.add(((Expression)a.value()).evaluate(env));
            } else if (a.isVariable()) {
                args.add(env.lookup(((Variable)a.value()).getName()));
            } else {
                args.add(a.value());
            }            
        }
        return args;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FunctionApplication)) {
            return false;
        }
        FunctionApplication other = (FunctionApplication) obj;
        return other.name.equals(this.name) && other.actuals.equals(this.actuals);
    }

    @Override
    public int hashCode() {
        return 37 * name.hashCode() + 31 * actuals.hashCode();
    }

    @Override
    public String toString() {
        String act = "(" + actuals.get(0);
        for (int i = 1; i < actuals.size(); i++) {
            act += ", " + actuals.get(i);
        }
        act += ")";
        return name + act;
    }
}
