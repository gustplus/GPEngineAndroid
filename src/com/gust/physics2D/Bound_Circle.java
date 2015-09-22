package com.gust.physics2D;

import com.gust.common.math.GPVector2f;

/**
 * ���ڱ�ʾԲ�α߽緶Χ
 * @author gustplus
 *
 */
public class Bound_Circle extends Bound{
	public GPVector2f center;
	public float radius;
	
	/**
	 * 
	 * @param x�ñ߽�����ĵ��X������
	 * @param y�ñ߽�����ĵ��Y������
	 * @param radius�ñ߽�İ뾶
	 */
	public Bound_Circle(float x, float y,float radius){
		center = new GPVector2f(x, y);
		this.radius = radius;
		type = TYPE_CIRCLE;
	}
	
	/**
	 * �жϸõ��Ƿ��ڷ�Χ��
	 * @param point ��ά������ʾ�ĵ��λ��
	 */
	public boolean isPointIn(GPVector2f point)
	{
		// TODO Auto-generated method stub
		return center.distSquared(point) < radius
				* radius;
	}
}
