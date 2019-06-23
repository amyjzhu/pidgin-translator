
package test.programs;
public class TryFinally01 {
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
		finally {
		    ;
		}
	    }
	    catch (RuntimeException e) {
		// discard the exception and terminate normally.
	    }
	}
    }
}