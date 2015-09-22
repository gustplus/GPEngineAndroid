package com.gust.common.game_util;

import com.gust.common.math.GPVector3f;

public interface GPWorld {
	public void update(float deltaTime);
	
	public void present();
	
	public GPVector3f[] getPositions(int index);
}
