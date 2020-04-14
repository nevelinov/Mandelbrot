package test;

public class Main {
	public static void main(String[] args) {
		MandelbrotSet mb =
				new MandelbrotSet(1640, 1640, 1, 1.3, 1.3, 1.6, "mb_test", 32);
		
		mb.generate();
		System.out.println("Done!");
	}
}
