package com.gust.game_entry2D;

import com.gust.common.math.GPVector2f;
import com.gust.physics2D.Bound_Rectangle;

/**
 * 用于2D游戏中静态的抽象矩形物体，具有位置，边界属性，常用于物理模拟
 * @author gustplus
 *
 */
public class GPGameObject_Rect {
	public GPVector2f position;
	public Bound_Rectangle bound;
	public float width;
	public float height;
	
	public GPGameObject_Rect(Bound_Rectangle rect, GPVector2f pos)
	{
		// TODO Auto-generated constructor stub
		bound = rect;
		position = pos;
		bound.setStatic(true);
	}
	public void setBindPosition(GPVector2f newPosition){
		this.position.set(newPosition);
	}
	
	public void move(GPVector2f movement){
		this.position.addTo(movement);
		bound.lowerLeft.addTo(movement);
	}
}
