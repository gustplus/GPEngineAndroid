package com.gust.physics3D;

import com.gust.common.math.GPVector3f;

public class Collision_System3D {

	public void pointInCuboid(GPVector3f point, GPAABB cuboid,
			float[] invertMatrix) {

	}

	public static boolean fixCollideCircles(GPBoundingSphere ball0,
			GPBoundingSphere ball1) {
		GPVector3f direction = ball0.position.sub(ball1.position);
		float len = direction.len();
		if (len > ball0.radius + ball1.radius) {
			return false;
		} else {
			direction = direction.normalize();
			len = (ball0.radius + ball1.radius) - len;
			ball0.position.addTo(direction.mul(len / 2));
			ball1.position.addTo(direction.mul(-len / 2));
			return true;
		}
	}

	public static boolean checkCollideCircles(GPBoundingSphere ball0,
			GPBoundingSphere ball1) {
		GPVector3f direction = ball0.position.sub(ball1.position);
		float len = direction.len();
		if (len > ball0.radius + ball1.radius) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isCollideCirclesStatic(GPBoundingSphere staticBall,
			GPBoundingSphere ball1) {
		GPVector3f direction = staticBall.position.sub(ball1.position);
		float len = direction.len();
		if (len > staticBall.radius + ball1.radius) {
			return false;
		} else {
			direction = direction.normalize();
			len = (staticBall.radius + ball1.radius) - len;
			ball1.position.addTo(direction.mul(-len));
			return true;
		}
	}

	public static boolean CollideCirecleWithAABB(GPBoundingSphere a, GPAABB b,
			GPVector3f positionOffset, GPVector3f movement) {
		float nearestX = a.position.add(movement).x;
		float nearestY = a.position.add(movement).z;
		int x = 0;
		int y = 0;

		if (a.position.x < positionOffset.x + b.getLeft_Up_Front().x) {
			x = -1;
			nearestX = positionOffset.x + b.getLeft_Up_Front().x;
		} else if (a.position.x > b.getRight_Bottom_Behind().x) {
			nearestX = b.getRight_Bottom_Behind().x;
			x = 1;
		}
		if (a.position.z > positionOffset.z + b.getRight_Bottom_Behind().z) {
			nearestY = positionOffset.z + b.getRight_Bottom_Behind().z;
			y = 1;
		} else if (a.position.z < positionOffset.z + b.getLeft_Up_Front().z) {
			nearestY = positionOffset.z + b.getLeft_Up_Front().z;
			y = -1;
		}

		float distX = a.position.x - nearestX - x * a.radius;
		float distZ = a.position.z - nearestY - y * a.radius;
		boolean result = (distX * distX + distZ * distZ) <= (a.radius * a.radius);
		if (result) {
			movement.set(0, 0, 0);
			a.position.addTo(distX / 4, 0, distZ / 4);
		}
		return result;
	}

	public static boolean fixCireclesCollisionInSide(GPBoundingSphere small,
			GPBoundingSphere big) {
		GPVector3f direction = small.position.sub(big.position);
		float len = direction.len();
		float dist = big.radius - small.radius;
		if (len > dist) {
			small.position.addTo(direction.normalize().mul(dist - len));
			return true;
		} else {
			return false;
		}
	}
}
