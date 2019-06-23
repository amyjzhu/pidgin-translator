package test.programs.pdg;

public class Test08_TryCatch {
	public static void main(String[] args) {
		int y = 1;
		try {
			if (y == 0) throw new RuntimeException();
			y = 77;
		} catch (RuntimeException e){
			y = 1;
		}
		int x = y + 55;
	}

//	private static int f(int x) {
//		if (x == 13) throw new RuntimeException();
//		else return 6;
//	}
}
