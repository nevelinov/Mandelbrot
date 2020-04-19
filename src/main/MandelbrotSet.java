package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class MandelbrotSet {
	String imageName;
	BufferedImage bufImage;
	ArrayList<Task> tasks;
	int tasksCount;
	boolean isQuiet;

	public MandelbrotSet(int xPixels, int yPixels,
			double xStart, double xEnd, double yStart, double yEnd,
			String imageName, int tasksCount, boolean isQuiet)
	{
		this.imageName = imageName;
		this.tasksCount = tasksCount;
		this.isQuiet = isQuiet;
		
		initializeImage(xPixels, yPixels);

		tasks = new ArrayList<Task>(tasksCount);
		createTasks(xPixels, yPixels, xStart, xEnd, yStart, yEnd);
	}

	void initializeImage(int width, int height)
	{
		bufImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = bufImage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);
	}

	void saveImage()
	{
		try {
			ImageIO.write(bufImage, "PNG", new File(imageName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void createTasks(int width, int height, double a, double b, double c, double d)
	{
		double xStep = Math.abs(b - a) / width;
		double yStep = Math.abs(d - c) / height;
		
		for(int i = 0; i < tasksCount; i++) {
			tasks.add(new Task(width, height, i, tasksCount,
					xStep, yStep, a, c, isQuiet, bufImage));
		}
	}

	public void generate()
	{
		ExecutorService pool = Executors.newFixedThreadPool(tasksCount);
		Date start = new Date();
		Date end;
		
		tasks.forEach(
				task -> pool.execute(task)
				);
		pool.shutdown();
		
		try {
			pool.awaitTermination(360, TimeUnit.SECONDS);
		} catch (Exception e) {
			System.out.println("Exception while waiting.. :(");
		}

		saveImage();
		
		end = new Date();
		long runDuration = end.getTime() - start.getTime();
		
		if(isQuiet == false) {
			System.out.printf("%nTotal execution time for current run: %d ms%n", runDuration);
		}
	}
}
