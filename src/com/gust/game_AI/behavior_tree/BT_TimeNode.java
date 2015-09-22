package com.gust.game_AI.behavior_tree;

public class BT_TimeNode extends BT_DecoratorNode {

	private float limitTime;
	private float pastTime;

	public BT_TimeNode(float limitTime) {
		// TODO Auto-generated constructor stub
		this.limitTime = limitTime;
		this.pastTime = 0;
	}

	@Override
	public boolean update(float deltaTime) {
		// TODO Auto-generated method stub
		boolean gone;
		// GPLogger.log("node", tag);
		pastTime += deltaTime;
		if (pastTime <= limitTime) {
			gone = false;
		} else {
			gone = true;
		}
		if (gone) {
			if (child != null)
				child.update(deltaTime);
		}
		return gone;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		pastTime = 0;
	}

}
