package com.nsoft.nphysics;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;

public class ThreadManager {
	
	public static ArrayList<Task> tasks = new ArrayList<>();
	
	public static void createTask(Runnable r,float delay) {
		
		if(Gdx.app.getType() == ApplicationType.Desktop && NPhysics.useMultiThreading) {
			
			NPhysics.getThreadManager().startThread(r, (long)(delay*1000));
		}else {
			
			tasks.add(new Task(r,delay));
		}
	}
	
	public static void act(float delta) {
		
		for (Task task : tasks) {
			
			if(!task.shouldRun) continue;
			
			if(task.sum < task.delay) {
				task.sum += delta;
			}else {
				
				task.task.run();
				task.shouldRun = false;
			}
		}
	}
	
	static class Task{
		
		Runnable task;
		float delay;
		float sum;
		boolean shouldRun = true;
		public Task(Runnable task, float delay) {
			this.task = task;
			this.delay = delay;
		}
	}
}
