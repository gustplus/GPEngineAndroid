package com.gust.common.ui;

import java.util.ArrayList;

import com.gust.common.math.GPVector2f;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.scene2D.GPEvent;
import com.gust.scene2D.GPSprite;
import com.gust.system.GPInput.GPTouch;

public class GPSeekBar extends GPSprite {
	private GPSprite seekPoint;
	private float rate;
	private GPVector2f pointPosition;
	private int type;
	private static final int TYPE_HORIZONTAL = 0; // Ë®Æ½
	private static final int TYPE_VERTICAL = 1; // ´¹Ö±

	public GPSeekBar(GPTextureRegion backgound, GPTextureRegion seekPoint,
			float x, float y, GPShader shader) {
		super(backgound, x, y, shader);
		this.selectMode = true;
		this.rate = 0;
		if (backgound.getWidth() >= backgound.getHeight()) {
			type = TYPE_HORIZONTAL;
			this.pointPosition = new GPVector2f(0, getContentBound().height / 2);
			this.seekPoint = new GPSprite(seekPoint, pointPosition.x,
					pointPosition.y, shader);
		} else {
			type = TYPE_VERTICAL;
			this.pointPosition = new GPVector2f(getContentBound().width / 2,
					getContentBound().height);
			this.seekPoint = new GPSprite(seekPoint, pointPosition.x,
					pointPosition.y, shader);
		}

		// pointHalfWidth = this.seekPoint.getContentBound().width;

		this.swallow = true;

		addChild(this.seekPoint);
	}

	public void setRate(float rate) {
		this.rate = rate;
		if (type == TYPE_HORIZONTAL) {
			this.pointPosition.x = this.getContentBound().width * rate;
		} else {
			this.pointPosition.y = this.getContentBound().height * rate;
		}
		seekPoint.setPosition(pointPosition);
		dispatchEvent(GPEvent.CHANGE);
	}

	public float getRate() {
		return rate;
	}

	@Override
	public boolean onTouchDown(GPTouch touch, float deltaTime) {
		if (selectMode) {
			if (type == TYPE_HORIZONTAL) {
				this.pointPosition.x = touch.x - getContentBound().lowerLeft.x;
				setRate(getRateByPointPosition(this.pointPosition.x));
			} else {
				this.pointPosition.y = touch.y - getContentBound().lowerLeft.y;
				setRate(getRateByPointPosition(this.pointPosition.y));
			}
			return true;
		}
		return false;
	}

	@Override
	public void onTouchDrag(GPTouch touch, float deltaTime) {
		if (selectMode) {
			if (type == TYPE_HORIZONTAL) {
				this.pointPosition.x = touch.x - getContentBound().lowerLeft.x;
				if (this.pointPosition.x > this.getContentBound().width) {
					this.pointPosition.x = this.getContentBound().width;
				}
				if (this.pointPosition.x < 0) {
					this.pointPosition.x = 0;
				}
				setRate(getRateByPointPosition(this.pointPosition.x));
			} else {
				this.pointPosition.y = touch.y - getContentBound().lowerLeft.y;
				if (this.pointPosition.y > this.getContentBound().height) {
					this.pointPosition.y = this.getContentBound().height;
				}
				if (this.pointPosition.y < 0) {
					this.pointPosition.y = 0;
				}
				setRate(getRateByPointPosition(this.pointPosition.y));
			}
			dispatchEvent(GPEvent.CHANGE);
		}
	}

	@Override
	public void onTouchUp(GPTouch touch, float deltaTime) {
		if (selectMode) {
			return;
		}
		return;
	}

	@Override
	public void onTouchesDown(ArrayList<GPTouch> touches, float deltaTime) {
	}

	@Override
	public void onTouchesDrag(ArrayList<GPTouch> touches, float deltaTime) {
	}

	@Override
	public void onToucheshUp(ArrayList<GPTouch> touches, float deltaTime) {
	}

	private float getRateByPointPosition(float point) {
		float max;
		if (type == TYPE_HORIZONTAL) {
			max = this.getContentBound().width;
		} else {
			max = this.getContentBound().height;
		}
		if (point > max) {
			point = max;
		}
		return point / max;
	}
}
