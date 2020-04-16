package main;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CMDLineParser {
	final String defaultImageSize = "640x480";
	final String defaultIntervals = "-2.0:2.0:-1.0:1.0";
	final short defaultTasksCount = 1;
	final String defaultImageName = "zad15.png";
	final boolean defaultIsQuiet = false;
	
	int xPixels;
	int yPixels;
	
	double xStart;
	double xEnd;
	double yStart;
	double yEnd;
	
	int tasksCount;
	
	String imageName;
		
	boolean isQuiet;
	
	Options options;
	DefaultParser parser;
	CommandLine cmdLine;
	
	public CMDLineParser(String[] cmdLineArguments) {
		initializeOptions();
		parseOptions(cmdLineArguments);
		
	}

	private void initializeOptions() {
		options = new Options();
		
		options.addOption("s", "size", true, "-s or -size");
		options.addOption("r", "rect", true, "-r or -rect");
		options.addOption("t", "task", true, "-t or -tasks");
		options.addOption("o", "output", true, "-o or -output");
		options.addOption("q", "quiet", false, "-q or -quiet");
	}
	
	private void parseOptions(String[] cmdLineArguments) {
		parser = new DefaultParser();
		
		try {
	        cmdLine = parser.parse( options, cmdLineArguments );
	    }
	    catch( ParseException exp ) {
	        System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
	    }
		
		parseImageSize();
		parseIntervals();
		
		tasksCount = cmdLine.hasOption("t") ?
				Integer.parseInt(cmdLine.getOptionValue("t")) : defaultTasksCount;
		imageName = cmdLine.getOptionValue("o", defaultImageName);
		isQuiet = cmdLine.hasOption("q") ? true : defaultIsQuiet;
	}
	
	private void parseImageSize() {	
		String[] sizes = cmdLine.getOptionValue("s", defaultImageSize).split("x");
		
		xPixels = Integer.parseInt(sizes[0]);
		yPixels = Integer.parseInt(sizes[1]);
	}
	
	private void parseIntervals() {
		String[] points = cmdLine.getOptionValue("r", defaultIntervals).split(":");
		
		xStart = Double.parseDouble(points[0]);
		xEnd = Double.parseDouble(points[1]);	
		yStart = Double.parseDouble(points[2]);
		yEnd = Double.parseDouble(points[3]);
	}

	public int getxPixels() {
		return xPixels;
	}

	public int getyPixels() {
		return yPixels;
	}

	public double getxStart() {
		return xStart;
	}

	public double getxEnd() {
		return xEnd;
	}

	public double getyStart() {
		return yStart;
	}

	public double getyEnd() {
		return yEnd;
	}

	public String getImageName() {
		return imageName;
	}

	public int getTasksCount() {
		return tasksCount;
	}

	public boolean isQuiet() {
		return isQuiet;
	}
}