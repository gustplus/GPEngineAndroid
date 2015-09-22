package com.gust.action;

import com.gust.scene2D.GPNode2D;

public abstract class GPAction2D extends GPAction {
	public GPNode2D component;
	
	public void initWithComponent(GPNode2D component){
		this.component = component;
		this.reset();
	}
}
