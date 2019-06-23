package accrue.query.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//import accrue.query.expression.OriginalGraph;

/**
 * Environment mapping variables to Objects, TODO no types here
 */
public class Environment {

    /**
     * Original PDG
     */
   // private static ProgramDependenceGraph originalPDG;

    /**
     * Variable map
     */
    private final Map<String, Object> vars;

    /**
     * Create a new environment
     * 
     * @param vars
     *            Variable map
     */
    private Environment(Map<String, Object> vars) {
        this.vars = vars;
    }

    /**
     * Create an empty environment
     * 
     * @return environment with just "pdg" bound to the original graph
     */
    public static Environment fresh() {
        Environment env = new Environment(
                Collections.<String, Object> singletonMap("pdg", new HashMap<String, Object>()));
        //Environment.originalPDG = pdg;
        return env;
    } // Probably should not have deleted PDG but I don't think
    // it was necessary for conversation

    /**
     * Create a new environment with the new mapping
     * 
     * @param id
     *            name
     * @param e
     *            expression
     * @return new environment updated with the new mapping
     */
    public Environment extend(String id, Object e) {
        Map<String, Object> newMap = new HashMap<String, Object>(vars);
        newMap.put(id, e);
        return new Environment(newMap);
    }

    /**
     * lookup a variable in the environment
     * 
     * @param id
     *            variable name
     * @return Object from the environment
     */
    public Object lookup(String id) {
        Object res = vars.get(id);
        if (res instanceof Lambda && ((Lambda)res).isThunk()) {
            // Call by need for thunks, once its looked up we evaluate
            //  and put back the results
            res = ((Lambda)res).apply(Collections.emptyList());
            vars.put(id, res);
        }
        
        return res;
    }

    /**
     * Get the original PDG
     * 
     * @return original PDG
     */
   // public ProgramDependenceGraph originalPDG() {
//        return originalPDG;
//    }

    @Override
    public String toString() {
        return vars.keySet().toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime + ((vars == null) ? 0 : vars.hashCode());
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
        Environment other = (Environment) obj;
        if (vars == null) {
            if (other.vars != null)
                return false;
        }
        else if (!vars.equals(other.vars))
            return false;
        return true;
    }
}