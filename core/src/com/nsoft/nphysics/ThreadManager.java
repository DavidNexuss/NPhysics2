/*NPhysics
Copyright (C) 2018  David Garcia Tejeda

Contact me at davidgt7d1@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

package com.nsoft.nphysics;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;

/**
 * Classe encarregada de manejar diferents tasques en paral·lel, en cas de que la plataforma
 * actual suporti l'ús del multi fil s'utilitza, en cas contrari s'afegeix la tasca dins del bucle
 * de renderitzat, només s'utilitza per tasques programades en un interval d'espera.
 * @author David
 */
public class ThreadManager {
	
	public static ArrayList<Task> stackTasks = new ArrayList<>();
	public static ArrayList<Task> tasks = new ArrayList<>();
	
	public static Runnable empty = ()->{};
	
	public static Task createTimer(float delay) {
		
		Task t = new Task(empty, delay);
		flowTask(t);
		
		return t;
		
	}
	public static Task createTask(Runnable r,float delay) {
		
		Task t = new Task(r, delay);
		tasks.add(t);
		
		flowTask(t);
		return t;
	}
	
	private static void flowTask(Task t) {
		
		if(Gdx.app.getType() == ApplicationType.Desktop && NPhysics.useMultiThreading) {
			
			NPhysics.getThreadManager().startThread(()->{
				
				t.task.run();
				t.shouldRun = false;
			}, (long)(t.delay*1000));
		}else {
			
			stackTasks.add(t);
		}
	}
	private static ArrayList<Task> buffer = new ArrayList<>();
	public static void act(float delta) {
		
		for (Task task : stackTasks) {
			
			if(task.isComplete()) continue;
			
			if(task.sum < task.delay) {
				task.sum += delta;
				buffer.add(task);
			}else {
				
				task.task.run();
				task.shouldRun = false;
			}
		}
		
		stackTasks.clear();
		stackTasks.addAll(buffer);
		buffer.clear();
	}
	
	public static class Task{
		
		Runnable task;
		float delay;
		float sum;
		boolean shouldRun = true;
		public Task(Runnable task, float delay) {
			this.task = task;
			this.delay = delay;
		}
		
		public boolean isComplete() {
			
			return !shouldRun;
		}
		
		public static Task createEmpty() {
			
			Task t = new Task(null, 0);
			t.shouldRun = false;
			return t;
		}
	}
}
