package test.programs.pdg;

public class MissingCode {

    public static void main(String[] args) {
        int guess = 5;
        String hi = "Hi" + guess;
        if (guess == 5){
            return;
        }
        guess = 6;
        return;
    }
}
