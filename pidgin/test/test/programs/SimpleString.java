package test.programs;

public class SimpleString {
  public static void main(String[] args) {
      int secret = 42;
    //    String sss = "Hello " + new String(secret);
    String ttt = "Hello " + String.valueOf(secret);
    String uuu = "Hello " + secret;
    String vvv = "Hello" + String.valueOf(secret).substring(1);
  }
}
