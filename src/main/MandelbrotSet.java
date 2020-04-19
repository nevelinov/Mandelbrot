package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class MandelbrotSet {
	String imageName;
	BufferedImage bufImage;
	ArrayList<Task> tasks;
	int tasksCount;
	int subtasksCountPerTask;
	boolean isQuiet;

	public MandelbrotSet(int xPixels, int yPixels,
			double xStart, double xEnd, double yStart, double yEnd,
			String imageName, int tasksCount, boolean isQuiet)
	{
		this.imageName = imageName;
		this.tasksCount = tasksCount;
		this.isQuiet = isQuiet;
		
		this.subtasksCountPerTask = (tasksCount == 1) ? 1 : 8;
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
		int xPixelsWidth = (int) Math.ceil(width / (double)tasksCount);
		int yPixelsHeight = (int) Math.ceil(height / (double)subtasksCountPerTask);
		
		double xWidth = Math.abs(b - a) / tasksCount;
		double yHeight = Math.abs(d - c) / subtasksCountPerTask;
				
		int xPixelStart, xPixelEnd;
		int yPixelStart, yPixelEnd;
		
		double xStart, xEnd;
		double yStart, yEnd;
		
		LinkedList<Subtask> subtasks = new LinkedList<Subtask>();
		
		
			
		for(int i = 0; i < tasksCount; i++) {
			xPixelStart = i * xPixelsWidth;
			xStart = a + i * xWidth;

			if(i == tasksCount - 1) {
				xPixelEnd = width - 1;
				xEnd = b;
			} else {
				xPixelEnd = (i+1) * xPixelsWidth - 1;
				xEnd = a + (i+1) * xWidth;
			}

			for(int j = 0; j < subtasksCountPerTask; j++) {
				yPixelStart = j * yPixelsHeight;
				yStart = c + j * yHeight;

				if(j == subtasksCountPerTask - 1) {
					yPixelEnd = height - 1;
					yEnd = d;
				} else {
					yPixelEnd = (j + 1) * yPixelsHeight - 1;
					yEnd = c + (j + 1) * yHeight;
				}
				subtasks.add(
						new Subtask(xStart, xEnd, yStart, yEnd,
								xPixelStart, xPixelEnd, yPixelStart, yPixelEnd,
								bufImage)
						);
			}
			// uncomment to see current subtask's data
			//System.out.printf("[%d; %d]x[%d; %d]%n", xPixelStart, xPixelEnd, yPixelStart, yPixelEnd);
			//System.out.printf("[%f; %f]x[%f; %f]%n%n", xStart, xEnd, yStart, yEnd);
		}
		
		dealSubtasks(subtasks);
	}
	
	void dealSubtasks(LinkedList<Subtask> subtasks)
	{
	
		for(int i = 1; i <= tasksCount; i++) {
			tasks.add(new Task("Thread " + Integer.toString(i), isQuiet));
		}
		
		int current = 0;
		@SuppressWarnings("unused")
		int i = 1;
		while(!subtasks.isEmpty()) {
			// uncomment to see where each task is dealt
			//System.out.printf("Subtask(%d): %s goes to thread %d%n",
			//		i++, subtasks.getFirst(), current + 1);
			
			tasks.get(current++).addSubtask(subtasks.removeFirst());
			current %= tasksCount;
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
