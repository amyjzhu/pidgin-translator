package test.programs;

public class TryFinally04 {
    public static void main(String[] args) {
     	foo(null);
    }

    public static int foo(TryFinally04 x) {
        try {
	    java.lang.Integer i = 0; while (++i < 1) {}
	    return 1;
        } finally {
            x.set(null);
        }
    }

    void set(Object o) { }
}