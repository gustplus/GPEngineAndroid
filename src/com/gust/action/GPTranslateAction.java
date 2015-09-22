package com.gust.action;

public class GPTranslateAction extends GPActionLast {

	private float translation;
	private float deltaChange;

	public void set(float translation, float time){
		this.translation = translation;
		this.totalTime = time;
		this.pastTime = 0;
	}
	
	public void start(){
		super.start();
		deltaChange = 0;
//		this.pastTime = 0;
	}

	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		float retainTime = totalTime - pastTime;
		if(isDone())
			isRunning = false;
		if (!isDone()&&isRunning) {
			if (retainTime > deltaTime) {
				deltaChange = deltaTime * translation / totalTime;
				pastTime += deltaTime;
			} else {
				deltaChange = retainTime * translation / totalTime;
				pastTime += retainTime;
			}
		}
	}

	public float getTranslate() {
		return deltaChange;
	}

	@Override
	public GPAction clone() {
		// TODO Auto-generated method stub
		GPTranslateAction trans = new GPTranslateAction();
		trans.set(translation, totalTime);
		return trans;
	}
	
}
