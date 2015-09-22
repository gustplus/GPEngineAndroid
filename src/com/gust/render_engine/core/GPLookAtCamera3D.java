package com.gust.render_engine.core;

import com.gust.common.math.GPVector3f;

/**
 * �ϵ��ӽ��������
 * @author gustplus
 *
 */
public class GPLookAtCamera3D extends Camera{
	final GPVector3f position;			//���λ��
	final GPVector3f up;					//ͷ������������ȷ�����������
	final GPVector3f lookAt;				//�������Ŀ���
	float fieldOfView;
	float aspectRatio;					//��ͼ�ĳ����
	float near;							
	float far;
	
	/**
	 * 
	 * @param fieldOfView Y�᷽����ӽǴ�С��һ��Ϊ67��
	 * @param aspectRatio ��ͼ�ĳ����
	 * @param near ��ƽ����루�������0��
	 * @param far Զƽ����루�������0��
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
	 * ȡ���������Ŀ���
	 * @return Vector3f�������Ŀ���
	 */
	public GPVector3f getLookAt(){
		return lookAt;
	}
	
	/**
	 * ���������
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
