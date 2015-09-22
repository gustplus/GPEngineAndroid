package com.gust.common.ui;

import com.gust.common.math.GPVector2f;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.scene2D.GPSprite;
import com.gust.scene2D.GPTouchEvent;
import com.gust.system.GPInput.GPTouch;

public class GPToggleButton extends GPSprite {
	private boolean isOn;
	private GPTextureRegion on;
	private GPTextureRegion off;

	public GPToggleButton(GPTextureRegion on, GPTextureRegion off, float x,
			float y, GPShader shader) {
		super(on, x, y, shader);
		isOn = true;
		this.on = on;
		this.off = off;
		this.selectMode = true;
	}

	public void click() {
		isOn = !isOn;
		setOn_Off(isOn);
	}

	public void drawself() {
		super.drawself();
	}

	public void setOn_Off(boolean isOn) {
		this.isOn = isOn;
		this.presentation = isOn ? on : off;
		setTextureRegion(presentation);
	}

	public boolean isOn() {
		return isOn;
	}

	public boolean onTouchDown(GPTouch touch, float deltaTime) {
		if (selectMode) {
			return true;
		}
		return false;
	}

	public void onTouchDrag(GPTouch touch, float deltaTime) {
	}

	public void onTouchUp(GPTouch touch, float deltaTime) {
		if (!visible) {
			return;
		}
		GPVector2f point = new GPVector2f(touch.x, touch.y);
		if (selectMode && contentBound.isPointIn(point)) {
			click();
			if (isOn) {
				dispatchEvent(GPTouchEvent.TURN_ON);
			} else {
				dispatchEvent(GPTouchEvent.TURN_OFF);
			}
		}
	}
}
