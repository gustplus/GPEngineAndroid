package com.gust.physics3D;

import com.gust.common.math.GPVector3f;

public interface GPBoundingShape {
	public boolean checkCollisionWithPoint(GPVector3f point);
}
