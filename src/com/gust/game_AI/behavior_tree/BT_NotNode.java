package com.gust.game_AI.behavior_tree;


public class BT_NotNode extends BT_DecoratorNode {

	@Override
	public boolean update(float deltaTime) {
		// TODO Auto-generated method stub
//		GPLogger.log("node", tag);
		return !child.update(deltaTime);
	}

}
