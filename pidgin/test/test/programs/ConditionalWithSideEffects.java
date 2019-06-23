package test.programs;

public class ConditionalWithSideEffects {
    
    Integer foo;
    
    public static void main(String[] args) {
        
        ConditionalWithSideEffects s = new ConditionalWithSideEffects();
        Integer es = s.foo;
        Integer bar = es != null ? es : (s.foo = new Integer(1));

    }
}