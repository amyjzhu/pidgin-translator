package test.programs;

//==== Output for CodeContext [d=[null, null]].@output input2  :  ../GraphInfoflow/src/test/programs/DeclassImplicit.java:20,24-39====
//value: 
//[42]
//   pc: 
//[]

/**
 * Simplest test for the declassification policy
 */
public class DeclassImplicit {
	
	/**
     * The declass function is <code>test.programs.DeclassLoop.makeSmall(int)</code> 
     * and is followed by an implicit flow from input to secret code
	 */
	public static void main(String[] args) {
		int input = /*@input "42"*/ 42;
		// Intentionally unused
		int secretCode = 0;
		if(input == 42){
			secretCode = 52;
		}
		int input2 = makeSmall(input);
		System.out.println(/*@output*/(input2));
	}
	
	/**
	 * This is the declassification function
	 */
	public static int makeSmall(int toBeDeclassified){
		// declassification happens here
		
		return toBeDeclassified;
	}
}
