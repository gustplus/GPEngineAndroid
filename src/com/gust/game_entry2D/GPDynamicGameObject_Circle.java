package com.gust.game_entry2D;

import com.gust.common.math.GPVector2f;
import com.gust.physics2D.Bound_Circle;

/**
 * 用于2D游戏中动态的抽象圆形物体，具有位置，边界,速度，加速度属性，常用于物理模拟
 * @author gustplus
 *
 */
public class GPDynamicGameObject_Circle extends GPGameObject_Circle{
	public GPDynamicGameObject_Circle(Bound_Circle circle, GPVector2f pos)
	{
		super(circle, pos);
		velocity = new GPVector2f();
		accel = new GPVector2f();
		circle.setStatic(false);
	}
	
	public GPVector2f velocity;
	public GPVector2f accel;
	
	public void update(float deltaTime){
		this.velocity.addTo(accel.mul(deltaTime));
		this.position.addTo(velocity.mul(deltaTime));
	}
}
