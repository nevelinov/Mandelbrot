package main;

import java.awt.image.BufferedImage;
import java.util.Date;

import org.apache.commons.math3.complex.Complex;

public class Task implements Runnable{
	int width;
	int height;
	int xPixelStart;
	int jump;
	double xStep;
	double yStep;
	double xStart;
	double yStart;
	String taskName;
	Date start;
	Date end;
	long duration;
	boolean isQuiet;
	BufferedImage bufImage;
	
	int maxItarations = 50;
	double escapeRadius = 2000000;
	
	Task(int width, int height, int xPixelStart, int jump,
			double xStep, double yStep,
			double xStart, double yStart, boolean isQuiet,
			BufferedImage bufImage)
	{
		this.width = width;
		this.height = height;
		this.xPixelStart = xPixelStart;
		this.jump = jump;
		this.xStep = xStep;
		this.yStep = yStep;
		this.xStart = xStart;
		this.yStart = yStart;
		
		this.taskName = "Thread " + Integer.toString(xPixelStart);
		this.isQuiet = isQuiet;
		
		this.bufImage = bufImage;
	}
		
	int calc(double cRe, double cIm)
	{
		Complex z = new Complex(0.0, 0.0);
		Complex c = new Complex(cRe, cIm);
		Complex e = new Complex(Math.E, 0);
		
		int iterations = 0;
		
		while(iterations < maxItarations && z.abs() < escapeRadius) {	
			z = e.pow(z).subtract(c);
			iterations++;
		}
		
		return iterations;
	}
	
	void colorPixel(int x, int y, int iterations)
	{
		int color = 255 << 24;
		
		if(iterations >= maxItarations) {
			bufImage.setRGB(x, y, color);
			return;
		}
		
		int red = (int)(255 * ((maxItarations - iterations) / (double)(maxItarations - 1)));
		int green = (int)(255 * (1 - (Math.abs(maxItarations / 2.0 - iterations) / (maxItarations / 2))));
		int blue = (int)(255 * ((iterations - 1) / (double)(maxItarations - 1)));
		
		color += red << 16;
		color += green << 8;
		color += blue;
		
		bufImage.setRGB(x, y, color);
	}

	public void run()
	{
		int steps;
		
		start = new Date();
		if(isQuiet == false) {
			System.out.printf("%s started%n", taskName);
		}
		
		int x = xPixelStart;
		for(int y = 0; y < height; y++) {
			for(;x < width; x += jump) {				
				steps = calc(xStart + x * xStep, yStart + y * yStep);
				colorPixel(x, y, steps);
			}
			x %= width;
		}
		
		end = new Date();
		duration = end.getTime() - start.getTime();
		if(isQuiet == false) {
			System.out.printf("%s finished, execution time: %d ms%n",
					taskName, duration);
		}
	}
}
