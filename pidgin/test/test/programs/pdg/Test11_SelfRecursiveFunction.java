package test.programs.pdg;

/**
 * Simple recursive functions to test PDG creation 
 */
public class Test11_SelfRecursiveFunction {
	public static void main(String[] args) {
		int x = 6;
		f(x);
	}
	
	public static int f(int argToF) {
		if (argToF < 0) return (argToF + 1);
		return f(argToF - 1);
	}
}


