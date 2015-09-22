package com.gust.game_entry2D;

import com.gust.common.math.GPVector2f;
import com.gust.physics2D.Bound_Circle;

/**
 * 用于2D游戏中静态的抽象圆形物体，具有位置，边界属性，常用于物理模拟
 * @author gustplus
 *
 */
public class GPGameObject_Circle {
	public GPVector2f position;
	public Bound_Circle bound;

	public GPGameObject_Circle(Bound_Circle circle, GPVector2f pos)
	{
		position = pos;
		bound = circle;
		bound.setStatic(true);
	}

	public void setPosition(GPVector2f newPosition)
	{
		this.position.set(newPosition);
		this.bound.center.set(newPosition);
	}
	
	public void move(GPVector2f movement){
		this.bound.center.addTo(movement);
		this.position.addTo(movement);
	}
}
