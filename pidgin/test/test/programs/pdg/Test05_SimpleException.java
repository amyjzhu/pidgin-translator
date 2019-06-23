package test.programs.pdg;

public class Test05_SimpleException {
	public static void main(String[] args) {
		int x = 5;
		if (x == 0)
			throw new RuntimeException();
		else
			x = 7;
		int y = x;
	}
}
