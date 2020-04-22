package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class MandelbrotSet {
	CMDLineParser cmdLine;
	String imageName;
	BufferedImage bufImage;
	ArrayList<Task> tasks;
	short tasksCount;
	short subtasksCountPerTask;
	long start;
	long end;
	boolean isQuiet;

	public MandelbrotSet(String[] args)
	{
		start = Calendar.getInstance().getTimeInMillis();
		cmdLine = new CMDLineParser(args);
		
		initializeFields();
	}
	
	void initializeFields()
	{
		this.imageName = cmdLine.getImageName();
		this.tasksCount = cmdLine.getTasksCount();
		this.subtasksCountPerTask = (tasksCount == 1) ? 1 : cmdLine.getGranularity();
		this.isQuiet = cmdLine.isQuiet();
		
		bufImage = new BufferedImage(cmdLine.getxPixels(), cmdLine.getyPixels(), BufferedImage.TYPE_3BYTE_BGR);
		tasks = new ArrayList<Task>(tasksCount);
		
		createTasks(cmdLine.getxPixels(), cmdLine.getyPixels(),
				cmdLine.getxStart(), cmdLine.getxEnd(),
				cmdLine.getyStart(), cmdLine.getyEnd());
	}

	void saveImage()
	{
		try {
			ImageIO.write(bufImage, "PNG", new File(imageName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void createTasks(short width, short height, double a, double b, double c, double d)
	{
		short xPixelsWidth = (short) Math.round(width / (double)tasksCount);
		short yPixelsHeight = (short) Math.round(height / (double)subtasksCountPerTask);
		
		double xWidth = Math.abs(b - a) / tasksCount;
		double yHeight = Math.abs(d - c) / subtasksCountPerTask;
				
		short xPixelStart, xPixelEnd;
		short yPixelStart, yPixelEnd;
		
		double xStart, xEnd;
		double yStart, yEnd;
		
		LinkedList<Subtask> subtasks = new LinkedList<Subtask>();
			
		for(short i = 0; i < tasksCount; i++) {
			xPixelStart = (short) (i * xPixelsWidth);
			xStart = a + i * xWidth;

			if(i == tasksCount - 1) {
				xPixelEnd = (short) (width - 1);
				xEnd = b;
			} else {
				xPixelEnd = (short) ((i+1) * xPixelsWidth - 1);
				xEnd = a + (i+1) * xWidth;
			}

			for(short j = 0; j < subtasksCountPerTask; j++) {
				yPixelStart = (short) (j * yPixelsHeight);
				yStart = c + j * yHeight;

				if(j == subtasksCountPerTask - 1) {
					yPixelEnd = (short) (height - 1);
					yEnd = d;
				} else {
					yPixelEnd = (short) ((j + 1) * yPixelsHeight - 1);
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
	
		for(short i = 1; i <= tasksCount; i++) {
			tasks.add(new Task("Thread " + Integer.toString(i), isQuiet));
		}
		
		int current = 0;
		@SuppressWarnings("unused")
		short i = 1;
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
			
		if(isQuiet == false) {
			end = Calendar.getInstance().getTimeInMillis();
			System.out.printf("%nTotal execution time for current run: %d ms%n", end - start);
		}
	}
}
