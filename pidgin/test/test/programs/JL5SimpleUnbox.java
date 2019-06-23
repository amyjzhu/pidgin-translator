package test.programs;

/**
 * Class to test the JL5 to infoflow bridge
 */
public class JL5SimpleUnbox {
	public static void main(String[] args) {

		Integer x = /*@input "H"*/ 5;
		int y = x;
		
		System.out.println(/*@output*/ y);
		
	}
}
