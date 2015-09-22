package com.gust.game_AI.behavior_tree;


/*
 * 计数节点
 */
public class BT_CounterNode extends BT_DecoratorNode {
	private int count;
	private int past;

	public BT_CounterNode(int count) {
		// TODO Auto-generated constructor stub
		this.count = count;
		this.past = 0;
	}

	@Override
	public boolean update(float deltaTime) {
		// TODO Auto-generated method stub
//		GPLogger.log("node", tag);
		if (child.update(deltaTime))
			++past;
		if (past > count)
			return false;
		else
			return true;
	}
	
	public void reset(){
		count = 0;
	}

}
