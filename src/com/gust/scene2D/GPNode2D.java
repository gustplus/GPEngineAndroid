package com.gust.scene2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gust.action.GPAction2D;
import com.gust.action.GPActionManager;
import com.gust.common.game_util.GPLogger;
import com.gust.common.math.GPConstants;
import com.gust.common.math.GPMatrix4;
import com.gust.common.math.GPVector2f;
import com.gust.common.ui.GPEventDispatcher;
import com.gust.physics2D.Bound_Rectangle;
import com.gust.physics2D.Collision_System2D;
import com.gust.system.GPInput.GPTouch;

public abstract class GPNode2D extends GPEventDispatcher implements
		Comparable<GPNode2D>, GPTouchDelegate {
	protected GPVector2f position;
	protected GPVector2f worldPosition;
	protected Bound_Rectangle bound = new Bound_Rectangle(0, 0, 0, 0);
	protected Bound_Rectangle contentBound = new Bound_Rectangle(0, 0, 0, 0);
	public boolean visible;
	protected float transparent; // 透明度
	protected float scaleX;
	protected float scaleY;
	protected float rotation;
	protected float worldRotation;
	public float depthZ;
	protected GPVector2f anthorPoint; // 锚点
	protected boolean touchChildren; // 子节点可否收到触摸事件
	public boolean swallow; // 吞噬触摸事件
	protected boolean shouldRemoveTouch; // 本节点是否不将触摸事件传递给后面的兄弟节点

	protected boolean worldIsCurrent; // 标记节点的位置，变换矩阵是否需要重新计算
	protected boolean modelIsCurrent; // 标记节点的boundingbox的体积数据是否需要重新计算

	protected GPMatrix4 worldTransform;
	protected GPMatrix4 selfTransform;

	protected ArrayList<GPNode2D> children;
	protected GPNode2D parent;

	protected boolean shouldBatch; // 是否加入批渲染队列

	protected int tag;

	protected boolean childrenNeedUpdate;

	// public String name = "";

	protected ArrayList<Integer> pointers;

	public GPNode2D(float x, float y, float width, float height) {
		this.setBoundBox(new Bound_Rectangle(x, y, width, height));
		this.transparent = 1.0f;
		this.position = new GPVector2f(x, y);
		this.worldPosition = new GPVector2f(x, y);
		visible = true;
		scaleX = scaleY = 1;
		rotation = 0;
		worldRotation = 0;
		depthZ = 0;
		this.swallow = false;
		// children = new ArrayList<GPNode2D>(3);
		touchChildren = true;
		anthorPoint = new GPVector2f();
		worldIsCurrent = false;
		modelIsCurrent = false;
		shouldBatch = false;
		this.swallow = false;
		shouldRemoveTouch = false;
		tag = -1;
		selfTransform = new GPMatrix4();
		worldTransform = new GPMatrix4();
		pointers = new ArrayList<Integer>(1);

		childrenNeedUpdate = true;
		// name = "GPNode";
	}

	public GPNode2D() {
		this.setBoundBox(new Bound_Rectangle(0, 0, 0, 0));
		this.transparent = 1.0f;
		this.position = new GPVector2f();
		this.worldPosition = new GPVector2f();
		visible = true;
		scaleX = scaleY = 1;
		rotation = 0;
		worldRotation = 0;
		depthZ = 0;
		this.swallow = false;
		// children = new ArrayList<GPNode2D>(3);
		touchChildren = true;
		anthorPoint = new GPVector2f();
		worldIsCurrent = false;
		modelIsCurrent = false;
		shouldBatch = false;
		this.swallow = false;
		shouldRemoveTouch = false;
		tag = -1;
		selfTransform = new GPMatrix4();
		worldTransform = new GPMatrix4();
		pointers = new ArrayList<Integer>(1);

		childrenNeedUpdate = true;
		// name = "GPNode";
	}

	public float getTransparent() {
		return transparent;
	}

	public void setTransparent(float transparent) {
		this.transparent = transparent;
		if (this.transparent <= 0) {
			this.visible = false;
		} else {
			visible = true;
			if (parent != null) {
				parent.modelIsCurrent = false;
			}
		}
	}

	public void changeToColor(float[] color, float time) {
	}

	public void setFunctionID(int functionID) {
	}

	// 用于根据anthorPoint计算位置（见updatetransform）
	protected GPVector2f getWH() {
		return new GPVector2f(bound.width, bound.height);
	}

	public GPMatrix4 getWorldTransform() {
		return worldTransform;
	}

	public GPMatrix4 getSelfTransform() {
		return selfTransform;
	}

	public Bound_Rectangle getContentBound() {
		return contentBound;
	}

	public void setContentSize(Bound_Rectangle contentBound) {
		// do nothing, the content size of mode is always (0,0,0,0)
	}

	public float getScaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		if (scaleX != this.scaleX) {
			this.modelIsCurrent = false;
			this.scaleX = scaleX;
		}
		// postUpdateFlagToParent();
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		if (scaleY != this.scaleY) {
			this.modelIsCurrent = false;
			this.scaleY = scaleY;
		}
		// postUpdateFlagToParent();
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.modelIsCurrent = false;
		this.rotation = GPConstants.translateAngleInround(rotation);

		// postUpdateFlagToParent();
	}

	public void setVisible(boolean visible) {
		if (this.visible != visible) {
			this.visible = visible;
			if (parent != null) {
				parent.modelIsCurrent = false;
			}
		}
		// postUpdateFlagToParent();
	}

	public GPVector2f getAnthorPoint() {
		return anthorPoint.clone();
	}

	public void setAnthorPoint(GPVector2f anthorPoint) {
		// do nothing, the anthorPoint of node is always (0, 0)
	}

	/**
	 * 更新状态
	 * 
	 * @param touches触摸事件
	 */
	public void update(List<GPTouch> touches, float deltaTime) {
		updateEvents(deltaTime);
		update(deltaTime);
		// sortAllChildren();
		updateChildren(touches, deltaTime);
	}

	public void updateChildren(List<GPTouch> touches, float deltaTime) {
		if (children != null) {
			int len = children.size();
			for (int i = len - 1; i >= 0; --i) {
				children.get(i).update(touches, deltaTime);
			}
		}
	}

	public void sortAllChildren() {
		if (children != null) {
			if (children != null && children.size() > 0) {
				Collections.sort(children);
			}
		}
	}

	public void update(float deltaTime) {

	}

	/**
	 * 更新空间数据，由根节点在每帧调用
	 */
	public void updateGeometryState() {
		this.updateTransform();
		this.updateWorldTransform();
		this.updateWorldPosition();
		if (children != null) {
			int len = children.size();
			for (int i = 0; i < len; ++i) {
				if (!worldIsCurrent) {
					// children.get(i).modelIsCurrent = false;
					children.get(i).worldIsCurrent = false;
				}
				// if (childrenNeedUpdate || !worldIsCurrent) {
				// GPLogger.log("", "update children");
				children.get(i).updateGeometryState();
				// }
			}
		}
		this.calculateBoundingBox();
		worldIsCurrent = true;
		modelIsCurrent = true;
		childrenNeedUpdate = false;
	}

	protected void updateTransform() {
		// if (parent != null && !parent.worldIsCurrent) {
		// worldIsCurrent = false;
		// }
		if (!worldIsCurrent || !modelIsCurrent) {
			// GPLogger.log("node", "update transform");
			// this.selfTransform.loadIdentity();
			// this.selfTransform.translatef(position.x, position.y, 0);
			// if (rotation > GPConstants.almostZero
			// || rotation < -GPConstants.almostZero) {
			// this.selfTransform.rotatef(rotation, 0, 0, 1);
			// }
			// if (scaleX > GPConstants.almostZero
			// || scaleX < -GPConstants.almostZero
			// || scaleY > GPConstants.almostZero
			// || scaleY < -GPConstants.almostZero) {
			// this.selfTransform.scalef(scaleX, scaleY, 1);
			// }
			// if (anthorPoint.x > GPConstants.almostZero
			// || anthorPoint.x < -GPConstants.almostZero
			// || anthorPoint.y > GPConstants.almostZero
			// || anthorPoint.y < -GPConstants.almostZero) {
			// GPVector2f bound = getWH();
			// this.selfTransform.translatef(-bound.x * anthorPoint.x,
			// -bound.y * anthorPoint.y, 0);
			// }
			if (!modelIsCurrent && worldIsCurrent) {
				if (anthorPoint.x < GPConstants.almostZero
						|| anthorPoint.x > -GPConstants.almostZero
						|| anthorPoint.y < GPConstants.almostZero
						|| anthorPoint.y > -GPConstants.almostZero) {
					return;
				}
			}
			GPVector2f bound = getWH();
			this.selfTransform.setTransform(bound.x, bound.y, anthorPoint,
					this.position, rotation, scaleX, scaleY);
		}
	}

	protected void updateWorldTransform() {
		if (!worldIsCurrent || !modelIsCurrent) {
			if (!modelIsCurrent && worldIsCurrent) {
				if (anthorPoint.x < GPConstants.almostZero
						|| anthorPoint.x > -GPConstants.almostZero
						|| anthorPoint.y < GPConstants.almostZero
						|| anthorPoint.y > -GPConstants.almostZero) {
					return;
				}
			}
			if (parent != null) {
				// GPLogger.log("node", "update world transform");
				this.worldTransform = parent.worldTransform.mul(selfTransform);
				worldRotation = parent.worldRotation + rotation;
			} else {
				this.worldTransform = selfTransform.clone();
				worldRotation = rotation;
			}
		}
	}

	protected void updateWorldPosition() {
		// if (parent != null && !parent.worldIsCurrent) {
		// worldIsCurrent = false;
		// }
		if (!worldIsCurrent) {
			// GPLogger.log("node", "update world position");
			if (parent == null) {
				this.worldPosition = position;
			} else {
				this.worldPosition = parent.worldTransform.transform(position);
			}
		}
	}

	protected void calculateBoundingBox() {
		if (!modelIsCurrent || !worldIsCurrent) {
			// GPLogger.log("node", "update bounding box");
			if (parent != null) {
				parent.modelIsCurrent = false;
			}
			if (children != null) {
				int size = children.size();
				if (size != 0) {
					this.bound = children.get(0).getBoundBox();
				}
				for (int i = 1; i < size; ++i) {
					this.bound.include(children.get(i).getBoundBox());
				}
			}
		}
	}

	public void reloadSelf() {

	}

	public void reload() {
		reloadSelf();
		if (children != null) {
			int len = children.size();
			for (int i = 0; i < len; ++i) {
				children.get(i).reload();
			}
		}
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public void runAction(GPAction2D action) {
		if (action == null) {
			GPLogger.log("node", "action is null");
			return;
		}
		action.initWithComponent(this);
		GPActionManager.getInstance().addAction(action);
	}

	public void stopAction(GPAction2D action) {
		if (action == null || action.component != this) {
			return;
		}
		GPActionManager.getInstance().removeAction(action);
	}

	public GPVector2f getPosition() {
		return position.clone();
	}

	public GPVector2f getWorldPosition() {
		this.updateTransform();
		this.updateWorldTransform();
		this.updateWorldPosition();
		return worldPosition.clone();
	}

	public void setPosition(GPVector2f position) {
		if (this.position.equals(position)) {
			return;
		}
		this.position.set(position);
		worldIsCurrent = false;

		// postUpdateFlagToParent();
	}

	public void setPosition(float x, float y) {
		this.position.set(x, y);
		worldIsCurrent = false;

		// postUpdateFlagToParent();
	}

	public final GPVector2f getPositionRef() {
		return position;
	}

	public void setPositionRef(GPVector2f position) {
		this.position = position;
		worldIsCurrent = false;

		// postUpdateFlagToParent();
	}

	public void move(GPVector2f move) {
		if ((move.x > GPConstants.almostZero || move.x < -GPConstants.almostZero)
				|| (move.y > GPConstants.almostZero || move.y < -GPConstants.almostZero)) {
			this.position.addTo(move);
			worldIsCurrent = false;

			// postUpdateFlagToParent();
		}
	}

	public void move(float x, float y) {
		if ((x > GPConstants.almostZero || x < -GPConstants.almostZero)
				|| (y > GPConstants.almostZero || y < -GPConstants.almostZero)) {
			this.position.addTo(x, y);
			worldIsCurrent = false;
		}
	}

	public void setWidth(float width) {
		this.bound.width = width;
		modelIsCurrent = false;
	}

	public float getWidth() {
		return bound.width;
	}

	public void setHeight(float height) {
		this.bound.height = height;
		modelIsCurrent = false;
	}

	public float getHeight() {
		return bound.height;
	}

	public boolean containPoint(GPVector2f point) {
		return bound.isPointIn(point);
	}

	public void addChild(GPNode2D child) {
		if (children == null) {
			children = new ArrayList<GPNode2D>(3);
		}
		if (child == null) {
			GPLogger.log("Node", "child is null");
			return;
		}
		if (this instanceof GPSpriteBatchNode || this.shouldBatch) {
			child.shouldBatch = true;
		} else {
			child.shouldBatch = false;
		}
		if (child.parent != null) {
			child.parent.removeChild(child, false);
		}

		children.add(child);
		child.parent = this;
		modelIsCurrent = false;

		// postUpdateFlagToParent();
	}

	public void addChild(GPNode2D child, int tag) {
		if (children == null) {
			children = new ArrayList<GPNode2D>(3);
		}

		if (this instanceof GPSpriteBatchNode || this.shouldBatch) {
			shouldBatch = true;
		} else {
			shouldBatch = false;
		}
		if (child.parent != null) {
			child.parent.removeChild(child, false);
		}
		children.add(child);
		child.tag = tag;
		child.parent = this;
		modelIsCurrent = false;

		// postUpdateFlagToParent();
	}

	public GPNode2D getChild(int index) {
		if (children == null) {
			return null;
		}
		return children.get(index);
	}

	public GPNode2D getChildByTag(int tag) {
		if (children == null) {
			return null;
		}
		int len = children.size();
		for (int i = 0; i < len; ++i) {
			GPNode2D child = children.get(i);
			if (child.tag == tag) {
				return child;
			}
		}
		return null;
	}

	public boolean removeChild(GPNode2D child, boolean cleanup) {
		if (children == null) {
			return false;
		}
		int index = children.indexOf(child);
		if (index < 0) {
			return false;
		}
		if (cleanup) {
			child.removeAllChildren();
		}
		children.remove(index);
		child.parent = null;
		child.worldIsCurrent = false;
		modelIsCurrent = false;

		// postUpdateFlagToParent();

		child.shouldBatch = false;

		return true;
	}

	public void removeAllChildren() {
		if (children == null) {
			return;
		}
		int len = children.size();
		for (int i = 0; i < len; ++i) {
			children.get(i).parent = null;
			children.get(i).shouldBatch = false;
			children.get(i).removeAllChildren();
			children.get(i).worldIsCurrent = false;
		}
		children.clear();
		this.bound.width = 0;
		this.bound.height = 0;
		modelIsCurrent = false;

		// postUpdateFlagToParent();
	}

	public void removeAllChildrenWithCleanUp() {
		if (children == null) {
			return;
		}
		int len = children.size();
		for (int i = 0; i < len; ++i) {
			children.get(i).parent = null;
			children.get(i).shouldBatch = false;
			children.get(i).removeAllChildren();
			children.get(i).worldIsCurrent = false;
			children.get(i).dispose();
		}
		children.clear();
		this.bound.width = 0;
		this.bound.height = 0;
		modelIsCurrent = false;

		// postUpdateFlagToParent();
	}

	public boolean removeChild(int index) {
		if (children == null) {
			return false;
		}
		if (index > 0 && index < children.size()) {
			GPNode2D child = children.remove(index);
			child.shouldBatch = false;
			child.parent = null;
			child.worldIsCurrent = false;
			modelIsCurrent = false;
			// postUpdateFlagToParent();
			return true;
		}
		return false;
	}

	public int numOfChildren() {
		if (children == null) {
			return 0;
		}
		return children.size();
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

	public void beforeDraw() {

	}

	public abstract void drawself();

	public void afterDraw() {

	}

	public void draw() {
		if (!visible || transparent < GPConstants.almostZero) {
			return;
		}
		beforeDraw();
		drawself();
		if (children != null) {
			int len = children.size();
			for (int i = 0; i < len; ++i) {
				if (!children.get(i).shouldBatch) {
					children.get(i).draw();
				}
			}
		}
		afterDraw();
	}

	public Bound_Rectangle getBoundBox() {
		this.updateGeometryState();
		return bound;
	}

	public void setBoundBox(Bound_Rectangle bound) {
		this.bound = bound;
		// this.contentBound = new Bound_Rectangle(position.x, position.y, 0,
		// 0);
	}

	public int compareTo(GPNode2D other) {
		return this.depthZ < other.depthZ ? -1 : 1;
	}

	public boolean dispatchTouch(GPTouch touch, float deltaTime) {
		if (dispatch(touch, deltaTime)) {
			return true;
		}
		if (children != null) {
			int len = children.size();
			for (int i = len - 1; i >= 0; --i) {
				GPNode2D child = children.get(i);
				if (child.dispatchTouch(touch, deltaTime)) {
					return true;
				}
			}

			if (shouldRemoveTouch) {
				shouldRemoveTouch = false;
				return true;
			}
		}
		return false;
	}

	private boolean dispatch(GPTouch touch, float deltaTime) {
		if (touch != null && visible) {
			if (touch.type == GPTouch.TOUCH_DOWN) {
				if (Collision_System2D.pointInRectangle(contentBound,
						new GPVector2f(touch.x, touch.y))) {
					boolean touchResult = visible
							&& onTouchDown(touch, deltaTime);
					if (touchResult) {
						shouldRemoveTouch = true;
						pointers.add(touch.pointer);
						if (touchChildren) {
							return false;
						}
						return true;
					}
				}
			}
			if (touch.type == GPTouch.TOUCH_UP) {
				if (pointers.contains(touch.pointer)) {
					onTouchUp(touch, deltaTime);
					pointers.remove(new Integer(touch.pointer));
					if (pointers.size() > 1) {
						pointers.trimToSize();
					}
					if (touchChildren) {
						return false;
					}
					return true;
				}
			}
			if (touch.type == GPTouch.TOUCH_DRAGGED) {
				if (pointers.contains(touch.pointer)) {
					onTouchDrag(touch, deltaTime);
					if (touchChildren) {
						return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	public boolean onTouchDown(GPTouch touch, float deltaTime) {
		return false;
	}

	public void onTouchDrag(GPTouch touch, float deltaTime) {
	}

	public void onTouchUp(GPTouch touch, float deltaTime) {
	}

	public void onTouchesDown(ArrayList<GPTouch> touches, float deltaTime) {
	}

	public void onTouchesDrag(ArrayList<GPTouch> touches, float deltaTime) {
	}

	public void onToucheshUp(ArrayList<GPTouch> touches, float deltaTime) {
	}

	public void dispose() {

	}

	// protected void //postUpdateFlagToParent() {
	// if (parent != null && !parent.childrenNeedUpdate) {
	// parent.childrenNeedUpdate = true;
	// parent.postUpdateFlagToParent();
	// }
	// }
}
