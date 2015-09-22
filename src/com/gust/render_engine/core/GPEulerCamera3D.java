package com.gust.render_engine.core;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.gust.common.math.GPConstants;
import com.gust.common.math.GPMatrix4;
import com.gust.common.math.GPVector3f;
import com.gust.engine.core.GPShader;
import com.gust.physics3D.GPPlane;
import com.gust.render_engine.base.GPMatrixState;
import com.gust.scene3D.GPCuller3D;

/**
 * ���ӽ��������
 * 
 * @author gustplus
 * 
 */
public class GPEulerCamera3D extends Camera {
	// private final GPVector3f position = new GPVector3f();
	private float[] postionPost;
	private float yaw = 0; // ���ӽ�
	private float tempYaw = 0; // ����ɫ�ڿ���ʱ���ڼ�¼�������Ļ��ӽ�
	private float pitch = 0; // ����
	private float roll = 0;
	private float fieldOfView;
	private float aspectRatio;
	private float near;
	private float far;
	private boolean isOnGround;
	private GPVector3f tempDirection; // ����ɫ�ڿ���ʱ���ڼ�¼�������ķ���
	public GPVector3f offset; // ������������ҽ�ɫ��λ��
	private int cameraHandler;
	private float maxPitch;
	private float minPitch;

	private GPVector3f position;

	private GPVector3f lookAtOffset;

	/**
	 * ���캯��
	 * 
	 * @param fieldOfView
	 *            �ӽǷ�Χ
	 * @param aspectRatio
	 *            �ӽǳ����
	 * @param near
	 *            ��ƽ��(�������0)
	 * @param far
	 *            Զƽ��(�������0)
	 */
	public GPEulerCamera3D(float fieldOfView, float aspectRatio, float near,
			float far) {
		this.fieldOfView = fieldOfView;
		this.aspectRatio = aspectRatio;
		this.near = near;
		this.far = far;
		this.position = new GPVector3f();
		this.offset = new GPVector3f(0, 0, 0);
		this.isOnGround = true;
		postionPost = new float[3];
		lookAtOffset = new GPVector3f();
		maxPitch = 90;
		minPitch = -90;
	}

	public GPCuller3D generateCuller() {
		GPCuller3D culler = new GPCuller3D();
		GPVector3f normal = new GPVector3f(0, 1, 0);
		normal.rotateTo(fieldOfView / 2, 1, 0, 0);
		GPPlane plane = new GPPlane(normal, new GPVector3f());
		culler.addPlane(plane, "up");

		normal = new GPVector3f(0, -1, 0);
		normal.rotateTo(-fieldOfView / 2, 1, 0, 0);
		plane = new GPPlane(normal, new GPVector3f());
		culler.addPlane(plane, "down");

		normal = new GPVector3f(0, 0, 1);
		plane = new GPPlane(normal, new GPVector3f(0, 0, near));
		culler.addPlane(plane, "front");

		normal = new GPVector3f(0, 0, -1);
		plane = new GPPlane(normal, new GPVector3f(0, 0, far));
		culler.addPlane(plane, "back");

		float tan = (float) (aspectRatio * Math.tan(fieldOfView / 2 * GPConstants.TO_RADIANS));
		float angle = (float) (Math.atan(tan) * GPConstants.TO_DEGREES);
		normal = new GPVector3f(-1, 0, 0);
		normal.rotateTo(angle, 0, 1, 0);
		plane = new GPPlane(normal, new GPVector3f());
		culler.addPlane(plane, "left");
		
		normal = new GPVector3f(1, 0, 0);
		normal.rotateTo(-angle, 1, 0, 0);
		plane = new GPPlane(normal, new GPVector3f());
		culler.addPlane(plane, "right");
		
		return culler;
	}
	
	public void updateCuller(GPCuller3D culler){
		if(isProjectionChange){
			culler = generateCuller();
		}
		if(isViewChange){
			GPMatrix4 invertMat = viewMat.clone();
			invertMat.invert();
			culler.transform(invertMat);
		}
	}

	public void setAspectRatio(float aspectRatio) {
		isProjectionChange = true;
		this.aspectRatio = aspectRatio;
	}

	public void setMaxMinPitch(float min, float max) {
		this.maxPitch = max;
		this.minPitch = min;
	}

	public GPVector3f getPosition() {
		return position.clone();
	}

	/**
	 * ����������������ҽ�ɫ��λ��
	 * 
	 * @param lookAtOffset
	 *            ������������ҽ�ɫ��λ��
	 */
	public void setLookOffset(GPVector3f lookAtOffset) {
		isViewChange = true;
		this.lookAtOffset = lookAtOffset;
	}

