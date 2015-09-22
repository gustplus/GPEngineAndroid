package com.gust.physics3D;

import com.gust.common.math.GPMatrix4;
import com.gust.common.math.GPVector3f;

public class GPAABB {
	private GPVector3f left_Up_Front;
	private GPVector3f right_Bottom_Behind;

	public GPVector3f getLeft_Up_Front() {
		return left_Up_Front.clone();
	}

	public void setLeft_Up_Front(GPVector3f left_Up_Front) {
		this.left_Up_Front = left_Up_Front;
	}

	public GPVector3f getRight_Bottom_Behind() {
		return right_Bottom_Behind.clone();
	}

	public void setRight_Bottom_Behind(GPVector3f right_Bottom_Behind) {
		this.right_Bottom_Behind = right_Bottom_Behind;
	}

	public GPAABB() {
		this.left_Up_Front = null;
		this.right_Bottom_Behind = null;
	}

	public void include(GPAABB aabb) {
		if (left_Up_Front == null) {
			left_Up_Front = aabb.left_Up_Front.clone();
		} else {
			if (aabb.left_Up_Front.x < left_Up_Front.x) {
				left_Up_Front.x = aabb.left_Up_Front.x;
			}
			if (aabb.left_Up_Front.y > left_Up_Front.y) {
				left_Up_Front.y = aabb.left_Up_Front.y;
			}
			if (aabb.left_Up_Front.z < left_Up_Front.z) {
				left_Up_Front.z = aabb.left_Up_Front.z;
			}
		}

		if (right_Bottom_Behind == null) {
			if (aabb.right_Bottom_Behind.x > right_Bottom_Behind.x) {
				right_Bottom_Behind.x = aabb.right_Bottom_Behind.x;
			}
			if (aabb.right_Bottom_Behind.y < right_Bottom_Behind.y) {
				right_Bottom_Behind.y = aabb.right_Bottom_Behind.y;
			}
			if (aabb.right_Bottom_Behind.z > right_Bottom_Behind.z) {
				right_Bottom_Behind.z = aabb.right_Bottom_Behind.z;
			}
		}
	}
	
	/**
	 * 对此AABB从模型坐标系转换到世界坐标系，返回新的AABB而不改变原AABB
	 * @param matrix
	 * @return 转换后的AABB
	 */
	public GPAABB transform(GPMatrix4 matrix) {
		GPAABB bound = new GPAABB();
		GPVector3f point = matrix.transform(left_Up_Front);
		bound.include(point);
		point = left_Up_Front.clone();
		point.y = right_Bottom_Behind.y;
		point = matrix.transform(point);
		bound.include(point);
		point = left_Up_Front.clone();
		point.y = right_Bottom_Behind.y;
		point.z = right_Bottom_Behind.z;
		point = matrix.transform(point);
		bound.include(point);
		point = left_Up_Front.clone();
		point.z = right_Bottom_Behind.z;
		point = matrix.transform(point);
		bound.include(point);

		point = matrix.transform(right_Bottom_Behind);
		bound.include(point);
		point = right_Bottom_Behind.clone();
		point.y = left_Up_Front.y;
		point = matrix.transform(point);
		bound.include(point);
		point = right_Bottom_Behind.clone();
		point.y = left_Up_Front.y;
		point.z = left_Up_Front.z;
		point = matrix.transform(point);
		bound.include(point);
		point = right_Bottom_Behind.clone();
		point.z = left_Up_Front.z;
		point = matrix.transform(point);
		bound.include(point);
		
		return bound;
	}

	public void include(GPVector3f point) {
		if (left_Up_Front == null) {
			left_Up_Front = point;
		} else {
			if (point.x < left_Up_Front.x) {
				left_Up_Front.x = point.x;
			}
			if (point.y > left_Up_Front.y) {
				left_Up_Front.y = point.y;
			}
			if (point.z < left_Up_Front.z) {
				left_Up_Front.z = point.z;
			}
		}

		if (right_Bottom_Behind == null) {
			right_Bottom_Behind = point;
		} else {
			if (point.x > right_Bottom_Behind.x) {
				right_Bottom_Behind.x = point.x;
			}
			if (point.y < right_Bottom_Behind.y) {
				right_Bottom_Behind.y = point.y;
			}
			if (point.z > right_Bottom_Behind.z) {
				right_Bottom_Behind.z = point.z;
			}
		}
	}

	public void reset() {
		left_Up_Front = null;
		right_Bottom_Behind = null;
	}

	public GPAABB clone() {
		GPAABB bound = new GPAABB();
		bound.left_Up_Front = left_Up_Front;
		bound.right_Bottom_Behind = right_Bottom_Behind;
		return bound;
	}
}
