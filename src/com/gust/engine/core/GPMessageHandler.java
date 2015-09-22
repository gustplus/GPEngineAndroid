package com.gust.engine.core;

public abstract class GPMessageHandler {
	protected String event;
	public abstract void handleMessage();
	
	public void setType(String event){
		this.event = event;
	}
	
	public boolean isInstance(String type){
		return this.event.equals(type);
	}
}
