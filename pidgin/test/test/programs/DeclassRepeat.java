package test.programs;

//==== Output for CodeContext [d=[null, null]].@output "The inpu..." + input + input2  :  ../GraphInfoflow/src/test/programs/DeclassRepeat.java:21,24-67====
//value: 
//[initial, loop]
//   pc: 
//[initial, loop]

/**
 * Simple test for the declassification policy, where the declassification
 * happens twice
 */
public class DeclassRepeat {
	
	/**
	 * The declassification function,
	 * <code>test.programs.DeclassLoop.makeShort(java.lang.String,int)</code> is
	 * called twice
	 */
	public static void main(String[] args) {
		String input = /*@input "initial"*/ "input string is too long";
		input = makeShort(input, 4);

		String input2 = /*@input "loop"*/"second input";
		input2 = makeShort(input2, 4);

		System.out.println(/*@output*/("The input was: " + input + input2));
	}
	
	/**
	 * This is the declassification function
	 */
	public static String makeShort(String toBeDeclassified, int maxLength){
		if(toBeDeclassified.length() < maxLength){
			return toBeDeclassified;
		}
		
		return toBeDeclassified.substring(0, maxLength-1);
	}
}
