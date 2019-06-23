package test.programs.context;

public class C {
    public int identity(int in){
        return same(in);
    }
    
    public int same(int arg){
        return arg;
    }
}
