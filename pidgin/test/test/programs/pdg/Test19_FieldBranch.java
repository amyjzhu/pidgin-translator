package test.programs.pdg;

public class Test19_FieldBranch {
	public static void main(String[] args) {
		ObjWithField x = new ObjWithField();
		ObjWithField y = new ObjWithField();
		boolean secret = true;
		ObjWithField o;
		if(secret){
			o = x;
		} else {
			o = y;
		}
		o.field = 5;	
		int output = x.field;
	}
}
