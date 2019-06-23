package accrue.query.expression;

import accrue.pdg.PDGEdgeType;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.PDGNodeType;
import accrue.query.util.Environment;

/**
 * Variable expression TODO no types here, also cheating this is not always an
 * expression
 */
public class Variable extends Expression {

    /**
     * Variable name
     */
    private String name;

    /**
     * New variable expression
     * 
     * @param name
     *            variable name
     */
    public Variable(String name) {
        this.name = name;
    }

    @Override
    public ProgramDependenceGraph evaluate(Environment env) {
        Expression e;
        Object o = env.lookup(name);
        if (o == null) {
            throw new IllegalArgumentException("No value found for Expression: " + name);
        }

        if (o instanceof Expression) {
            e = (Expression) o;
            return e.evaluate(env);
        } else if (o instanceof ProgramDependenceGraph) {
            return (ProgramDependenceGraph) o;
        } else {
            throw new IllegalArgumentException("Argument is the wrong type. Expected Expression or ProgramDependenceGraph, got " + o.getClass().getSimpleName() + " value is " + o);
        }
    }

    /**
     * Evaluate this variable to a String
     * 
     * @param env
     *            environment
     * @return String for varname
     */
    public String evaluateString(Environment env) {
        String e;
        Object o = env.lookup(name);
        if (o == null) {
            throw new IllegalArgumentException("No value found for String: " + name);
        }

        if (o instanceof String) {
            e = (String) o;
        } else {
            throw new IllegalArgumentException("Argument is the wrong type. Expected String, got " + o.getClass().getSimpleName() + " value is " + o);
        }
        return e;
    }

    /**
     * Evaluate this variable to an Integer
     * 
     * @param env
     *            environment
     * @return Integer for varname
     */
    public Integer evaluateInteger(Environment env) {
        Integer e;
        Object o = env.lookup(name);
        if (o == null) {
            throw new IllegalArgumentException("No value found for Integer: " + name);
        }

        if (o instanceof Integer) {
            e = (Integer) o;
        } else {
            throw new IllegalArgumentException("Argument is the wrong type. Expected Integer, got " + o.getClass().getSimpleName() + " value is " + o);
        }
        return e;
    }

    /**
     * Evaluate this variable to a PDGNodeType
     * 
     * @param env
     *            environment
     * @return PDGNodeType for varname
     */
    public PDGNodeType evaluateNodeType(Environment env) {
        PDGNodeType e;
        Object o = env.lookup(name);
        if (o == null) {
            throw new IllegalArgumentException("No value found for NodeType: " + name);
        }

        if (o instanceof PDGNodeType) {
            e = (PDGNodeType) o;
        } else {
            throw new IllegalArgumentException("Argument is the wrong type. Expected PDGNodeType, got " + o.getClass().getSimpleName() + " value is " + o);
        }
        return e;
    }

    /**
     * Evaluate this variable to a PDGEdgeType
     * 
     * @param env
     *            environment
     * @return PDGEdgeType for varname
     */
    public PDGEdgeType evaluateEdgeType(Environment env) {
        PDGEdgeType e;
        Object o = env.lookup(name);
        if (o == null) {
            throw new IllegalArgumentException("No value found for EdgeType: " + name);
        }

        if (o instanceof PDGEdgeType) {
            e = (PDGEdgeType) o;
        } else {
            throw new IllegalArgumentException("Argument is the wrong type. Expected PDGEdgeType, got " + o.getClass().getSimpleName() + " value is " + o);
        }
        return e;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Variable)) {
            return false;
        }
        Variable other = (Variable) obj;

        return other.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return 37 * name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
    
    /**
     * Get the variable name
     * 
     * @return variable name
     */
    public String getName() {
        return name;
    }
}
