package com.gust.game_AI.behavior_tree;


public class BT_AndNode extends BT_CompositeNode {

	@Override
	public boolean update(float deltaTime) {
		// TODO Auto-generated method stub
//		GPLogger.log("node", tag);
		if (children != null&&children.size()>0) {
			for (BT_Node child : children) {
				if(!child.update(deltaTime)){
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		super.reset();
	}
}
