package test.programs.pdg;

public class Test06_ThrowMultiple {
	public static void main(String[] args) {
		int x = 5;
		if (x == 0) throw new RuntimeException();
		else if (x == 1) throw new RuntimeException();
	}
}
