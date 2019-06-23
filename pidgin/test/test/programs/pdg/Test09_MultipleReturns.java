package test.programs.pdg;

public class Test09_MultipleReturns {
	public static void main(String[] args) {
		int x = 5;
		x = f(x);
		return;
	}

	private static int f(int argToF) {
		if (argToF == 0)
			return 42;
		return argToF + 17;
	}
}
