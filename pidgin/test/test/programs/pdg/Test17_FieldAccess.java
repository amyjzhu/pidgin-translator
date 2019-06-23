package test.programs.pdg;

public class Test17_FieldAccess {
	public static void main(String[] args) {
		int y = 6;
		ObjWithField x = null;
		if (y == 5){
			x = new ObjWithField();
		}
		int z = x.field;
	}
}
