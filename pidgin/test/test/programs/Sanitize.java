package test.programs;


public class Sanitize {

	public static void main(String[] args) {
		String s = /*@input "User1"*/"insert into tomato";
		execute(sanitize(s));
		
		String s2 = /*@input "User2"*/"drop database db";
		if (needsSanitization(s2))
			s2 = sanitize(s2);
		
		execute(s2);
		
		//execute(/*@input "Trusted"*/"select time()");
	}
	
	//Checks if query needs sanitization -- cheap to check
	public static boolean needsSanitization(String sql) {
		if (sql.contains("drop")) {
			return false;
		}
		return true;
	}
	
	//Removes `drop` from queries -- could be expensive
	public static String sanitize(String sql) {
		return sql.replaceAll("drop", "");
	}
	
	public static void execute(String sql) {
		
	}
}
