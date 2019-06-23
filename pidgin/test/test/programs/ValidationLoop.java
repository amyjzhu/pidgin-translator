package test.programs;

import java.util.Scanner;

//==== Output for CodeContext [d=[null, null]].@output "The inpu..." + input  :  ../GraphInfoflow/src/test/programs/ValidationLoop.java:27,24-58====
//value: 
//[initial, loop]
//   pc: 
//[initial, loop]

/**
 * Simple FAILING test for the declassification policy, where the declassification is in a loop.
 * A more complex verification-type policy is needed.
 * <br><br>
 * This test fails because the declassification function does not return a value,
 * that directly feeds into the output, but instead there is a control flow dependent 
 * on the return from the declassification function.  This could be thought of
 * as a verification function rather than a declassification function.
 */
public class ValidationLoop {
	
	/**
     * The verification function, <code>test.programs.VerificationLoop.shorten(String,int)</code> 
     * is called in a loop
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner("filename");
		String input = /*@input "initial"*/ (scanner.next());
		
		while(!isShort(input, 4)){
			input =  /*@input "loop"*/(scanner.next());
		}
		System.out.println(/*@output*/("The input was: " + input));
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
