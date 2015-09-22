package com.gust.physics2D;

import com.gust.common.game_util.GPLogger;
import com.gust.common.math.GPConstants;
import com.gust.common.math.GPVector2f;
import com.gust.game_entry2D.GPGameObject_Rect;

/**
 * 二维的碰撞检测系统
 * 
 * @author gustplus
 * 
 */
public class Collision_System2D {
	public static boolean isCollideCircles(Bound_Circle a, Bound_Circle b) {
		float radiusdist = a.radius + b.radius;
		return a.center.distSquared(b.center) <= radiusdist * radiusdist;
	}

	public static boolean isCollideCircle_Rectangle(Bound_Circle a,
			Bound_Rectangle b) {
		float nearestX = a.center.x;
		float nearestY = a.center.y;

		if (a.center.x < b.lowerLeft.x)
			nearestX = b.lowerLeft.x;
		else if (a.center.x > b.lowerLeft.x + b.width)
			nearestX = b.lowerLeft.x + b.width;

		if (a.center.y < b.lowerLeft.y)
			nearestY = b.lowerLeft.y;
		else if (a.center.y > b.lowerLeft.y + b.height)
			nearestY = b.lowerLeft.x + b.height;

		return a.center.distSquared(nearestX, nearestY) <= (a.radius * a.radius);
	}

	public static boolean isCollideRectangles(Bound_Rectangle a,
			Bound_Rectangle b) {
		//缩小一个包围体的范围以避免精度造成的错误
		if (a.lowerLeft.x  + 1 < b.lowerLeft.x + b.width
				&& a.lowerLeft.x + a.width -1 > b.lowerLeft.x
				&& a.lowerLeft.y  + 1 < b.lowerLeft.y + b.height
				&& a.lowerLeft.y + a.height - 1 > b.lowerLeft.y)
			return true;
		else
			return false;
	}

	public static boolean pointInRectangle(Bound_Rectangle rectangle,
			GPVector2f position) {
		return rectangle.isPointIn(position);
	}

	public static boolean pointInRectangle(Bound_Rectangle rectangle, float x,
			float y) {
		return rectangle.isPointIn(new GPVector2f(x, y));
	}

	public static boolean pointInCircle(Bound_Circle circle, GPVector2f poition) {
		return circle.isPointIn(poition);
	}

	public static boolean pointInCircle(Bound_Circle circle, float x, float y) {
		return circle.isPointIn(new GPVector2f(x, y));
	}

	public static boolean fixCollisionBack(GPVector2f direction,
			GPGameObject_Rect obj1, GPGameObject_Rect obj2) {
		Bound_Rectangle rect1 = obj1.bound;
		Bound_Rectangle rect2 = obj2.bound;
		if (!isCollideRectangles(rect1, rect2)) {
			return false;
		}

		if (rect1.isStatic() && rect2.isStatic()) {
			return false;
		}

		GPVector2f fixVector;
		boolean fromLow = true;
		if (direction.x < 0) {
			fromLow = false;
		}
		float x = calDistance(rect1.lowerLeft.x, rect1.lowerLeft.x
				+ rect1.width, rect2.lowerLeft.x, rect2.lowerLeft.x
				+ rect2.width, fromLow);
		fromLow = true;
		if (direction.y < 0) {
			fromLow = false;
		}
		float y = calDistance(rect1.lowerLeft.y, rect1.lowerLeft.y
				+ rect1.height, rect2.lowerLeft.y, rect2.lowerLeft.y
				+ rect2.height, fromLow);
		fixVector = new GPVector2f(x, y);
		GPVector2f reDir = direction.mul(-1);
		fixVector = getFixVector(reDir, fixVector);

		GPVector2f move;
		if (rect2.isStatic()) {
			move = fixVector.mul(-1f);
			rect1.lowerLeft.addTo(move);
			obj1.move(move);
		} else if (rect1.isStatic()) {
			rect2.lowerLeft.addTo(fixVector);
			obj2.move(fixVector);
		} else {
			move = fixVector.mul(-0.5f);
			rect1.lowerLeft.addTo(move);
			obj1.move(move);
			move.mulWith(-1);
			rect2.lowerLeft.addTo(move);
			obj2.move(move);
		}

		return true;
	}

