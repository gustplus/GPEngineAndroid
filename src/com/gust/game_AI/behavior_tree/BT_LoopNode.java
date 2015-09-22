package com.gust.game_AI.behavior_tree;


public class BT_LoopNode extends BT_DecoratorNode {

	private int loopTime;
	
	public BT_LoopNode(int loop) {
		// TODO Auto-generated constructor stub
		this.loopTime = loop;
	}
	
	@Override
	public boolean update(float deltaTime) {
		// TODO Auto-generated method stub
//		GPLogger.log("node", tag);
		for(int i = 0;i<loopTime;i++){
			child.update(deltaTime);
		}
		return true;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
