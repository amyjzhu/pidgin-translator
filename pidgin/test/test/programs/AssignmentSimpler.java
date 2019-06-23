package test.programs;

/**
 * Test the copy flow from x to y
 */
public class AssignmentSimpler {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int x = /*@input "H"*/ (5 + 6);
		int y = 6;
		int z = y* x;
		
		int returnedValue = function(y);
	}

	public static int function(int copyOfY){
		return copyOfY;
	}
}
