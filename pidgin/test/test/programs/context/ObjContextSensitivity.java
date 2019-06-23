package test.programs.context;

public class ObjContextSensitivity {

    public static void main(String[] args) {
        C c1 = new C();
        C c2 = new C();
        
        int a = /*@input "a"*/5;
        int b = c1.identity(a);
        int c = 6;
        int d = c2.identity(c);
        
        int e = /*@output*/b;
        int f = /*@output*/d;
        int g = c1.identity(c);
        
        int h = /*@output*/g;
    }
}
