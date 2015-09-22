package com.gust.game_AI.behavior_tree;

import com.gust.common.game_util.GPLogger;

public class BT_ParallelNode extends BT_CompositeNode {
	private BT_ActionListener listener;
	
	public void setActionListener(BT_ActionListener listener){
		this.listener = listener;
	}
	
	@Override
	public boolean update(float deltaTime) {
		// TODO Auto-generated method stub
		GPLogger.log("node", tag);
		int size = children.size();
		if (children != null&&size>0) {
			boolean results[] = new boolean[size];
			for (int i = 0;i<size;i++) {
				results[i] = children.get(i).update(deltaTime);
			}
			if(listener != null){
				return listener.action(results);
			}
		}
		return true;
	}

}
