package test.programs;

//==== Output for CodeContext [d=[null, null]].@output 2  :  ../GraphInfoflow/src/test/programs/BranchSimple.java:13,10-20====
//value: 
//[]
//   pc: 
//[H]

/**
 * Program with a branch statement to test the creation of "true" and "false"
 * edges
 */
public class BranchSimple {
	  public static void main(String[] args) {
		String numberIn = /*@input "H"*/ args[0];
		// Intentionally unused
		int x;
		if(numberIn == "1")
			x = /*@output*/ 2;
	  }
}
