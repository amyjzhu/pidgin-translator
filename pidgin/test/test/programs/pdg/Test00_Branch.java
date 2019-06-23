package test.programs.pdg;

/**
 * Simplest branch 
 */
public class Test00_Branch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int y = 6;
		if (y == 5){
			y = 7;
		} else {
			y = 8;
		}
		int x = y;
	}
}