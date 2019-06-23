
package test.programs;
public class TryFinally02 {
    public static void main(String[] args) {
	try {
	    if (args.length > 3) {
		throw new RuntimeException();
	    }
	}
	finally {
	    int x;
	    try {
		try {
		    if (args.length > 3) {
			throw new IllegalArgumentException();
		    }
		}
		catch (IllegalArgumentException e) {
		    // catch it and discard it.
		    // should be an "other" edge to the finally
		}
		catch (RuntimeException e) {
		    // rethrow
		    throw e;
		}
		finally {
		    ;
		}
	    }
	    catch (IllegalArgumentException e) {
		// discard the exception and terminate normally.
	    }
	}
    }
}