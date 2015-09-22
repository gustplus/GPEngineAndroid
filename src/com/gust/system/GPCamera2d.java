package com.gust.system;

import com.gust.common.math.GPVector2f;

public interface GPCamera2d {
	public void setupViewAndMatrix();

	public void moveCamera(GPVector2f to);

	public void moveCamera(float x, float y);

	public void setZoom(float zoom);

	public void touchInWorld(GPVector2f touchPoint);
	
	public float getfrusumWidth();
}
