package test.programs.guessinggame;

public class SimpleGameSecretIsInput {
    public static void main(String[] args) {
        int secret = /*@input "Secret"*/getRandom(1, 10);
        output("Guess a number from 1 to 10.");
        int guess = getInput();
        while ((!isValidInput(guess))) {
            output(/*@output*/(guess + " is not valid." + "Guess a number from 1 to 10"));
            guess = getInput();
        }

        boolean correctGuess = ((secret) == guess);
        if (correctGuess) {
            output(/*@output*/("Congratulations! " + guess + " was right."));
        } else {
            output(/*@output*/("Sorry, incorrect " + secret + " was that answer."));
        }
    }

    private static boolean isValidInput(int guess) {
        // Check whether the guess is between 1 and 10
        return guess != 0;
    }

    private static void output(String toPrint) {
        // Output the string
    }

    private static int getInput() {
        // Get the user input and return it
        return 0;
    }

    private static int getRandom(int i, int j) {
        // Get a random number between i and j
        return 0;
    }
}
