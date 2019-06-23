package test.programs.pdg;

/**
 * Simplest function call 
 */
public class Test02_FunctionCall {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int y = 1;
		if ( y==2 ){
			y = function();
		} 
		else { y= 3; }
		y = y + 4;
	}

	public static int function(){
		int z = 5;
		return 6;
	}
}