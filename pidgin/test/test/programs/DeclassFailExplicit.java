package test.programs;

//==== Output for CodeContext [d=[null, null]].@output input  :  ../GraphInfoflow/src/test/programs/DeclassFailExplicit.java:18,24-38====
//value: 
//[42]
//   pc: 
//[]
//==== Output for CodeContext [d=[null, null]].@output input2  :  ../GraphInfoflow/src/test/programs/DeclassFailExplicit.java:19,24-39====
//value: 
//[42]
//   pc: 
//[]
/**
 * This test should fail since there is an explicit flow in addition to the
 * declass flow
 */
public class DeclassFailExplicit {
	
	/**
	 * The declass function,
	 * <code>test.programs.DeclassLoop.makeSmall(int)</code> is called, but not
	 * for both output channels
	 */
	public static void main(String[] args) {
		// TODO modify parser to allow white space before the @ as in /* @input "42" */
		int input = /*@input "42"*/ 42;
		int input2 = makeSmall(input);
		System.out.println(/*@output*/(input));
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
