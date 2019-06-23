package test.programs;

public class IfElse {
	
	public static void main(String[] args) {
		int h = /*@input "H"*/ args.length;
		
		int x;
		if (h > 5){
			x = 1;
		} else {
			x = 2;
		}
		int y = /*@output*/ x;
		
	}
}
