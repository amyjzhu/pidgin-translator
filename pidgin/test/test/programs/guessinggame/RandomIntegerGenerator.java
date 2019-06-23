package test.programs.guessinggame;

import java.util.Random;

public class RandomIntegerGenerator {
    private Random rng;
    
    public RandomIntegerGenerator() {
        super();
        rng = new Random();
    }
    
    public int generateInRange(int low, int high) {
        int random = ((Random) rng).nextInt(high - low);
        random += low;
        return random;
    }
}
