package com.gust.action;

import com.gust.scene2D.GPNode2D;

public class GPActionSqueue extends GPAction2D {
	private GPAction2D[] actions;
	private int curIndex;
	private int num;

	public GPActionSqueue(GPAction2D... actions) {
		// TODO Auto-generated constructor stub
		this.actions = actions;
		num = actions.length;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		this.curIndex = 0;
		this.actions[0].start();
	}

	@Override
	public void initWithComponent(GPNode2D component) {
		// TODO Auto-generated method stub
		super.initWithComponent(component);
		actions[curIndex].initWithComponent(component);
	}

	public void update(float deltaTime) {
		if (!isDone() && curIndex < num && isRunning) {
			if (actions[curIndex].isDone() && curIndex < (num - 1)) {
				actions[curIndex].reset();
				++curIndex;
				actions[curIndex].reset();
				actions[curIndex].initWithComponent(component);
				actions[curIndex].start();
			}
			actions[curIndex].update(deltaTime);
		}
	}

	public boolean isDone() {
		// TODO Auto-generated method stub
		return (curIndex == (num - 1) && actions[num - 1].isDone());
	}

	public void reset() {
		// TODO Auto-generated method stub
		curIndex = 0;
		actions[0].reset();
	}
	
	public int getCurrentIndex(){
		return curIndex;
	}

	@Override
	public GPAction clone() {
		// TODO Auto-generated method stub
		return new GPActionSqueue(actions.clone());
	}

}
