package com.gust.render_engine.core;

import com.gust.common.math.GPMatrix4;

public abstract class Camera {
	protected GPMatrix4 projectionMat;
	protected GPMatrix4 viewMat;
	protected boolean isProjectionChange;
	protected boolean isViewChange;
	
	public Camera() {
		super();
		projectionMat = new GPMatrix4();
		viewMat = new GPMatrix4();
		isProjectionChange = false;
		isViewChange = false;
	}
	
	public GPMatrix4 getProjectionMatrix(){
		return projectionMat;
	}
	
	public GPMatrix4 getViewMatrix(){
		return viewMat;
	}
	
	public GPMatrix4 getProjectionAndViewMatrix(){
		return projectionMat.mul(viewMat);
	}
	
	public abstract void setup();
}
