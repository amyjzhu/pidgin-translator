package test.programs.pdg;

public class Test23_MultiMerge {
    /**
     * @param args
     */
    public static void main(String[] args) {
        int y = 1;
        int z = 2;
        if(y == 3){
            z = 4;
            y = 5;
        } else {
            z = 6;
        }
        int w = z;
        int u = y;
    }
}
