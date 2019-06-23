package accrue.query.cheat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import accrue.algorithm.restrict.Restrictor;
import accrue.pdg.ProgramDependenceGraph;
import accrue.pdg.node.AbstractPDGNode;
import accrue.query.expression.Expression;
import accrue.query.primitive.PrimitiveExpression;
import accrue.query.util.Argument;
import accrue.query.util.Environment;
import accrue.util.OrderedPair;

/**
 * Cheat by programmatically instantiating a Java class
 */
public class Restrict extends PrimitiveExpression {

    /**
     * parameter holding the name of the class to be instantiated
     */
    private final Argument<?> className;
    /**
     * input nodes used by the class
     */
    private final Expression ins;
    /**
     * output nodes used by the class
     */
    private final Expression outs;

    /**
     * Create a new Restrictor from a class name, a set of input nodes, and a
     * set of output nodes
     * 
     * @param className
     *            class name
     * @param ins
     *            input nodes
     * @param outs
     *            output nodes
     */
    public Restrict(Argument<?> className, Expression ins, Expression outs) {
        this.className = className;
        this.ins = ins;
        this.outs = outs;
    }

    @Override
    public ProgramDependenceGraph evaluate(ProgramDependenceGraph g, Environment env) {

        String name = Argument.getStringForArg(className, env);

        Restrictor r = null;
        try {
            Class<?> restrictClass = Class.forName("accrue.algorithm.restrict." + name);
            Constructor<?> cons = restrictClass.getConstructor();
            r = (Restrictor) cons.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to create Restrictor: " + e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to create Restrictor: " + e);
        } catch (SecurityException e) {
            throw new RuntimeException("Failed to create Restrictor: " + e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to create Restrictor: " + e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to create Restrictor: " + e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to create Restrictor: " + e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to create Restrictor: " + e);
        }
        System.err.println("Running: " + name + " Restrictor");

        Set<AbstractPDGNode> inSet = getInputSet(env);
        Set<AbstractPDGNode> outSet = getOutputSet(env);

        ProgramDependenceGraph res = r.restrict(g, inSet, outSet);

        return res;
    }

    /**
     * Get the set of nodes for the input set
     * 
     * @param env
     *            current variable environment
     * @return set of input nodes
     */
    private Set<AbstractPDGNode> getInputSet(Environment env) {
        return ins.evaluate(env).vertexSet();
    }

    /**
     * Get the set of nodes for the output set
     * 
     * @param env
     *            current variable environment
     * @return set of output nodes
     */
    private Set<AbstractPDGNode> getOutputSet(Environment env) {
        return outs.evaluate(env).vertexSet();
    }

    @Override
    public Object getAdditionalCacheKey(Environment env) {
        OrderedPair<Set<AbstractPDGNode>, Set<AbstractPDGNode>> insAndOuts =
                new OrderedPair<Set<AbstractPDGNode>, Set<AbstractPDGNode>>(
                        getInputSet(env),
                        getOutputSet(env));

        return new OrderedPair<String, OrderedPair<Set<AbstractPDGNode>, Set<AbstractPDGNode>>>(
                Argument.getStringForArg(className, env),
                insAndOuts);
    }

    @Override
    public String toString() {
        return "restrict(" + className + ", " + ins + ", " + outs + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((className == null) ? 0 : className.hashCode());
        result = prime * result + ((ins == null) ? 0 : ins.hashCode());
        result = prime * result + ((outs == null) ? 0 : outs.hashCode());
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
        Restrict other = (Restrict) obj;
        if (className == null) {
            if (other.className != null)
                return false;
        }
        else if (!className.equals(other.className))
            return false;
        if (ins == null) {
            if (other.ins != null)
                return false;
        }
        else if (!ins.equals(other.ins))
            return false;
        if (outs == null) {
            if (other.outs != null)
                return false;
        }
        else if (!outs.equals(other.outs))
            return false;
        return true;
    }
}
