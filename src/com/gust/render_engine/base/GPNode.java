package com.gust.render_engine.base;

import java.util.ArrayList;
import java.util.List;

import android.opengl.GLES20;

import com.gust.common.game_util.GPLogger;
import com.gust.common.math.GPVector3f;
import com.gust.physics3D.GPAABB;

public class GPNode extends GPSpatial {

	public int depth; // 深度

	protected GPNode parent;

	public GPVector3f rotateOffsetFromCenter;

	List<GPNode> children;

	public float[] quat; // 表示物体方向的四元数{w,x,y,z}

	private static int count = 0;

	private int num;

	public boolean canRotate;

	public GPAABB AABB;
	private boolean AABBchanged;

	protected boolean visible;

	public GPNode(GPVector3f position) {
		// TODO Auto-generated constructor stub
		super(position);
		++count;
		num = count;
		this.AABB = new GPAABB();
		this.quat = new float[] { 0, 0, 0, 0 };
		this.rotateOffsetFromCenter = new GPVector3f();
		this.AABBchanged = false;
		angles = new GPVector3f();
		this.canRotate = true;
		this.visible = true;
	}

	public GPNode() {
		// TODO Auto-generated constructor stub
		super();
		++count;
		num = count;
		this.AABB = new GPAABB();
		this.quat = new float[] { 0, 0, 0, 0 };
		this.rotateOffsetFromCenter = new GPVector3f();
		this.AABBchanged = false;
		angles = new GPVector3f();
		this.canRotate = true;
	}

	public void setRotation(float[] quat) {
		this.quat = quat;
	}

	public int getChildNum() {
		if (children != null) {
			return children.size();
		}
		return 0;
	}

	public void setOffset(GPVector3f rotateOffsetFromCenter) {
		this.rotateOffsetFromCenter = rotateOffsetFromCenter;
	}

	public void draw(int type) {
		// TODO Auto-generated method stub
		GPMatrixState.pushMatrix();

		GPMatrixState.translatef(position.x, position.y, position.z);

		GPMatrixState.translatef(-rotateOffsetFromCenter.x,
				-rotateOffsetFromCenter.y, -rotateOffsetFromCenter.z);

		if (canRotate) {
			GPMatrixState.rotatef(angles.z, 0, 0, 1);
			GPMatrixState.rotatef(angles.y, 0, 1, 0);
			GPMatrixState.rotatef(angles.x, 1, 0, 0);
		}

		GPMatrixState.translatef(rotateOffsetFromCenter.x,
				rotateOffsetFromCenter.y, rotateOffsetFromCenter.z);
		if (visible) {
			drawSelf(type);
			if (children != null) {
				int size = children.size();
				GPNode temp;
				for (int i = 0; i < size; i++) {
					temp = this.children.get(i);
					temp.draw(type);
				}
			}
		}
		GPMatrixState.popMatrix();

	}

	public void draw() {
		// TODO Auto-generated method stub
		draw(GLES20.GL_TRIANGLES);
	}

	public void setParent(GPNode parent) {
		this.parent = parent;
		this.depth = parent.depth + 1;
	}

	public GPNode getParent() {
		if (parent != null)
			return parent;
		else {
			GPLogger.log(tag, "no parent");
			return null;
		}
	}

	public void addChild(GPNode child, String tag) {
		AABBchanged = true;
		child.setParent(this);
		child.tag = tag;
		if (children == null)
			children = new ArrayList<GPNode>(10);
		this.children.add(child);
	}

	public void addChild(GPNode child) {
		this.addChild(child, this.num + "");
	}

	public GPNode getChildByTag(String tag) {
		if (children != null) {
			int size = children.size();
			GPNode temp;
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					temp = children.get(i);
					if (temp.tag.equals(tag))
						return temp;
				}
			}
		}
		GPLogger.log(tag, "child not found");
		return null;
	}

	public void removeAllChildren() {
		AABBchanged = true;
		if (children != null)
			this.children.clear();
	}

	public void removeChild(GPNode child) {
		if (children != null) {
			AABBchanged = true;
			child.removeAllChildren();
			this.children.remove(child);
		}
	}

	public final boolean isLeaf() {
		if (children != null)
			if (children.size() == 0)
				return true;
			else
				return false;
		else
			return true;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void drawSelf(int Type) {
		// do nothing, children should override this method
	}

	public GPAABB getAABB() {
		// TODO Auto-generated method stub
		if (children != null && AABBchanged) {
			GPAABB aabb;
			int size = children.size();
			GPNode temp;
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					temp = children.get(i);
					aabb = temp.getAABB();
//					if (aabb.left_Up_Front.x < AABB.left_Up_Front.x)
//						AABB.left_Up_Front.x = aabb.left_Up_Front.x;
//					if (aabb.right_Bottom_Behind.x > AABB.right_Bottom_Behind.x)
//						AABB.right_Bottom_Behind.x = aabb.right_Bottom_Behind.x;
//					if (aabb.left_Up_Front.y > AABB.left_Up_Front.y)
//						AABB.left_Up_Front.y = aabb.left_Up_Front.y;
//					if (aabb.right_Bottom_Behind.y < AABB.right_Bottom_Behind.y)
//						AABB.right_Bottom_Behind.y = aabb.right_Bottom_Behind.y;
//					if (aabb.right_Bottom_Behind.z > AABB.right_Bottom_Behind.z)
//						AABB.right_Bottom_Behind.z = aabb.right_Bottom_Behind.z;
//					if (aabb.left_Up_Front.z < AABB.left_Up_Front.z)
//						AABB.left_Up_Front.z = aabb.left_Up_Front.z;
				}
			}
		}
		return AABB;
	}

}
