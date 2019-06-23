package test.programs;

/**
 * Print out the last four numbers of a credit card
 */
public class DeclassCC {
	
	public static void main(String[] args) {
		long creditCard = /*@input "H"*/ 1234567812345678L;
		long declassified = lastFour(creditCard);
		System.out.println(/*@output*/(declassified));
	}
	
	/**
	 * The declassification function, <code>test.programs.DeclassLoop.makeSmall(int)</code> 
	 */
	public static long lastFour(long toBeDeclassified){
		// declassification happens here
		long declass = toBeDeclassified % 10000;
		return declass;
	}
}
