package test.programs;

//==== Output for CodeContext [d=[null, null]].@output "The inpu..." + input2  :  ../GraphInfoflow/src/test/programs/DeclassLoop.java:20,24-59====
//value: 
//[initial, loop]
//   pc: 
//[initial, loop]

/**
 * Simple test for the declassification policy, where the declassification is in a loop.
 */
public class DeclassLoop {
	
	/**
     * The declassification function, <code>test.programs.DeclassLoop.makeShort(String,int)</code> 
     * is called in a loop
	 */
	public static void main(String[] args) {
		String input = /*@input "initial"*/ "input string is too long";
		
		String input2 = makeShort(input, 4);
		while(!input.equals(input2)){
			input =  /*@input "loop"*/input.substring(0, input.length()-2);
			input2 = makeShort(input, 4);
		}
		System.out.println(/*@output*/("The input was: " + input2));
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
