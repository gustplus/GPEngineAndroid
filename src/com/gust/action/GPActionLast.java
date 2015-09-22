package com.gust.action;

import com.gust.scene2D.GPNode2D;

public abstract class GPActionLast extends GPAction2D {
	protected float totalTime;
	protected float pastTime;

	@Override
	public void initWithComponent(GPNode2D component) {
		super.initWithComponent(component);
	}

	@Override
	public void reset() {
		pastTime = 0;
		stop();
	}
	
	@Override
	public boolean isDone() {
		return pastTime >= totalTime;
	}

}
