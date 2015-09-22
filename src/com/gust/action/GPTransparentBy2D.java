package com.gust.action;

import com.gust.scene2D.GPNode2D;

public class GPTransparentBy2D extends GPActionLast {
	private float translation;
	private float transparent;

	public GPTransparentBy2D(float translation, float time) {
		super();
		this.totalTime = time;
		this.translation = translation;
		this.pastTime = 0;
		this.transparent = 1;
	}

	public void set(float translation, float time) {
		this.translation = translation;
		this.totalTime = time;
		this.pastTime = 0;
		this.transparent = 1;
	}

	public void start() {
		super.start();
	}
	
	@Override
	public void initWithComponent(GPNode2D component) {
		super.initWithComponent(component);
		transparent = this.component.getTransparent();
	}

	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		float retainTime = totalTime - pastTime;
		if (!isDone() && isRunning) {
			if (retainTime > deltaTime) {
				transparent += deltaTime * translation / totalTime;
				pastTime += deltaTime;
			} else {
				transparent += retainTime * translation / totalTime;
				pastTime += retainTime;
			}
			
			component.setTransparent(transparent);
		}
	}

	@Override
	public GPAction clone() {
		// TODO Auto-generated method stub
		GPTransparentBy2D trans = new GPTransparentBy2D(translation,
				totalTime);
		return trans;
	}

}
