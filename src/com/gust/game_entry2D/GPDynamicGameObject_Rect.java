package com.gust.game_entry2D;

import com.gust.common.math.GPVector2f;
import com.gust.physics2D.Bound_Rectangle;
/**
 * ����2D��Ϸ�ĳ���������壬����λ�ã��߽�,�ٶȣ����ٶ����ԣ�����������ģ��
 * @author gustplus
 *
 */
public class GPDynamicGameObject_Rect extends GPGameObject_Rect{
	public GPDynamicGameObject_Rect(Bound_Rectangle rect, GPVector2f pos)
	{
		super(rect, pos);
		velocity = new GPVector2f();
		accel = new GPVector2f();
		bound.setStatic(false);
	}
	
	public GPVector2f velocity;
	public GPVector2f accel;

	public void update(float deltaTime){
		this.velocity.addTo(accel.mul(deltaTime));
		this.position.addTo(velocity.mul(deltaTime));
		this.bound.lowerLeft.addTo(velocity.mul(deltaTime));
	}
}
