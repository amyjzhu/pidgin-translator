package test.programs;

public class Resource {

	boolean busy = false;
	void open() {
//		if (busy) {
//			throw new RuntimeException("already opened");
//		}
		busy = true;
	}
	
	void close() {
//		if (!busy) {
//			throw new RuntimeException("not opened");
//		}
		busy = false;
	}
	
	public static void main(String[] args) {
		Resource r1 = new Resource();
		Resource r2 = r1;
		r1.open();
		r1.close();
	}
}
