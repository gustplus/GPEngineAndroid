package com.gust.common.control;

import java.util.List;

import com.gust.common.math.GPVector2f;
import com.gust.system.GPInput.GPTouch;

public abstract class GPViewStick {
	
	protected GPVector2f viewDirection;

	protected float range = 1;
	protected int pointer = -1;
	protected GPVector2f firstPosition;
	protected GPVector2f secondPosition;
	
	public abstract GPVector2f controlView(List<GPTouch> touchEvents);
	
	public void setRange(float range){
		this.range = range;
	}
}
