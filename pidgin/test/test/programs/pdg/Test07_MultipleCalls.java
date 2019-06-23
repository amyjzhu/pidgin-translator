package test.programs.pdg;

public class Test07_MultipleCalls {
	public static void main(String[] args) {
		int x = 5;
		if (x == 0) x = f(3);
		else if (x == 1) x = f(4);
		x = x + 1;
	}
	
	public static int f(int arg){
		int y = arg + 5;
		return y;
	}
}
