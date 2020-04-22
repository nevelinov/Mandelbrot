package main;

import java.util.Calendar;
import java.util.LinkedList;

public class Task implements Runnable{
	LinkedList<Subtask> subtasks;
	String taskName;
	long start;
	long end;
	boolean isQuiet;
	
	Task(String taskName, boolean isQuiet)
	{
		this.taskName = taskName;
		this.isQuiet = isQuiet;
		subtasks = new LinkedList<Subtask>();
	}

	@Override
	public void run() {
		if(isQuiet == false) {
			start = Calendar.getInstance().getTimeInMillis();
			System.out.printf("%s started%n", taskName);
		}
		
		subtasks.forEach(subtask -> subtask.perform());
		
		if(isQuiet == false) {
			end = Calendar.getInstance().getTimeInMillis();
			System.out.printf("%s finished, execution time: %d ms%n",
					taskName, end - start);
		}
	}
	
	void addSubtask(Subtask subtask)
	{
		subtasks.add(subtask);
	}
}