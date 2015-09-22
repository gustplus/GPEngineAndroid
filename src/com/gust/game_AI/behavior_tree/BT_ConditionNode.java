package com.gust.game_AI.behavior_tree;


public abstract class BT_ConditionNode extends BT_Node {

	public abstract boolean condition();

	@Override
	public boolean update(float deltaTime) {
		// TODO Auto-generated method stub
//		if (tag != null)
//			GPLogger.log("node", tag);
		return condition();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
