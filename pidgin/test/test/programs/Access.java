package test.programs;

public class Access {
    public static void main(String[] args) {
        int x = 0;

        if (check()) {
            if(x > 0) {
                x = 5;
            } else {
                x = 6;
            }
            x = 7;
        }
        
        output(x);
    }

    private static void output(int x) {
    }

    private static boolean check() {
        return false;
    }   
}
