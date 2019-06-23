package test.programs;

public class MergeCallResults {
    
    public static void main(String[] args) {
       MergeCallResults s = new MergeCallResults();
       s = s.foo(1);
    }
    
    MergeCallResults foo(int x) {
        if (x > 1) {
            throw new RuntimeException();
        }
        return new MergeCallResults();
    }
}

