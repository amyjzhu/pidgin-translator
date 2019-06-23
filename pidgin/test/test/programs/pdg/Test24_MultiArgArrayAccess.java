package test.programs.pdg;

public class Test24_MultiArgArrayAccess {
	public static void main(String[] args) {
	    
	    String[] x = new String[5];
	    String a = "A";
	    String b = "B";
		x[1] = a;
		x[2] = b;
		
		String y = x[2];
	}
}