	public static boolean fixCollision(GPVector2f direction,
			GPGameObject_Rect obj1, GPGameObject_Rect obj2) {
		if (direction.x < GPConstants.almostZero
				&& direction.x > -GPConstants.almostZero && direction.y < GPConstants.almostZero
				&& direction.y > -GPConstants.almostZero) {
			return false;
		}
		
		Bound_Rectangle rect1 = obj1.bound;
		Bound_Rectangle rect2 = obj2.bound;
		if (!isCollideRectangles(rect1, rect2)) {
			return false;
		}

		if (rect1.isStatic() && rect2.isStatic()) {
			return false;
		}

		GPVector2f fixVector;
		boolean fromLow = true;
		if (direction.x < 0) {
			fromLow = false;
		}
		float x = calDistance(rect1.lowerLeft.x, rect1.lowerLeft.x
				+ rect1.width, rect2.lowerLeft.x, rect2.lowerLeft.x
				+ rect2.width, fromLow);

		fromLow = true;
		if (direction.y < 0) {
			fromLow = false;
		}
		float y = calDistance(rect1.lowerLeft.y, rect1.lowerLeft.y
				+ rect1.height, rect2.lowerLeft.y, rect2.lowerLeft.y
				+ rect2.height, fromLow);
		fixVector = new GPVector2f(x, y);
		GPVector2f reDir = direction.mul(-1);
		fixVector = getFixVector(reDir, fixVector);

//		fixVector.x = (float) ((int) fixVector.x);
//		if (fixVector.x > 0) {
//			fixVector.x += 1;
//		}
//		fixVector.y = (float) ((int) fixVector.y);
//		if (fixVector.y > 0) {
//			fixVector.y += 1;
//		}

		GPVector2f move;
		if (rect2.isStatic()) {
			move = fixVector.mul(-1f);
			if (Math.abs(fixVector.x) < Math.abs(x)) {
				move.x = 0;
			} else {
				move.y = 0;
			}
			obj1.move(move);
		} else if (rect1.isStatic()) {
			obj2.move(fixVector);
		} else {
			move = fixVector.mul(-0.5f);
			obj1.move(move);
			move.mulWith(-1);
			obj2.move(move);
		}
		return true;
	}

	private static GPVector2f getFixVector(GPVector2f direction, GPVector2f fix) {
		GPVector2f dir = direction.clone().mul(-1);
		GPVector2f fixDir = new GPVector2f(1, 1);
		float width;
		float height;
		if (dir.x < 0) {
			fixDir.x = -1;
			dir.x = -dir.x;
		}
		if (dir.y < 0) {
			fixDir.y = -1;
			dir.y = -dir.y;
		}
		height = fix.x / dir.x * dir.y;
		if (height <= fix.y) {
			width = fix.x;
			return fixDir.mul(new GPVector2f(width, height));
		}
		width = fix.y / dir.y * dir.x;
		if (width <= fix.x) {
			height = fix.y;
			return fixDir.mul(new GPVector2f(width, height));
		}
		return fixDir.mul(fix);
	}

	private static float calDistance(float s0, float e0, float s1, float e1,
			boolean fromLow) {
		if (s0 < s1) {
			if (e0 < e1) {
				if (fromLow) {
					return e0 - s1;
				} else {
					return e1 - s0;
				}
			} else {
				if (fromLow) {
					return e1 - s0;
				} else {
					return e0 - s1;
				}
			}
		} else {
			if (e0 < e1) {
				if (fromLow) {
					return e1 - s0;
				} else {
					return e0 - s1;
				}
			} else {
				if (fromLow) {
					return e0 - s1;
				} else {
					return e1 - s0;
				}
			}
		}
	}

	public static boolean fixCollisionPointLine(GPVector2f point, GPLine line) {
		float dist = line.distanceWithPiont(point);
		if (dist >= 0) {
			return false;
		}
		dist = -dist;
		GPVector2f normal = line.getNormal();
		point.addTo(normal.mul(dist));
		return true;
	}
	
	public static boolean isCollisionPointLine(GPVector2f point, GPLine line) {
		float dist = line.distanceWithPiont(point);
		if (dist >= 0) {
			return false;
		}
		return true;
	}
}
