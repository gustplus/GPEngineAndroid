package com.gust.action;

public abstract class GPAction {	
	 protected boolean isRunning;
	public void start(){
		isRunning = true;
	}
	
	public void stop(){
		isRunning = false;
	}
	
	public abstract void update(float deltaTime);
	
	public abstract boolean isDone();
	
	public abstract void reset();
	
	public abstract GPAction clone();
}
