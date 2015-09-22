package com.gust.action;

import com.gust.common.math.GPVector2f;
import com.gust.scene2D.GPNode2D;

public class GPActionMoveBy2D extends GPActionLast {
	private GPVector2f moveBy;
	private GPVector2f detlaMove;

	public GPActionMoveBy2D(GPVector2f moveBy, float totalTime) {
		this.moveBy = moveBy;
		this.totalTime = totalTime;
		this.pastTime = 0;
		
	}

	@Override
	public void initWithComponent(GPNode2D component) {
		super.initWithComponent(component);
		detlaMove = moveBy.mul(1.0f / totalTime);
	}

	public void update(float deltaTime) {
		if (!isDone() && isRunning) {
			float time = totalTime - pastTime;
			if (time > 0) {
				if (time > deltaTime)
					this.component.setPosition(this.component.getPosition()
							.add(this.detlaMove.mul(deltaTime)));
				else
					this.component.setPosition(this.component.getPosition()
							.add(this.detlaMove.mul(time)));
				pastTime += deltaTime;
			}
		}
	}

	@Override
	public GPAction clone() {
		return new GPActionMoveBy2D(this.moveBy, this.totalTime);
	}
}
