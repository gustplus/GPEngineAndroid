package com.gust.physics2D;

import com.gust.common.math.GPVector2f;

/**
 * 用于表示圆形边界范围
 * @author gustplus
 *
 */
public class Bound_Circle extends Bound{
	public GPVector2f center;
	public float radius;
	
	/**
	 * 
	 * @param x该边界的中心点的X轴坐标
	 * @param y该边界的中心点的Y轴坐标
	 * @param radius该边界的半径
	 */
	public Bound_Circle(float x, float y,float radius){
		center = new GPVector2f(x, y);
		this.radius = radius;
		type = TYPE_CIRCLE;
	}
	
	/**
	 * 判断该点是否在范围内
	 * @param point 二维向量表示的点的位置
	 */
	public boolean isPointIn(GPVector2f point)
	{
		// TODO Auto-generated method stub
		return center.distSquared(point) < radius
				* radius;
	}
}
