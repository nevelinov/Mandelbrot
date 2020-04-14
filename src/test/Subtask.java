package test;

import java.awt.Color;
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
	
	int calc(double cRe, double cIm)
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
	
	void colorPixel(int x, int y, int steps)
	{
		if (steps == 2) {
			bufImage.setRGB(x, y, Color.red.getRGB());
		} else if (3 <= steps && steps <= 6) {
			bufImage.setRGB(x, y, Color.orange.getRGB());
		} else if (7 <= steps && steps <= 12) {
			bufImage.setRGB(x, y, Color.pink.getRGB());
		} else if (13 <= steps && steps <= 23) {
			bufImage.setRGB(x, y, Color.yellow.getRGB());
		} else if (24 <= steps && steps <= 34) {
			bufImage.setRGB(x, y, Color.green.getRGB());
		} else {
			bufImage.setRGB(x, y, Color.blue.getRGB());
		}
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
}
