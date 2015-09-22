package com.gust.render_engine.core;

import com.gust.common.math.GPVector3f;

/**
 * 上帝视角摄像机类
 * @author gustplus
 *
 */
public class GPLookAtCamera3D extends Camera{
	final GPVector3f position;			//相机位置
	final GPVector3f up;					//头顶向量，用于确定摄像机方向
	final GPVector3f lookAt;				//摄像机的目标点
	float fieldOfView;
	float aspectRatio;					//视图的长宽比
	float near;							
	float far;
	
	/**
	 * 
	 * @param fieldOfView Y轴方向的视角大小（一般为67）
	 * @param aspectRatio 视图的长宽比
	 * @param near 近平面距离（必须大于0）
	 * @param far 远平面距离（必须大于0）
	 */
	public GPLookAtCamera3D(float fieldOfView, float aspectRatio, float near, float far){
		this.fieldOfView= fieldOfView;
		this.aspectRatio = aspectRatio;
		this.near = near;
		this.far = far;
		
		this.position =new GPVector3f();
		this.up = new GPVector3f(0, 1, 0);
		this.lookAt = new GPVector3f(0, 0, -1);
	}
	
	public GPVector3f getPosition(){
		return position;
	}
	
	public GPVector3f getUp(){
		return up;
	}
	
	/**
	 * 取得摄像机的目标点
	 * @return Vector3f摄像机的目标点
	 */
	public GPVector3f getLookAt(){
		return lookAt;
	}
	
	/**
	 * 启动摄像机
	 */
	public void setup(){
		if (isProjectionChange) {
			projectionMat.loadIdentity();
			projectionMat.perspective(fieldOfView, aspectRatio, near, far);
			isProjectionChange = false;
		}
		if (isViewChange) {
			isViewChange = false;
			viewMat.loadIdentity();
			viewMat.lookatf(position.x, position.y, position.z, lookAt.x, lookAt.y, lookAt.z, up.x, up.y, up.z);
		}
	}
}
