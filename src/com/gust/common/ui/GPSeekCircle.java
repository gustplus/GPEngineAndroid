package com.gust.common.ui;

import com.gust.common.math.GPConstants;
import com.gust.common.math.GPVector2f;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.scene2D.GPEvent;
import com.gust.scene2D.GPSprite;
import com.gust.system.GPInput.GPTouch;

public class GPSeekCircle extends GPSprite {
	private float rate;
	private float angle;
	private float firstAngle;
	private float secendAngle;

	public GPSeekCircle(GPTextureRegion presentation, float x, float y, GPShader shader) {
		super(presentation, x, y, shader);
		this.rate = 0;
		angle = 0;
		selectMode = true;
	}

	public void setRate(float rate) {
		this.rate = rate;
		this.angle = 360 * rate;
	}

	public float getRate() {
		return rate;
	}
	
	@Override
	public boolean onTouchDown(GPTouch touch, float deltaTime) {
		if (!visible) {
			return false;
		}
		if (selectMode) {
			GPVector2f point = new GPVector2f(touch.x, touch.y).sub(getPosition());
			this.firstAngle = point.angle();
			this.secendAngle = firstAngle;
			return true;
		}
		return false;
	}

	@Override
	public void onTouchDrag(GPTouch touch, float deltaTime) {
		if (!visible) {
			return;
		}
		if (selectMode) {
			GPVector2f point = new GPVector2f(touch.x, touch.y).sub(getPosition());
			this.secendAngle = point.angle();
			getRateByAngle(secendAngle - firstAngle);
			this.firstAngle = secendAngle;
			dispatchEvent(GPEvent.CHANGE);
			
			this.setRotation(angle);
		}
	}

	@Override
	public void onTouchUp(GPTouch touch, float deltaTime) {
		if (!visible) {
			return;
		}
		if (selectMode) {
			GPVector2f point = new GPVector2f(touch.x, touch.y).sub(getPosition());
			this.secendAngle = point.angle();
			getRateByAngle(firstAngle - secendAngle);
			firstAngle = secendAngle = 0;
			dispatchEvent(GPEvent.CHANGE);
			
			this.setRotation(angle);
		}
		return;
	}

	private float getRateByAngle(float angle) {
		this.angle += angle;
		this.angle = GPConstants.translateAngleInround(this.angle);
		rate = this.angle / 360;
		return rate;
	}
}
