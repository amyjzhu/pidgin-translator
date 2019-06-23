package test.programs.pdg;

/**
 * Simplest branch 
 */
public class Test01_Branch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int y = 6;
		int x = 5;
		int z = 2;
		if(x == y){
			z = 4;
		} else {
			if (x == z){
				z = 3;
			} 
		}
		int w = z;
	}
}