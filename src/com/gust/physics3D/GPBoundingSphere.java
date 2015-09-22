package com.gust.physics3D;

import com.gust.common.math.GPVector3f;

public class GPBoundingSphere implements GPBoundingShape {
	public GPVector3f position;
	public float radius;

	public GPBoundingSphere(GPVector3f position, float radius) {
		// TODO Auto-generated constructor stub
		this.position = position;
		this.radius = radius;
	}
	
	public GPBoundingSphere(GPAABB aabb){
		position = aabb.getLeft_Up_Front();
		position.addTo(aabb.getRight_Bottom_Behind());
		position.mulWith(0.5f);
		
		radius = position.dist(aabb.getLeft_Up_Front());
	}

	public boolean checkCollisionWithPoint(GPVector3f point) {
		// TODO Auto-generated method stub
		float len = position.sub(point).len();
		return len <= radius;
	}

}
