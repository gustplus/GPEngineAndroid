package com.gust.common.ui;

public abstract class GPEventListener {
	protected String type;
	public abstract boolean doEvent(GPEventDispatcher target);
	
	public void setType(String type){
		this.type = type;
	}
	
	public boolean isInstance(String type){
		return this.type.equals(type);
	}
}