	/**
	 * ��������������ƶ������
	 * 
	 * @param direction
	 *            �ƶ����򼰴�С
	 */
	public void moveforwards(GPVector3f direction) {
		isViewChange = true;
		if (isOnGround)
			this.tempDirection = direction;
		position.addTo(tempDirection.rotate(tempYaw, 0, -1, 0));
	}

	/**
	 * �ƶ��������λ�ã������������
	 * 
	 * @param positionChange
	 *            �ƶ����򼰴�С
	 */
	public void moveAbsolut(GPVector3f positionChange) {
		isViewChange = true;
		position.addTo(positionChange);
	}

	// public void moveArround(Vector3f direction)
	// {
	// if (isOnGround)
	// this.tempDirection = direction;
	// position.addTo(tempDirection);
	// }

	/**
	 * ����������������ҽ�ɫ��λ��
	 * 
	 * @param offset
	 *            ������������ҽ�ɫ����ת��
	 */
	public void setOffsetToOrgin(GPVector3f offset) {
		isViewChange = true;
		this.offset = offset;
	}

	public float getYaw() {
		return yaw;
	}

	public void setOnGround(boolean isOnGround) {
		this.isOnGround = isOnGround;
		if (isOnGround)
			tempYaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public float getPitch() {
		return pitch;
	}

	// pitchΪ����,yawΪ���ң�
	public void setAngles(float yaw, float pitch) {
		isViewChange = true;
		if (pitch < minPitch)
			pitch = minPitch;
		if (pitch > maxPitch)
			pitch = maxPitch;
		this.pitch = pitch;
		if (isOnGround) {
			this.yaw = yaw;
			tempYaw = yaw;
		} else
			this.yaw = yaw;
	}

	public void rotate(float yawInc, float pitchInc) {
		isViewChange = true;
		this.yaw += yawInc;
		if (isOnGround)
			this.tempYaw = yaw;
		this.pitch += pitchInc;
		if (pitch < minPitch)
			pitch = minPitch;
		if (pitch > maxPitch)
			pitch = maxPitch;
	}

	/**
	 * �������
	 */
	public void setup() {
		// this.aspectRatio = GPRuntimeVaribles.SCREENWIDTH /
		// GPRuntimeVaribles.SCREENHEIGHT;
		// GPMatrixState.loadIdentity(GPMatrixState.PROJESTION_MODE);
		// GPMatrixState.Perspective(fieldOfView, aspectRatio, near, far);
		// GPMatrixState.loadIdentity(GPMatrixState.VIEW_MODE);
		// GPMatrixState.cRotatef(-pitch, 1, 0, 0);
		// GPMatrixState.cTranslatef(-offset.x, -offset.y, -offset.z);
		// GPMatrixState.cRotatef(-yaw, 0, 1, 0);
		// GPMatrixState.cTranslatef(-lookAtOffset.x, -lookAtOffset.y,
		// -lookAtOffset.z);
		// GPMatrixState.cTranslatef(-position.x, -position.y, -position.z);
		// GPMatrixState.loadIdentity(GPMatrixState.MODEL_MODE);

		if (isProjectionChange) {
			projectionMat.loadIdentity();
			projectionMat.perspective(fieldOfView, aspectRatio, near, far);
			isProjectionChange = false;
		}
		if (isViewChange) {
			viewMat.loadIdentity();
			viewMat.rotatef(-pitch, 1, 0, 0);
			viewMat.translatef(-offset.x, -offset.y, -offset.z);
			viewMat.rotatef(-yaw, 0, 1, 0);
			viewMat.translatef(-lookAtOffset.x, -lookAtOffset.y,
					-lookAtOffset.z);
			viewMat.translatef(-position.x, -position.y, -position.z);
			isViewChange = false;
		}
	}

	/**
	 * �������λ�ô��������ɫ��
	 * 
	 * @param programNum
	 *            ��ɫ��
	 */
	public void postPosition(GPShader shader) {
		cameraHandler = shader.getUniformLocation("cameraPosition");
		postionPost[0] = position.x;
		postionPost[1] = position.y;
		postionPost[2] = position.z;
		GLES20.glUniform3fv(cameraHandler, 1, postionPost, 0);
	}

	final float[] inVec = { 0, 0, -1, 1 };
	final float[] outVec = new float[4];
	final GPVector3f direction = new GPVector3f();

	public GPVector3f getDirection() {
		Matrix.multiplyMV(outVec, 0, GPMatrixState.getCameraMatrix(), 0, inVec,
				0);
		this.direction.set(outVec[0], outVec[1], outVec[2]);
		return direction;
	}
}
