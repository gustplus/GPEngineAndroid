package com.gust.action;

import com.gust.common.game_util.GPLogger;
import com.gust.scene2D.GPNode2D;

public class GPTintTo2D extends GPActionLast {
	private float[] color;

	public GPTintTo2D(float[] color, float totalTime) {
		this.color = color;
		this.totalTime = totalTime;
		this.pastTime = 0;
	}

	@Override
	public void initWithComponent(GPNode2D component) {
		// TODO Auto-generated method stub
		super.initWithComponent(component);
	}

	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		if (isRunning) {
			float time = totalTime - pastTime;
			pastTime += deltaTime;
			if (time > 0) {
				component.changeToColor(color, pastTime / totalTime);
			} else {
				GPLogger.log("", "dasdasd");
				isRunning = false;
			}
		}
	}

	@Override
	public GPAction clone() {
		// TODO Auto-generated method stub
		return new GPTintTo2D(color, totalTime);
	}

	@Override
	public boolean isDone() {
		boolean isDone = super.isDone();
		if (isDone) {
			isRunning = false;
			component.setFunctionID(0);
		}
		return isDone;
	}
}