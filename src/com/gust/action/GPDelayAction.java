package com.gust.action;

import com.gust.scene2D.GPNode2D;

public class GPDelayAction extends GPActionLast {

	public GPDelayAction(float delayTime) {
		// TODO Auto-generated constructor stub
		pastTime = 0;
		totalTime = delayTime;
	}

	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		if (isRunning)
			if (!isDone())
				pastTime += deltaTime;
	}

	public boolean isDone() {
		// TODO Auto-generated method stub
		return pastTime >= totalTime;
	}

	public void reset() {
		// TODO Auto-generated method stub
		pastTime = 0;
	}

	@Override
	public void initWithComponent(GPNode2D component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GPAction clone() {
		// TODO Auto-generated method stub
		GPDelayAction delay = new GPDelayAction(totalTime);
		return delay;
	}

}
