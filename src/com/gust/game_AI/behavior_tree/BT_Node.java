package com.gust.game_AI.behavior_tree;

public abstract class BT_Node {
	protected BT_Node child;
	protected BT_Node parent;
	protected int depth;
	protected String tag;

	public BT_Node() {
		parent = null;
	}

	public void addChild(BT_Node child, String tag) {
		this.child = child;
		this.tag = tag;
	}

	public void setParent(BT_Node parent) {
		this.parent = parent;
		this.depth = parent.depth + 1;
	}

	public BT_Node getParent() {
		return parent;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public BT_Node getChildByTag(String tag) {
		if (child != null) {
			if (child.tag.equals(tag)) {
				return child;
			} else {
				BT_Node node = child.getChildByTag(tag);
				if (node != null)
					return node;
			}
		}
		return null;
	}

	public boolean update(float deltaTime) {
		if (child != null) {
//			GPLogger.log("node", tag);
			return child.update(deltaTime);
		}
		return true;
	}

	public void reset() {
		if (child != null)
			child.reset();
	}

	public void recycle() {
		if (child != null) {
			child.recycle();
		}
	}
}
