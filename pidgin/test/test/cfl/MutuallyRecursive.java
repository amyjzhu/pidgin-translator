package test.cfl;

public class MutuallyRecursive {
    
    static int secretInt = 42;
    static int publicInt = 43;
    static boolean secretTest;
    
    public static void main(String[] args) {
        boolean publicEven = isEven(publicInt);
        System.err.println(publicEven);
        
        boolean secretEven = isEven(secretInt);
        secretTest = secretEven;
    }

    private static boolean isEven(int arg) {
        if (arg == 0) {
            return true;
        }
        return isOdd(arg - 1);
    }

    private static boolean isOdd(int i) {
        if (i == 0) {
            return false;
        }
        return isEven(i - 1);
    }
}
