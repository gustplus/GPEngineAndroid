package com.gust.physics2D;

import com.gust.common.math.GPVector2f;

public abstract class Bound {
	protected int type;
	protected boolean isStatic = false;
	
	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public final static int TYPE_RECT = 0;
	public final static int TYPE_POLYGON = 1;
	public final static int TYPE_CIRCLE = 2;
	
	public int getType() {
		return type;
	}

	public abstract boolean isPointIn(GPVector2f point);
}
