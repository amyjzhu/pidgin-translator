package test.programs.pdg;

public class MultiThrowCatch {
	public static void main(String[] args) {

	    int x = 1;
	    int y = 2;
		try {
		    if (x == 3) {
		        throw new E1();
		    } else {
		        throw new E2();
		    }
		} catch(Throwable e) {
		    y = 4;
		} 
		y = 5;
	}
}

class E1 extends Throwable {
    
}

class E2 extends Throwable {
    
}
