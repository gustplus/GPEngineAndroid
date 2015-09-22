package com.gust.game_AI.behavior_tree;


public class BT_OrNode extends BT_CompositeNode {

	@Override
	public boolean update(float deltaTime) {
		// TODO Auto-generated method stub
//		if (tag != null)
//			GPLogger.log("node", tag);
		if (children != null && children.size() > 0) {
			for (BT_Node child : children) {
				if (child.update(deltaTime))
					return true;
			}
		}
		return false;
	}

}
