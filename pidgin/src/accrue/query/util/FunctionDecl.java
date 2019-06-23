package accrue.query.util;

import java.util.List;

/**
 * Function and argument names
 */
public class FunctionDecl {

    /**
     * Name of function or let
     */
    private final String name;
    /**
     * Name of arguments
     */
    private final List<String> args;

    /**
     * Creat a new function decl
     * 
     * @param name
     *            name of function
     * @param args
     *            name of arguments
     */
    public FunctionDecl(String name, List<String> args) {
        this.name = name;
        this.args = args;
    }

    
    public List<String> getArgs() {
        return args;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public int hashCode() {
        return 17 * name.hashCode() + 31 * args.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FunctionDecl)) {
            return false;
        }
        
        FunctionDecl other = (FunctionDecl)obj;
        return other.name.equals(this.name) && other.args.equals(other.args);
    }
    
    @Override
    public String toString() {
        return name + args;
    }
}
