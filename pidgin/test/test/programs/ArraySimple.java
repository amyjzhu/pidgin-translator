package test.programs;

public class ArraySimple {
  public static void main(String[] args) {
    int secret = 42;
    int[] secretarr = new int[1];
    secretarr[0] = secret;
    int[] anotherarr = new int[1];
    System.arraycopy(secretarr, 0, anotherarr, 0, 1);
    int zzz = anotherarr[0];
  }
}
