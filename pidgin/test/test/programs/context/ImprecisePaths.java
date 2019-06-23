package test.programs.context;

public class ImprecisePaths {
    public static void main(String[] args) {
        int guess = /*@input "Init"*/getInput();
        int test = /*@output*/guess;
        
        while ((!isValidInput(guess))) {
            guess = getInput();
        }
    }
    
    private static boolean isValidInput(int guess) {
        // Check whether the guess is between 1 and 10
        return guess != 0;
    }

    private static int getInput() {
        // Get the user input and return it
        return 0;
    }
}
