package test.programs;

//==== Output for CodeContext [d=[null, null]].@output inputInt  :  ../GraphInfoflow/src/test/programs/IntegerValueOf.java:9,24-41====
//value: 
//[number]
//   pc: 
//[number]
public class IntegerValueOf {
	
	public static void main(String[] args) {
		String input = /*@input "number"*/ "42";	
		int inputInt = new Integer(input).intValue();
		System.out.println(/*@output*/ inputInt);
	}
}