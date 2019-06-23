package test.programs;

//==== Output for CodeContext [d=[null, null]].@output 5  :  ../GraphInfoflow/src/test/programs/ImplicitOnly.java:9,25-35====
//value: 
//[]
//   pc: 
//[number]
/**
 * This program has an implicit flow from "input" to the print statement, but no explicit flow.
 */
public class ImplicitOnly {

	public static void main(String[] args) {
		int input = /*@input "number"*/ 42;
		
		if(input > 0){
			System.out.println(/*@output*/ 5);
		}		
	}
}
