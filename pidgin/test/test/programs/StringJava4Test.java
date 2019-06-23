package test.programs;

public class StringJava4Test {
    
    public static void main(String[] args) {
        ((java.io.PrintStream) System.out).println((String)
                                                     ((String)
                                                        ("Hi, " + "hello, ") +
                                                      "how are you"));
    }
    
    public StringJava4Test() { super(); }
}
