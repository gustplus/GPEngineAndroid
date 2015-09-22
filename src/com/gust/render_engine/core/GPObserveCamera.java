package com.gust.render_engine.core;

import com.gust.common.math.GPVector3f;
import com.gust.render_engine.base.GPMatrixState;
import com.gust.render_engine.base.GPRuntimeVaribles;

/**
 * 观察相机
 * 
 * @author gustplus
 * 
 */
public class GPObserveCamera {
	private float yaw;
	private float pitch;
	private float roll;
	private GPVector3f direction; 				// 视线方向
	private GPVector3f eyePoint; 					// 观察位置
	private GPVector3f focusPoint; 				// 视线焦点
	private float fieldOfView;
	private float aspectRatio;
	private float near;
	private float far;

	public GPObserveCamera(float fieldOfView, float aspectRatio, float near,
			float far)
	{
		// TODO Auto-generated constructor stub
		this.fieldOfView = fieldOfView;
		this.aspectRatio = aspectRatio;
		this.near = near;
		this.far = far;
		this.direction = new GPVector3f(0, 0, -1);
		this.yaw = 0;
		this.pitch = 0;
		this.roll = 0;
		this.eyePoint = new GPVector3f(0, 10, 20);
		this.focusPoint = new GPVector3f();
	}

	/**
	 * 设置视线焦点
	 * 
	 * @param eyePoint
	 */
	public void setEyePoint(GPVector3f eyePoint)
	{
		this.eyePoint.set(eyePoint);
		this.direction = this.focusPoint.add(this.eyePoint.mul(-1)).normalize();
	}

	/**
	 * 移动视线焦点
	 * 
	 * @param moveDistance
	 */
	public void moveEyePoint(GPVector3f moveDistance)
	{
		this.eyePoint.addTo(moveDistance);
		this.direction = this.focusPoint.add(this.eyePoint.mul(-1)).normalize();
	}

	public void moveFocusPoint(GPVector3f moveDistance)
	{
		this.focusPoint.addTo(moveDistance);
		this.direction = this.focusPoint.add(this.eyePoint.mul(-1)).normalize();
	}

	public void setFocusPoint(GPVector3f focusPoint)
	{
		this.focusPoint.set(focusPoint);
		this.direction = this.focusPoint.add(this.eyePoint.mul(-1)).normalize();
	}

	public void rotate(float yawInc, float pitchInc, float rollInc)
	{
		this.yaw += yawInc;
		this.pitch += pitchInc;
		this.roll += rollInc;
	}
	
	public void moveClose(float distance){
		eyePoint.add(direction.mul(distance));
	}

	public void setMatrices()
	{
		this.aspectRatio = GPRuntimeVaribles.SCREENWIDTH/GPRuntimeVaribles.SCREENHEIGHT;
		GPMatrixState.loadIdentity(GPMatrixState.PROJESTION_MODE);
		GPMatrixState.Perspective(fieldOfView, aspectRatio, near, far);
		GPMatrixState.loadIdentity(GPMatrixState.VIEW_MODE);
		GPMatrixState.lookatf(eyePoint.x, eyePoint.y, eyePoint.z, focusPoint.x,
				focusPoint.y, focusPoint.z, 0, 1, 0);
		GPMatrixState.loadIdentity(GPMatrixState.MODEL_MODE);
		GPMatrixState.rotatef(roll, 0, 0, 1);
		GPMatrixState.rotatef(pitch, 1, 0, 0);
		GPMatrixState.rotatef(yaw, 0, 1, 0);

	}
}
