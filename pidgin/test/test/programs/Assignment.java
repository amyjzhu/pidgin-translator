package test.programs;

/**
 * Test the copy flow from x to y
 */
public class Assignment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int x = /*@input "H"*/ (5 + 6);
		int y = x;
		int z = y + x;
		System.out.println(/*@output*/z);
		
		int copyOfW = function(x, x+y);
		System.out.println(/*@output*/copyOfW);
	}

	public static int function(int copyOfX, int xPlusY){
		int w = copyOfX;
		int v = xPlusY;
		
		System.out.println(/*@output*/w);
		System.out.println(/*@output*/v);
		
		return w;
	}
}
