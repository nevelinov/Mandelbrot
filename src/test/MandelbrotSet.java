package test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class MandelbrotSet {
	String imageName;
	BufferedImage bufImage;
	ArrayList<Task> tasks;
	int tasksCount;
	int subtasksCount;

	public MandelbrotSet(int width, int height,
			double a, double b, double c, double d,
			String imageName, int tasksCount)
	{
		this.imageName = imageName + ".png";
		this.tasksCount = tasksCount;
		this.subtasksCount = tasksCount * 2; // Might cause too many and too small subtasks
		initializeImage(width, height);

		tasks = new ArrayList<Task>(tasksCount);
		createTasks(width, height, a, b, c, d);
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
		int yPixelsHeight = (int) Math.ceil(height / (double)subtasksCount);
		
		double xWidth = Math.abs(b - a) / tasksCount;
		double yHeight = Math.abs(d - c) / subtasksCount;
				
		int xPixelStart, xPixelEnd;
		int yPixelStart, yPixelEnd;
		
		double xStart, xEnd;
		double yStart, yEnd;
		
		ArrayList<Subtask> subtasks = new ArrayList<>(tasksCount * subtasksCount);
		
		for(int j = 0; j < subtasksCount; j++) {
			yPixelStart = j * yPixelsHeight;
			yStart = c + j * yHeight;
			
			if(j == subtasksCount - 1) {
				yPixelEnd = height - 1;
				yEnd = d;
			} else {
				yPixelEnd = (j+1) * yPixelsHeight - 1;
				yEnd = c + (j+1) * yHeight;
			}
			
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
				
				subtasks.add(
						new Subtask(xStart, xEnd, yStart, yEnd,
								xPixelStart, xPixelEnd, yPixelStart, yPixelEnd,
								bufImage)
						);
				System.out.printf("[%d; %d]x[%d; %d]%n", xPixelStart, xPixelEnd, yPixelStart, yPixelEnd);
				System.out.printf("[%f; %f]x[%f; %f]%n%n", xStart, xEnd, yStart, yEnd);
			}
		}
		
		dealSubtasks(subtasks);
	}
	
	void dealSubtasks(ArrayList<Subtask> subtasks)
	{
		Collections.shuffle(subtasks);
		
		for(int i = 0; i < tasksCount; i++) {
			tasks.add(new Task("Thread " + Integer.toString(i)));
		}
		
		int current = 0;
		while(!subtasks.isEmpty()) {
			tasks.get(current++).addSubtask(subtasks.remove(subtasks.size() -1));
			current %= tasksCount;
		}
	}

	public void generate()
	{
		ExecutorService pool = Executors.newFixedThreadPool(tasksCount);

		tasks.forEach(
				task -> pool.execute(task)
				);
		pool.shutdown();
		
		try {
			pool.awaitTermination(60, TimeUnit.SECONDS);
		} catch (Exception e) {
			System.out.println("Execption while waiting.. :(");
		}

		saveImage();
	}
}
