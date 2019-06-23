package test.programs.guessinggame;

import java.util.Scanner;

public class GuessingGame {
	IntegerSanitizer sanitizer;

	public GuessingGame() {
		super();
		sanitizer = new IntegerSanitizer();
	}

	public void play() {
		Scanner scanner = new Scanner((java.io.InputStream) System.in);
		RandomIntegerGenerator gen = new RandomIntegerGenerator();
		((java.io.PrintStream) System.out).print("\n^   ^\n - -\n >o<\n\n");
		((java.io.PrintStream) System.out).print("Enter a number between 1 and 10: ");
		int guess;
		String guessString = /*@input "initial"*/(String) ((Scanner) scanner).next();
		while (!this.isValidInput((String) guessString)) {
			((java.io.PrintStream) System.out).print("\nOops, please enter a valid number between 1 and 10: ");
			guessString = /*@input "loop"*/ (String) ((Scanner) scanner).next();
		}
		guess = ((Integer) new Integer((String) guessString)).intValue();
		int secret = ((RandomIntegerGenerator) gen).generateInRange(1, 10);
		boolean correctGuess = GuessingGame.equal(secret, guess);
		if (correctGuess) {
			((java.io.PrintStream) System.out).print(/*@output*/ (String) String.format("\nAwesome! You correctly guessed %d. ", (Object[]) (new Object[] { Integer.valueOf(secret) })));
			((java.io.PrintStream) System.out).print(("\n\n      nn       \n    (*.|       \n     |.  )     \n     " + "UUf_,)/   \n\n"));
			((java.io.PrintStream) System.out).print("Play again? y/n?:");
		} else {
			((java.io.PrintStream) System.out).print(/*@output*/ (String) String.format("\nSorry! Your guess of %d is incorrect. The number was %d.", (Object[]) (new Object[] { Integer.valueOf(guess), Integer.valueOf(secret) })));
			((java.io.PrintStream) System.out).print(("\n\n    |\\   /|      \n    ( *o* )        \n   (  v v  )   " + "  \n     ^   ^         \n\n"));
			((java.io.PrintStream) System.out).print("Play again? y/n?:");
		}
		String playAgain = (String) ((Scanner) scanner).next();
		while (!(((String) playAgain).equals("y") || ((String) playAgain).equals("n"))) {
			((java.io.PrintStream) System.out).print("\nEnter \"y\" or \"n\"!!! Play again? y/n: ");
			playAgain = (String) ((Scanner) scanner).next();
		}
		if (((String) playAgain).equals("y")) {
			this.play();
		} else {
			((java.io.PrintStream) System.out).println("Thanks for playing.");
			return;
		}
	}

	private boolean isValidInput(String guessString) {
		int guessInt;
		try {
			guessInt = ((Integer) new Integer((String) guessString)).intValue();
		} catch (NumberFormatException e) {
			return false;
		}
		int sanguess = ((IntegerSanitizer) sanitizer).rangeSanitize(guessInt, 1, 10);
		if (sanguess != guessInt) {
			return false;
		}
		return true;
	}

	private static boolean equal(int i, int j) {
		return i == j;
	}
}
