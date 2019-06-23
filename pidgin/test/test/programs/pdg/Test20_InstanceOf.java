package test.programs.pdg;

public class Test20_InstanceOf {
	public static void main(String[] args) {
		RuntimeException e = new NullPointerException();
		
		int y = 1;
		if (e instanceof ArithmeticException){
			y = 5;
		} else {
			y = 6;
		}
		int z = y;
	}
}
