package test.programs;

//==== Output for CodeContext [d=[null, null]].@output input2  :  ../GraphInfoflow/src/test/programs/DeclassFailOtherArg.java:17,24-39====
//value: 
//[42]
//   pc: 
//[]

/**
 * This should fail since the declassified argument is the first and the flow is
 * into the second
 */
public class DeclassFailOtherArg {

	/**
	 * The declass function,
	 * <code>test.programs.DeclassLoop.makeSmall(int, int)</code> is called with
	 * the arguments flipped
	 */
	public static void main(String[] args) {
		int input = /*@input"42"*/42;
		int input2 = makeSmall(5, input);
		System.out.println(/*@output*/(input2));
	}

	/**
	 * This is the declassification function
	 */
	public static int makeSmall(int toBeDeclassified, int size){
		// declassification happens here
		
		return toBeDeclassified - size;
	}
}
