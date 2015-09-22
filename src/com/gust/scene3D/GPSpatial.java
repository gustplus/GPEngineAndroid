package com.gust.scene3D;

import com.gust.common.math.GPConstants;
import com.gust.common.math.GPMatrix4;
import com.gust.common.math.GPVector3f;
import com.gust.physics3D.GPAABB;

public abstract class GPSpatial {
	protected GPVector3f position;
	protected GPVector3f worldPosition;

	public GPVector3f angles; // x,y,z分别为pitch,yaw,roll;
	public GPVector3f scales;
	protected GPMatrix4 worldTransform;
	protected GPMatrix4 selfTransform;

	protected boolean visible;

	public int tag;

	protected GPSpatial parent;

	protected boolean worldIsCurrent; // 标记矩阵、位置是否需要重新计算
	protected boolean boundIsCurrent; // 标记包围盒是否需要重新计算

	public GPAABB AABB;

	public GPSpatial(GPVector3f position) {
		this.position = position.clone();
		this.worldPosition = position.clone();
		this.worldIsCurrent = false;
		this.boundIsCurrent = false;
		this.angles = new GPVector3f();
		worldTransform = new GPMatrix4();
		selfTransform = new GPMatrix4();
		scales = new GPVector3f(1, 1, 1);
		this.AABB = new GPAABB();
		this.tag = -1;
		visible = true;
	}

	public GPSpatial() {
		this.position = new GPVector3f();
		this.worldPosition = position.clone();
		this.angles = new GPVector3f();
		this.worldIsCurrent = false;
		this.boundIsCurrent = false;
		worldTransform = new GPMatrix4();
		selfTransform = new GPMatrix4();
		scales = new GPVector3f(1, 1, 1);
		this.AABB = new GPAABB();
		this.tag = -1;
		visible = true;
	}

	public void setPosition(GPVector3f position) {
		this.position = position;
		this.worldIsCurrent = false;
		this.boundIsCurrent = false;
	}

	public void Rotate(float angle, float x, float y, float z) {
		this.angles.add(x, y, z);
		this.worldIsCurrent = false;
		this.boundIsCurrent = false;
	}

	public void setRotate(GPVector3f angles) {
		if (!this.angles.equals(angles)) {
			this.worldIsCurrent = false;
			this.boundIsCurrent = false;
			this.angles = angles.clone();
		}
	}

	public void updateWorldData() {
		updateTransforms();
		updateWorldPosition();
	}

	public void updateTransforms() {
		// TODO Auto-generated method stub
		// if (parent != null && !parent.worldIsCurrent) {
		// boundIsCurrent = false;
		// }
		// 更新modelTransform
		if (!worldIsCurrent) {
			this.selfTransform.loadIdentity();
			if (position.x > GPConstants.almostZero
					|| position.y > GPConstants.almostZero) {
				this.selfTransform.transform(position);
			}
			if (angles.x > GPConstants.almostZero) {
				this.selfTransform.rotatef(angles.x, 1, 0, 0);
			}
			if (angles.y > GPConstants.almostZero) {
				this.selfTransform.rotatef(angles.x, 0, 1, 0);
			}
			if (angles.z > GPConstants.almostZero) {
				this.selfTransform.rotatef(angles.x, 0, 0, 1);
			}
			if (this.scales.x != 1 || this.scales.y != 1 || this.scales.z != 1) {
				this.selfTransform.scalef(scales.x, scales.y, scales.z);
			}
			// 更新worldTransform
			if (parent != null) {
				this.worldTransform = parent.worldTransform.mul(selfTransform);
			} else {
				this.worldTransform = selfTransform.clone();
			}

			// worldIsCurrent = false; // 本节点位置改变，标记子节点需要更新
		}
	}

	private void updateWorldPosition() {
		// TODO Auto-generated method stub
		if (!worldIsCurrent) {
			if (parent == null) {
				this.worldPosition = position;
			} else {
				this.worldPosition = parent.worldTransform.transform(position);
			}
		}
	}

	public void updateGeometryState(boolean updateParent) {
		updateWorldData();
		worldIsCurrent = true;
		if (!boundIsCurrent) {
			updateWorldBound();
		}
		this.boundIsCurrent = true;
		if (updateParent) {
			propagateBoundToRoot();
		}
	}

	public void propagateBoundToRoot() {
		if (parent != null) {
			parent.updateWorldBound();
			parent.propagateBoundToRoot();
		}
	}

	public void updateBoundState(){
		updateWorldBound();
		propagateBoundToRoot();
	}

	public abstract void updateWorldBound();

	public GPAABB getBound() {
		// TODO Auto-generated method stub
		return AABB.clone();
	}

	public GPVector3f getWorldPosition() {
		return worldPosition.clone();
	}

	public void setWorldPosition(GPVector3f worldPosition) {
		this.worldPosition = worldPosition;
	}

	public GPMatrix4 getWorldTransform() {
		return worldTransform.clone();
	}

	public void setWorldTransform(GPMatrix4 worldTransform) {
		this.worldTransform = worldTransform;
	}

	public GPMatrix4 getSelfTransform() {
		return selfTransform.clone();
	}

	public void setSelfTransform(GPMatrix4 selfTransform) {
		this.selfTransform = selfTransform;
	}

	public void removeFromParent() {
		((GPNode) parent).removeChild(this);
	}

	public abstract void removeChild(GPSpatial child);

	public abstract void removeChildByTag(int tag);
}
