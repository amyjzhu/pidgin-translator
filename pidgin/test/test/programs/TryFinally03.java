package test.programs;

public class TryFinally03 {
    public static void main(String[] args) {
	TryFinally03 x = new TryFinally03();
        try {
        } finally {
            x.set(null);
        }
    }

    static void set(Object o) { }
}