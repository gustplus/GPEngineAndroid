package com.gust.scene3D;

import java.util.ArrayList;

import com.gust.common.game_util.GPLogger;
import com.gust.common.math.GPVector3f;
import com.gust.physics3D.GPAABB;

public class GPNode extends GPSpatial {

	public int depth; // …Ó∂»

	ArrayList<GPSpatial> children;

	public boolean canRotate;

	public GPNode(GPVector3f position) {
		// TODO Auto-generated constructor stub
		super(position);
		this.canRotate = true;
		this.visible = true;
		depth = 0;
	}

	public GPNode() {
		// TODO Auto-generated constructor stub
		super();
		this.canRotate = true;
		this.visible = true;
		depth = 0;
	}

	public int numOfChildren() {
		if (children != null) {
			return children.size();
		}
		return 0;
	}

	// public void draw(int primitiveMode) {
	// if (visible) {
	// drawSelf(primitiveMode);
	// if (children != null) {
	// int size = children.size();
	// GPSpatial temp;
	// for (int i = 0; i < size; i++) {
	// temp = this.children.get(i);
	// // temp.draw(primitiveMode);
	// }
	// }
	// }
	// }
	//
	// public void draw() {
	// // TODO Auto-generated method stub
	// draw(GLES20.GL_TRIANGLES);
	// }

	protected void setParent(GPNode parent) {
		if (parent == null) {
			return;
		}
		if (this.parent != null) {
			removeFromParent();
		}
		this.parent = parent;
		this.depth = parent.depth + 1;
		this.worldIsCurrent = false;
		this.boundIsCurrent = false;
	}

	public GPSpatial getParent() {
		if (parent != null)
			return parent;
		else {
			GPLogger.log("GPNode", "no parent");
			return null;
		}
	}

	public void addChild(GPNode child, int tag) {
		if (child == null) {
			return;
		}
		if (parent != null) {
			removeFromParent();
		}
		child.setParent(this);
		child.tag = tag;
		if (children == null)
			children = new ArrayList<GPSpatial>(5);
		this.children.add(child);
		this.worldIsCurrent = false;
		this.boundIsCurrent = false;
	}

	public void addChild(GPNode child) {
		this.addChild(child, -1);
		this.worldIsCurrent = false;
		this.boundIsCurrent = false;
	}

	public GPSpatial getChildByTag(int tag) {
		if (children != null) {
			int size = children.size();
			GPSpatial temp;
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					temp = children.get(i);
					if (temp.tag == tag)
						return temp;
				}
			}
		}
		GPLogger.log("GPNode", "child not found");
		return null;
	}

	public void removeAllChildren() {
		if (children != null) {
			this.children.clear();
			this.worldIsCurrent = false;
			this.boundIsCurrent = false;
		}
	}

	public void removeChild(GPNode child) {
		if (child == null) {
			return;
		}
		if (children != null) {
			child.removeAllChildren();
			this.children.remove(child);
			this.worldIsCurrent = false;
			this.boundIsCurrent = false;
		}
	}

	public final boolean isLeaf() {
		if (parent == null) {
			return false;
		}
		if (children != null) {
			if (children.size() == 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	// public void drawSelf(int Type) {
	// // do nothing, children should override this method
	// }

	public GPAABB getAABB() {
		return AABB.clone();
	}

	@Override
	public void updateWorldBound() {
		if (children != null) {
			int size = children.size();
			GPSpatial temp;
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					temp = children.get(i);
					if (i == 0) {
						this.AABB = temp.getBound();
					}
					AABB.include(temp.getBound());
				}
			}
		}
	}

	@Override
	public void updateWorldData() {
		// TODO Auto-generated method stub
		super.updateWorldData();
		int len = children.size();
		for (int i = 0; i < len; ++i) {
			children.get(i).updateGeometryState(false);
		}
	}

	@Override
	public void removeChild(GPSpatial child) {
		// TODO Auto-generated method stub
		children.remove(child);
		this.worldIsCurrent = false;
		this.boundIsCurrent = false;
	}

	@Override
	public void removeChildByTag(int tag) {
		// TODO Auto-generated method stub
		int len = children.size();
		for (int i = 0; i < len; ++i) {
			GPSpatial child = children.get(i);
			if (tag == child.tag) {
				children.remove(i);
				this.worldIsCurrent = false;
				this.boundIsCurrent = false;
			}
		}
	}
}
