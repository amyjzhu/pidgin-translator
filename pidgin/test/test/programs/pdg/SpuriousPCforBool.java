package test.programs.pdg;

public class SpuriousPCforBool {
	public static void main(String[] args) {

		boolean secret = true;
		int o = 3;
		if (secret == false){
			o = 5;
		} else {
			o = 6;
		}
	}
}
