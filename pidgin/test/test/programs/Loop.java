package test.programs;

/**
 * Test for a simple while loop
 */
public class Loop {
	public static void main(String[] args) {
		int x = /*@input "H"*/5; 
		while (x > 0){
			x = x - 1;
		}
		int y = /*@output*/(x + 7);
	}
}
