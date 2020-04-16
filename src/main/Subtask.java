package main;

import java.awt.image.BufferedImage;
import org.apache.commons.math3.complex.Complex;

public class Subtask {
	double xStart;
	double xEnd;
	double yStart;
	double yEnd;
	int xPixelStart;
	int xPixelEnd;
	int yPixelStart;
	int yPixelEnd;
	BufferedImage bufImage;
	
	int maxItarations = 50;
	double escapeRadius = 2000000000;
	
	Subtask(double xStart, double xEnd, double yStart, double yEnd,
			int xPixelStart, int xPixelEnd, int yPixelStart, int yPixelEnd,
			BufferedImage bufImage)
	{
		this.xStart = xStart;
		this.xEnd = xEnd;
		this.yStart = yStart;
		this.yEnd = yEnd;
		this.xPixelStart = xPixelStart;
		this.xPixelEnd = xPixelEnd;
		this.yPixelStart = yPixelStart;
		this.yPixelEnd = yPixelEnd;
		this.bufImage = bufImage;
	}
	
	//  F(Z)=е^Z+C
	int calcPlus(double cRe, double cIm)
	{
		Complex c = new Complex(cRe, cIm);
		Complex z = c;
		Complex e = new Complex(Math.E, 0);
		
		int steps = 2;
		while(steps < 100 && z.abs() <= 50) {
			z = e.pow(z).add(c);
			steps++;
		}
		return steps;
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

	public void perform()
	{
		int steps;
		
		int pixelWidth = xPixelEnd - xPixelStart + 1;
		int pixelHeight = yPixelEnd - yPixelStart + 1;
		
		double xStep = Math.abs(xEnd - xStart) / pixelWidth;
		double yStep = Math.abs(yEnd - yStart) / pixelHeight;
		
		for(int x = 0; x < pixelWidth; x++) {
			for(int y = 0; y < pixelHeight; y++) {
				steps = calc(xStart + x*xStep, yStart + y*yStep);
				colorPixel(x + xPixelStart, y + yPixelStart, steps);
			}
		}	
	}

	@Override
	public String toString() {
		return String.format("[%.2f; %.2f]x[%.2f; %.2f]", xStart, xEnd, yStart, yEnd);
	}
}
