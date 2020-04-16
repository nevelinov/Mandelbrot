package main;

import java.util.Date;
import java.util.LinkedList;

public class Task implements Runnable{
	LinkedList<Subtask> subtasks;
	String taskName;
	Date start;
	Date end;
	long duration;
	boolean isQuiet;
	
	Task(String taskName, boolean isQuiet)
	{
		this.taskName = taskName;
		this.isQuiet = isQuiet;
		subtasks = new LinkedList<Subtask>();
	}

	@Override
	public void run() {
		start = new Date();
		
		if(isQuiet == false) {
			System.out.printf("%s started%n", taskName);
		}
		
		subtasks.forEach(subtask -> subtask.perform());
		end = new Date();
		
		duration = end.getTime() - start.getTime();
		if(isQuiet == false) {
			System.out.printf("%s finished, execution time: %d ms%n",
					taskName, duration);
		}
	}
	
	void addSubtask(Subtask subtask)
	{
		subtasks.add(subtask);
	}
}