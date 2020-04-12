package test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.math3.complex.*;

public class MandelbrotSet {
	int width;
	int height;
	double a;
	double b;
	double c;
	double d;
	String name;
	BufferedImage bufImage;
	
	public MandelbrotSet(int width, int height,
			double a, double b, double c, double d, String name) {
		this.width = width;
		this.height = height;
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.name = name;
		
		initializeImage();
	}
	
	void initializeImage() {
		bufImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = bufImage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);
	}
	
	int calc(Complex c) {
		int steps = 2;
		Complex e = new Complex(Math.E, 0);
		
		Complex z = c;
		while(steps < 100 && z.abs() <= 50) {
			z = e.pow(z).add(c);
			steps++;
		}
		return steps;
	}
	
	public void run()
	{
		int steps;
		double xStep = Math.abs(b - a) / width;
		double yStep = Math.abs(d - c) / height;
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				steps = calc(new Complex (a + i*xStep, c + j*yStep));
				//System.out.printf("(%f, %f) to (%d, %d) - %d%n", i * xStep, j * yStep, i, j, steps);

				if (steps == 2) {
					bufImage.setRGB(i, j, Color.red.getRGB());
				} else if (3 <= steps && steps <= 6) {
					bufImage.setRGB(i, j, Color.orange.getRGB());
				} else if (7 <= steps && steps <= 12) {
					bufImage.setRGB(i, j, Color.pink.getRGB());
				} else if (13 <= steps && steps <= 23) {
					bufImage.setRGB(i, j, Color.yellow.getRGB());
				} else if (24 <= steps && steps <= 34) {
					bufImage.setRGB(i, j, Color.green.getRGB());
				} else {
					bufImage.setRGB(i, j, Color.blue.getRGB());
				}
			}
		}
		
		try {
			ImageIO.write(bufImage, "PNG", new File(name + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
