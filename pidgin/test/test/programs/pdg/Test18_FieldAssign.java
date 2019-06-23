package test.programs.pdg;

public class Test18_FieldAssign {
	public static void main(String[] args) {
        int y = 0;
        ObjWithField x = null;
        if (y == 2){
            x = new ObjWithField();
        }
        x.field = 3;
	}
}
