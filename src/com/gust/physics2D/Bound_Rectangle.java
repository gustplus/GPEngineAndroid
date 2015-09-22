package com.gust.physics2D;

import java.util.ArrayList;

import com.gust.common.math.GPVector2f;

/**
 * 用于表示矩形边界范围
 * 
 * @author gustplus
 * 
 */
public class Bound_Rectangle extends Bound {
	public GPVector2f lowerLeft;
	public float width;
	public float height;

	/**
	 * 
	 * @param lowX该边界的左下角X轴坐标
	 * @param lowY该边界的左下角Y轴坐标
	 * @param width该边界的宽度
	 * @param height该边界的高度
	 */
	public Bound_Rectangle(float lowX, float lowY, float width, float height) {
		lowerLeft = new GPVector2f(lowX, lowY);
		this.width = width;
		this.height = height;
		this.type = TYPE_RECT;
	}

	public void setWidthHeight(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * 判断该点是否在范围内
	 * 
	 * @param point
	 *            二维向量表示的点的位置
	 */
	public boolean isPointIn(GPVector2f point) {
		// TODO Auto-generated method stub
		return lowerLeft.x <= point.x && lowerLeft.x + width >= point.x
				&& lowerLeft.y <= point.y && lowerLeft.y + height >= point.y;
	}

	public boolean isPointIn(GPVector2f point, float wPadding, float hPadding) {
		// TODO Auto-generated method stub
		return lowerLeft.x + wPadding <= point.x
				&& lowerLeft.x + width - wPadding >= point.x
				&& lowerLeft.y + hPadding <= point.y
				&& lowerLeft.y + height - hPadding >= point.y;
	}

	public boolean isRectIn(Bound_Rectangle rect) {
		// TODO Auto-generated method stub
		return lowerLeft.x <= rect.lowerLeft.x
				&& lowerLeft.x + width >= rect.lowerLeft.x + rect.width
				&& lowerLeft.y <= rect.lowerLeft.y
				&& lowerLeft.y + height >= rect.lowerLeft.y + rect.height;
	}

	public void include(Bound_Rectangle other) {
		if (other.lowerLeft.x < lowerLeft.x) {
			width += lowerLeft.x - other.lowerLeft.x;
			lowerLeft.x = other.lowerLeft.x;
		}
		if (other.lowerLeft.y < lowerLeft.y) {
			height += lowerLeft.y - other.lowerLeft.y;
			lowerLeft.y = other.lowerLeft.y;
		}

		float Right = other.lowerLeft.x + other.width;
		float Top = other.lowerLeft.y + other.height;
		if (Right > lowerLeft.x + width) {
			width = Right - lowerLeft.x;
		}
		if (Top > lowerLeft.y + height) {
			height = Top - lowerLeft.y;
		}
	}

	public void createWithPoints(ArrayList<GPVector2f> points) {
		this.width = 0;
		this.height = 0;
		int size = points.size();
		for (int i = 0; i < size; ++i) {
			GPVector2f point = points.get(i);
			float x = point.x;
			float y = point.y;
			if (i == 0) {
				lowerLeft.x = x;
				lowerLeft.y = y;
				continue;
			}
			if (x > lowerLeft.x + width) {
				width = x - lowerLeft.x;
			}
			if (y > lowerLeft.y + height) {
				height = y - lowerLeft.y;
			}
			if (x < lowerLeft.x) {
				width += lowerLeft.x - x;
				lowerLeft.x = x;
			}
			if (y < lowerLeft.y) {
				height += lowerLeft.y - y;
				lowerLeft.y = y;
			}
		}
	}

	public GPVector2f getCenterPoint() {
		return lowerLeft.add(width / 2, height / 2);
	}

	public void getClosestPointOnRect(GPVector2f point, GPVector2f result) {
		float x = point.x;
		x = Math.max(x, lowerLeft.x);
		x = Math.min(x, lowerLeft.x + width);
		float y = point.y;
		y = Math.max(y, lowerLeft.y);
		y = Math.min(y, lowerLeft.y + height);
		result.set(x, y);
	}

	public Bound_Rectangle getIntersection(Bound_Rectangle rect) {
		if (Collision_System2D.isCollideRectangles(rect, this)) {
			return new Bound_Rectangle(lowerLeft.x , lowerLeft.y, 0, 0);
		}
		GPVector2f tmpLowLeft = lowerLeft.clone();
		tmpLowLeft.x = rect.lowerLeft.x > tmpLowLeft.x ? rect.lowerLeft.x
				: lowerLeft.x;
		tmpLowLeft.y = rect.lowerLeft.y > tmpLowLeft.y ? rect.lowerLeft.y
				: lowerLeft.y;
		float tmpWidth = rect.lowerLeft.x + rect.width > lowerLeft.x + width ? rect.lowerLeft.x + rect.width - tmpLowLeft.x
				: lowerLeft.x + width - tmpLowLeft.x;
		float tmpHeight = rect.lowerLeft.y + rect.height > lowerLeft.y + height ? rect.lowerLeft.y + rect.height - tmpLowLeft.y
				: lowerLeft.y + height - tmpLowLeft.y;
		return new Bound_Rectangle(tmpLowLeft.x, tmpLowLeft.y, tmpWidth, tmpHeight);
	}
	
	public void intersectWith(Bound_Rectangle rect) {
		if (!Collision_System2D.isCollideRectangles(rect, this)) {
			width = 0;
			height = 0;
			return;
		}
		GPVector2f tmpLowLeft = lowerLeft.clone();
		lowerLeft.x = rect.lowerLeft.x > tmpLowLeft.x ? rect.lowerLeft.x
				: tmpLowLeft.x;
		lowerLeft.y = rect.lowerLeft.y > tmpLowLeft.y ? rect.lowerLeft.y
				: tmpLowLeft.y;
		width = rect.lowerLeft.x + rect.width < tmpLowLeft.x + width ? rect.lowerLeft.x + rect.width - lowerLeft.x
				: tmpLowLeft.x + width - lowerLeft.x;
		height = rect.lowerLeft.y + rect.height < tmpLowLeft.y + height ? rect.lowerLeft.y + rect.height - lowerLeft.y
				: tmpLowLeft.y + height - lowerLeft.y;
	}

	public Bound_Rectangle clone() {
		return new Bound_Rectangle(lowerLeft.x, lowerLeft.y, width, height);
	}

	public float getMaxX() {
		return lowerLeft.x + width;
	}

	public float getMaxY() {
		return lowerLeft.y + height;
	}
}
