package test.cfl;

public class TwoStaticCalls {
    
    static int secretInt = 42;
    static int publicInt = 43;
    
    public static void main(String[] args) {
        
        int secretOut = foo(secretInt);
        int publicOut = foo(publicInt);
        System.err.println(publicOut);
        secretInt = secretOut;
    }

    private static int foo(int arg) {
        return arg + 37;
    }
}
