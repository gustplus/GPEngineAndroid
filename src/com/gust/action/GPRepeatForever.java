package com.gust.action;

import com.gust.scene2D.GPNode2D;

public class GPRepeatForever extends GPAction2D {

	private GPAction2D action;

	public GPRepeatForever(GPAction2D action) {
		// TODO Auto-generated constructor stub
		this.action = action;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		action.start();
	}

	@Override
	public void initWithComponent(GPNode2D component) {
		// TODO Auto-generated method stub
		super.initWithComponent(component);
		action.initWithComponent(component);
	}

	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		if (isRunning) {
			if (action.isDone())
				action.reset();
			action.update(deltaTime);
		}
	}

	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public GPAction clone() {
		// TODO Auto-generated method stub
		return new GPRepeatForever((GPAction2D)action.clone());
	}

}
