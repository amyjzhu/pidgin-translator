package test.programs.pdg;

/**
 * Simple recursive functions to test PDG creation 
 */
public class Test12_RecursiveFunctions {
	public static void main(String[] args) {
		int x = 42;
		even(x);
	}
	
	public static boolean even(int argToF) {
		if (argToF == 0) return true;
		return odd(argToF - 1);
	}
	
	public static boolean odd(int argToG) {
		if (argToG == 0) return false;
		return even(argToG - 1);
	}
	
	/*
	 * 6 -> x
	 * x -> argToF
	 * argToF -> argToF < 0 
	 * 0 -> argToF < 0
	 * argToF < 0 -> return (true)
	 * argToF -> argToF + 1
	 * 1 -> argToF + 1
	 * argToF + 1 -> return
	 * 
	 * argToF -> argToF - 1
	 * 1 -> argToF - 1
	 * argToF - 1 -> argToF
	 * argToF < 0 -> return (false)
	 * return f -> return f
	 */
}


