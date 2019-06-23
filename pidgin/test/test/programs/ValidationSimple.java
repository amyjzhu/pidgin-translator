package test.programs;

//==== Output for CodeContext [d=[null, null]].@output "The inpu..." + input  :  ../GraphInfoflow/src/test/programs/ValidationSimple.java:21,25-59====
//value: 
//[initial]
//   pc: 
//[initial]

/**
 * Simple FAILING test for the declassification policy, where the declassification is in a loop.
 * A more complex verification-type policy is needed.
 * <br><br>
 * This test fails because the declassification function does not return a value,
 * that directly feeds into the output, but instead there is a control flow dependent 
 * on the return from the declassification function.  This could be thought of
 * as a verification function rather than a declassification function.
 */
public class ValidationSimple {
	
	/**
     * The verification function is <code>test.programs.VerificationLoop.isShort(String,int)</code> 
	 */
	public static void main(String[] args) {
		String input = /*@input "initial"*/ "input string is too long";
		
		if(isShort(input, 4)){
			System.out.println(/*@output*/("The input was: " + input));
		}	
	}
	
	/**
	 * This is the verification function
	 */
	public static boolean isShort(String toBeDeclassified, int maxLength){
		if(toBeDeclassified.length() > maxLength){
			return false;
		}
		return true;
	}
}
