package test.programs;


//==== Output for CodeContext [d=[null, null]].@output "The inpu..." + input + input2  :  ../GraphInfoflow/src/test/programs/DeclassTwoFunctions.java:20,24-67====
//value: 
//[initial, loop]
//   pc: 
//[initial, loop]
/**
 * Simple test for the declassification policy, where the declassification is in a loop.
 */
public class DeclassTwoFunctions {
	
	/**
	 * The declassification functions,
	 * <code>test.programs.DeclassTwoFunctions.makeShort(java.lang.String, int)</code> and
	 * <code>test.programs.DeclassTwoFunctions.truncate(java.lang.String, int)</code> are both used
	 */
	public static void main(String[] args) {
		String input = /*@input "initial"*/ "input string is too long";
		input = makeShort(input, 4);

		String input2 = /*@input "loop"*/"second input";
		input2 = truncate(input2, 4);

		System.out.println(/*@output*/("The input was: " + input + input2));
	}
	
	/**
	 * This is a declassification function
	 */
	public static String makeShort(String toBeDeclassified, int maxLength){
		if(toBeDeclassified.length() < maxLength){
			return toBeDeclassified;
		}
		
		return toBeDeclassified.substring(0, maxLength-1);
	}
	
	/**
	 * This is a declassification function
	 */
	public static String truncate(String toBeDeclassified, int maxLength){
		if(toBeDeclassified.length() < maxLength){
			return toBeDeclassified;
		}
		
		return toBeDeclassified.substring(0, maxLength-1);
	}
}
