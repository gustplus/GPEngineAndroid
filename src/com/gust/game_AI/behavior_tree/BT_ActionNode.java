package com.gust.game_AI.behavior_tree;

import com.gust.common.game_util.GPLogger;

public abstract class BT_ActionNode extends BT_Node {
	
	@Override
	public void addChild(BT_Node child, String tag) {
		GPLogger.log("action node", "action can't add child!");
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
