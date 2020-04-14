package test;

import java.util.LinkedList;

public class Task implements Runnable{
	LinkedList<Subtask> subtasks;
	String name;
	
	Task(String name)
	{
		this.name = name;
		subtasks = new LinkedList<Subtask>();
	}

	@Override
	public void run() {
		subtasks.forEach(subtask -> subtask.perform());
	}
	
	void addSubtask(Subtask subtask)
	{
		subtasks.add(subtask);
	}
}