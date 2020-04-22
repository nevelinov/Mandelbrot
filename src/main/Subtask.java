package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.apache.commons.math3.complex.Complex;

public class Subtask {
	double xStart;
	double xEnd;
	double yStart;
	double yEnd;
	short xPixelStart;
	short xPixelEnd;
	short yPixelStart;
	short yPixelEnd;
	BufferedImage bufImage;
	BufferedImage temp;
	
	short maxItarations = 50;
	double escapeRadius = 2000000;
	
	Subtask(double xStart, double xEnd, double yStart, double yEnd,
			short xPixelStart, short xPixelEnd, short yPixelStart, short yPixelEnd,
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
		this.temp = new BufferedImage(xPixelEnd - xPixelStart + 1,
				yPixelEnd - yPixelStart + 1, BufferedImage.TYPE_3BYTE_BGR);
	}
	
	short calc(double cRe, double cIm)
	{
		Complex z = new Complex(0.0, 0.0);
		Complex c = new Complex(cRe, cIm);
		Complex e = new Complex(Math.E, 0);
		
		short iterations = 0;
		
		while(iterations < maxItarations && z.abs() < escapeRadius) {	
			z = e.pow(z).subtract(c);
			iterations++;
		}
		
		return iterations;
	}
	
	void colorPixel(short x, short y, short iterations)
	{
		int color = 255 << 24;
		
		if(iterations >= maxItarations) {
			temp.setRGB(x, y, color);
			return;
		}
		
		int red = (int)(255 * ((maxItarations - iterations) / (double)(maxItarations - 1)));
		int green = (int)(255 * (1 - (Math.abs(maxItarations / 2.0 - iterations) / (maxItarations / 2))));
		int blue = (int)(255 * ((iterations - 1) / (double)(maxItarations - 1)));
		
		color += red << 16;
		color += green << 8;
		color += blue;
		
		temp.setRGB(x, y, color);
	}

	public void perform()
	{
		short steps;
		
		short pixelWidth = (short) (xPixelEnd - xPixelStart + 1);
		short pixelHeight = (short) (yPixelEnd - yPixelStart + 1);
		
		double xStep = Math.abs(xEnd - xStart) / pixelWidth;
		double yStep = Math.abs(yEnd - yStart) / pixelHeight;
		
		for(short x = 0; x < pixelWidth; x++) {
			for(short y = 0; y < pixelHeight; y++) {
				steps = calc(xStart + x*xStep, yStart + y*yStep);
				colorPixel(x, y, steps);
			}
		}
		
		Graphics2D g = bufImage.createGraphics();
		g.drawImage(temp, null, xPixelStart, yPixelStart);
		g.dispose();
	}

	@Override
	public String toString() {
		return String.format("[%.2f; %.2f]x[%.2f; %.2f]", xStart, xEnd, yStart, yEnd);
	}
}
