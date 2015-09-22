package com.gust.action;

import com.gust.common.math.GPVector2f;

public class GPActionMoveTo2D extends GPActionLast {
	private GPVector2f moveTo;

	public GPActionMoveTo2D(GPVector2f moveTo, float totalTime) {
		this.moveTo = moveTo;
		this.totalTime = totalTime;
		this.pastTime = 0;
	}

	public void update(float deltaTime) {
		if (isDone() && isRunning) {
			float time = totalTime - pastTime;
			if (time > 0) {
				if (time > deltaTime) {
					this.component.setPosition(moveTo);
				} else {
					GPVector2f detlaMove = this.component.getPosition()
							.sub(moveTo).mul(deltaTime / time);
					this.component.setPosition(this.component.getPosition()
							.add(detlaMove));
					pastTime += deltaTime;
				}
			}
		}
	}

	@Override
	public GPAction clone() {
		// TODO Auto-generated method stub
		return new GPActionMoveTo2D(moveTo, totalTime);
	}
}