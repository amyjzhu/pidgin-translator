package test.programs;

public class WeirdControlFlow {

	public static void main(String[] args) {
		int h = /*@input "H"*/ 1;
		boolean b = h > 5;
		boolean c = h < 7;
		int x = 5;
		try {
			if(b){
				if(c){
					throw new RuntimeException();
				} else {
					
				}
			} else {
				throw new RuntimeException();
			}
		} catch (RuntimeException e){
			x = 77;
		}
		int z = x;
	}
}
