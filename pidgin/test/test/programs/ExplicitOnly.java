package test.programs;

//==== Output for CodeContext [d=[null, null]].@output doingMoreStuff  :  ../GraphInfoflow/src/test/programs/ExplicitOnly.java:7,24-47====
//value: 
//[H]
//   pc: 
//[]
public class ExplicitOnly {
	  public static void main(String[] args) {
		long creditCardNumber = /*@input "H"*/ 1234567812345678L;
		long doingMoreStuff = creditCardNumber * 2;
		System.out.println(/*@output*/ doingMoreStuff);
	  }
}
