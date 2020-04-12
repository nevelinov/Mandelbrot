package test;

public class Main {
	public static void main(String[] args) {
		MandelbrotSet mb = new MandelbrotSet(640, 640, 1, 1.3, 1.3, 1.6, "mb_test");
		
		mb.run();
		System.out.println("Done!");
	}
}
