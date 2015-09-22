package com.gust.game_AI.behavior_tree;

import java.util.ArrayList;

/*
 * 组合节点
 */
public abstract class BT_CompositeNode extends BT_Node {
	protected ArrayList<BT_Node> children;

	@Override
	public void addChild(BT_Node child, String tag) {
		// TODO Auto-generated method stub
		if (children == null)
			children = new ArrayList<BT_Node>();
		this.children.add(child);
		child.setTag(tag);
	}

	@Override
	public BT_Node getChildByTag(String tag) {
		// TODO Auto-generated method stub
		if (children != null&&children.size()>0) {
			for (BT_Node child : children) {
				if (child.tag.equals(tag)) {
					return child;
				} else {
					BT_Node node = child.getChildByTag(tag);
					if (node != null)
						return node;
				}
			}
		}
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		if(children != null)
		for (BT_Node child : children) {
			child.reset();
		}
	}

	@Override
	public void recycle() {
		// TODO Auto-generated method stub
		super.recycle();
		if(children != null){
			for(BT_Node child:children){
				child.recycle();
			}
		}
	}
	
	

}
