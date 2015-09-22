package com.gust.game_AI.FSM;

import com.gust.game_entry3D.GPGameObject3D_bullet;

public interface AIControl {
	public void update();
	public void updateDatas();
	public void setTarget(GPGameObject3D_bullet target);
}
