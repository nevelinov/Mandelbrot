package main;

public class Main {
	public static void main(String[] args) {
		CMDLineParser cmdLine = new CMDLineParser(args);
		
		MandelbrotSet mb =
				new MandelbrotSet(720, 720,
						-2, 2,
						-2, 2,
						"test.png",
						2,
						false);
		mb.generate();
		
		
		
		/*MandelbrotSet mb =
				new MandelbrotSet(cmdLine.getxPixels(), cmdLine.getyPixels(),
						cmdLine.getxStart(), cmdLine.getxEnd(),
						cmdLine.getyStart(), cmdLine.getyEnd(),
						cmdLine.getImageName(),
						cmdLine.getTasksCount(),
						cmdLine.isQuiet());
						
		mb.generate();*/
	}
}
