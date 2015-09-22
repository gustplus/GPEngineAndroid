package com.gust.physics3D;

import com.gust.common.math.GPMatrix4;
import com.gust.common.math.GPVector3f;

public class GPPlane {
	private GPVector3f normal;
	private GPVector3f point;
	private String tag;
	
	public GPPlane(GPVector3f normal, GPVector3f point){
		this.normal = normal;
		this.point = point;
		this.tag = "";
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public void rotate(GPMatrix4 transform){
		this.normal = transform.rotate(normal);
		this.point = transform.rotate(point);
	}
	
	public void transform(GPMatrix4 transform){
		this.normal = transform.rotate(normal);
		this.point = transform.transform(point);
	}
	
	public boolean isPostive(GPAABB aabb){
		GPVector3f point = aabb.getLeft_Up_Front();
		GPVector3f vector = point.sub(this.point);
		float result = vector.dotMul(normal);
		if(result >= 0){
			return true;
		}
		
		point = aabb.getLeft_Up_Front().clone();
		point.y = aabb.getRight_Bottom_Behind().y;
		vector = point.sub(this.point);
		result = vector.dotMul(normal);
		if(result >= 0){
			return true;
		}
		
		point = aabb.getLeft_Up_Front().clone();
		point.y = aabb.getRight_Bottom_Behind().y;
		point.z = aabb.getRight_Bottom_Behind().z;
		vector = point.sub(this.point);
		result = vector.dotMul(normal);
		if(result >= 0){
			return true;
		}
		
		point = aabb.getLeft_Up_Front().clone();
		point.z = aabb.getRight_Bottom_Behind().z;
		vector = point.sub(this.point);
		result = vector.dotMul(normal);
		if(result >= 0){
			return true;
		}
		
		point = aabb.getRight_Bottom_Behind().clone();
		vector = point.sub(this.point);
		result = vector.dotMul(normal);
		if(result >= 0){
			return true;
		}
		
		point = aabb.getRight_Bottom_Behind().clone();
		point.y = aabb.getLeft_Up_Front().y;
		vector = point.sub(this.point);
		result = vector.dotMul(normal);
		if(result >= 0){
			return true;
		}
		
		point = aabb.getRight_Bottom_Behind().clone();
		point.y = aabb.getLeft_Up_Front().y;
		point.z = aabb.getLeft_Up_Front().z;
		vector = point.sub(this.point);
		result = vector.dotMul(normal);
		if(result >= 0){
			return true;
		}
		
		point = aabb.getRight_Bottom_Behind().clone();
		point.z = aabb.getLeft_Up_Front().z;
		vector = point.sub(this.point);
		result = vector.dotMul(normal);
		if(result >= 0){
			return true;
		}
		
		return false;
	}
}
