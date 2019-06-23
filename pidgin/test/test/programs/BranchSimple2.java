package test.programs;

//==== Output for CodeContext [d=[null, null]].@output 1  :  ../GraphInfoflow/src/test/programs/BranchSimple2.java:15,10-20====
//value: 
//[]
//   pc: 
//[]

/**
 * Program with a branch statement to test the creation of "true" and "false"
 * edges
 */
public class BranchSimple2 {
	  public static void main(String[] args) {
		int number = 1;
		// Intentionally unused
		int x = 0;
		if(number == 1){
			x = /*@input "input"*/ 2;
		} else
			x = /*@output*/ 1;
	  }
}
