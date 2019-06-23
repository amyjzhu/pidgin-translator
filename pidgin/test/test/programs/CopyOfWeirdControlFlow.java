package test.programs;

public class CopyOfWeirdControlFlow {

	public static void main(String[] args) {
		int h = /*@input "H"*/ args.length;
		boolean b = h > 5;
		boolean c = h < 7;
	
		try {
			if(b){
				if(c){
					throw new NullPointerException();
				} else {
					
				}
			} else {
				throw new NullPointerException();
			}
		} catch (NullPointerException e){
			int x = /*@output*/ h;
		}
	}
}
