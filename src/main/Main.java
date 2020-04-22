package main;

public class Main {
	public static void main(String[] args) {	
		MandelbrotSet mb = new MandelbrotSet(args);
		
		mb.generate();
	}
}
