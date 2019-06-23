package test.programs.guessinggame;

public class IntegerSanitizer {
    
    public int rangeSanitize(int value, int low, int high) {
        int range = high - low;
        value = (value - low) % range + low;
        return value;
    }
    
    public IntegerSanitizer() { super(); }
}
