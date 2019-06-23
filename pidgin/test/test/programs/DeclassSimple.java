package test.programs;

//==== Output for CodeContext [d=[null, null]].@output input2  :  ../GraphInfoflow/src/test/programs/DeclassSimple.java:14,24-39====
//value: 
//[42]
//   pc: 
//[]

/**
 * Simplest test for the declassification policy
 */
public class DeclassSimple {
	
	/**
     * The declass function, <code>test.programs.DeclassLoop.makeSmall(int)</code> 
	 */
	public static void main(String[] args) {
		int input = /*@input "42"*/ 42;
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
