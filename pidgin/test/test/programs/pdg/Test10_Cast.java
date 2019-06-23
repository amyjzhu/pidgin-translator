package test.programs.pdg;

public class Test10_Cast {
	public static void main(String[] args) {
		IndexOutOfBoundsException e = new IndexOutOfBoundsException();

		RuntimeException e2 = e;

		ArithmeticException e3 = (ArithmeticException) e2;
	}
}
