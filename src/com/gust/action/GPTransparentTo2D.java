package com.gust.action;

import com.gust.scene2D.GPNode2D;

public class GPTransparentTo2D extends GPActionLast {
	private float translation;
	private float destination;
	private float transparent;

	public GPTransparentTo2D(float translation, float time) {
		super();
		this.totalTime = time;
		this.destination = translation;
		if(this.destination > 1){
			this.destination = 1;
		}
		if(this.destination < 0){
			this.destination = 0;
		}
		this.pastTime = 0;
		this.transparent = 1;
	}

	public void set(float translation, float time) {
		this.destination = translation;
		if(this.destination > 1){
			this.destination = 1;
		}
		if(this.destination < 0){
			this.destination = 0;
		}
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
		this.translation = this.destination - this.component.getTransparent();
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
		GPTransparentTo2D trans = new GPTransparentTo2D(translation,
				totalTime);
		return trans;
	}

}
