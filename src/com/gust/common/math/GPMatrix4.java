package com.gust.common.math;

import android.opengl.Matrix;

public class GPMatrix4 {
	private float matrix[] = new float[16];

	public GPMatrix4(float[] matrix) {
		this.matrix = (float[]) matrix.clone();
	}

	public GPMatrix4() {
		loadIdentity();
	}

	public float[] getData() {
		return matrix;
	}

	public void orthof(float left, float right, float bottom, float top,
			float zNear, float zFar) {
		Matrix.orthoM(matrix, 0, left, right, bottom, top, zNear, zFar);
	}

	/**
	 * 设置透投影矩阵的参数
	 * 
	 * @param fieldOfView视角
	 *            （0~180）
	 * @param aspectRatio长宽比
	 * @param near近平面
	 *            （>0）
	 * @param far远平面
	 *            (>near)
	 */
	public void perspective(float fieldOfView, float aspectRatio, float near,
			float far) {
		float top = (float) (near * Math.tan(Math.toRadians(fieldOfView / 2)));
		float bottom = -top;
		float right = top * aspectRatio;
		float left = -right;
		Matrix.frustumM(matrix, 0, left, right, bottom, top, near, far);
	}

	/**
	 * 设置透视投影矩阵的参数
	 * 
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @param near
	 * @param far
	 */
	public void frustum(float left, float right, float bottom, float top,
			float near, float far) {
		Matrix.frustumM(matrix, 0, left, right, bottom, top, near, far);
	}

	/**
	 * 将矩阵置为单位矩阵
	 */
	public void loadIdentity() {
		Matrix.setIdentityM(matrix, 0);
	}

	/**
	 * 移动相机
	 * 
	 * @param fromX观察者的X坐标
	 * @param fromY观察者的Y坐标
	 * @param fromZ观察者的Z坐标
	 * @param toX观察点的X坐标
	 * @param toY观察点的Y坐标
	 * @param toZ观察点的Z坐标
	 * @param upX头顶方向
	 * @param upY头顶方向
	 * @param upZ头顶方向
	 */
	public void lookatf(float fromX, float fromY, float fromZ, float toX,
			float toY, float toZ, float upX, float upY, float upZ) {
		Matrix.setLookAtM(matrix, 0, fromX, fromY, fromZ, toX, toY, toZ, upX,
				upY, upZ);
	}

	public void rotatef(float angle, float x, float y, float z) {
		Matrix.rotateM(matrix, 0, angle, x, y, z);
	}

	public void translatef(float x, float y, float z) {
		Matrix.translateM(matrix, 0, x, y, z);
	}

	public void translatef(GPVector3f translate) {
		Matrix.translateM(matrix, 0, translate.x, translate.y, translate.z);
	}

	public void scalef(float x, float y, float z) {
		Matrix.scaleM(matrix, 0, x, y, z);
	}

	public GPMatrix4 mul(GPMatrix4 mat) {
		float[] result = new float[16];
		Matrix.multiplyMM(result, 0, matrix, 0, mat.matrix, 0);
		return new GPMatrix4(result);
	}

	public void mulWith(GPMatrix4 mat) {
		float[] result = new float[16];
		Matrix.multiplyMM(result, 0, matrix, 0, mat.matrix, 0);
		this.matrix = result;
	}

	public void transpose() {
		float[] result = new float[16];
		Matrix.transposeM(result, 0, matrix, 0);
		this.matrix = result;
	}

	public void invert() {
		float[] result = new float[16];
		Matrix.invertM(result, 0, matrix, 0);
		this.matrix = result;
	}

	public GPVector2f transform(GPVector2f point) {
		float[] data = { point.x, point.y, 0f, 1f };
		float[] result = new float[4];
		Matrix.multiplyMV(result, 0, matrix, 0, data, 0);
		return new GPVector2f(result[0], result[1]);
	}

	public GPVector3f transform(GPVector3f point) {
		float[] data = { point.x, point.y, point.z, 1f };
		float[] result = new float[4];
		Matrix.multiplyMV(result, 0, matrix, 0, data, 0);
		return new GPVector3f(result[0], result[1], result[2]);
	}

	public GPVector3f rotate(GPVector3f point) {
		float[] data = { point.x, point.y, point.z, 0f };
		float[] result = new float[4];
		Matrix.multiplyMV(result, 0, matrix, 0, data, 0);
		return new GPVector3f(result[0], result[1], result[2]);
	}

	public GPMatrix4 clone() {
		GPMatrix4 mat = new GPMatrix4(matrix);
		return mat;
	}
	
	public void setTransform(float width, float height, GPVector2f anthorPoint,
			GPVector2f pos, float rotationZ_X, float rotationZ_Y, float scaleX,
			float scaleY) {
		float x = pos.x;
		float y = pos.y;
		float z = 0;

		// if (_ignoreAnchorPointForPosition)
		// {
		// x += _anchorPointInPoints.x;
		// y += _anchorPointInPoints.y;
		// }

		// Rotation values
		// Change rotation code to handle X and Y
		// If we skew with the exact same value for both x and y then we're
		// simply just rotating
		float cx = 1, sx = 0, cy = 1, sy = 0;
		if (rotationZ_X != 0 || rotationZ_Y != 0) {
			float radiansX = -GPConstants.TO_RADIANS * rotationZ_X;
			float radiansY = -GPConstants.TO_RADIANS * rotationZ_Y;
			cx = (float) Math.cos(radiansX);
			sx = (float) Math.sin(radiansX);
			cy = (float) Math.cos(radiansY);
			sy = (float) Math.sin(radiansY);
		}

		// bool needsSkewMatrix = ( _skewX || _skewY );

		// optimization:
		// inline anchor point calculation if skew is not needed
		// Adjusted transform calculation for rotational skew
		if (!anthorPoint.equals(GPVector2f.zero)) {
			x += cy * -width * anthorPoint.x * scaleX + -sx * anthorPoint.y * -height * scaleY;
			y += sy * -width * anthorPoint.x * scaleX + cx * anthorPoint.y * -height * scaleY;
		}

		// Build Transform Matrix
		// Adjusted transform calculation for rotational skew
		matrix[0] = cy * scaleX;
		matrix[1] = sy * scaleX;
		matrix[2] = 0;
		matrix[3] = 0;
		matrix[4] = -sx * scaleY;
		matrix[5] = cx * scaleY;
		matrix[6] = 0;
		matrix[7] = 0;
		matrix[8] = 0;
		matrix[9] = 0;
		matrix[10] = 1;
		matrix[11] = 0;
		matrix[12] = x;
		matrix[13] = y;
		matrix[14] = z;
		matrix[15] = 1;

		// XXX
		// FIX ME: Expensive operation.
		// FIX ME: It should be done together with the rotationZ
		// if(_rotationY) {
		// Mat4 rotY;
		// Mat4::createRotationY(CC_DEGREES_TO_RADIANS(_rotationY), &rotY);
		// _transform = _transform * rotY;
		// }
		// if(_rotationX) {
		// Mat4 rotX;
		// Mat4::createRotationX(CC_DEGREES_TO_RADIANS(_rotationX), &rotX);
		// _transform = _transform * rotX;
		// }

		// XXX: Try to inline skew
		// If skew is needed, apply skew and then anchor point
		// if (needsSkewMatrix)
		// {
		// Mat4 skewMatrix(1, (float)tanf(CC_DEGREES_TO_RADIANS(_skewY)), 0, 0,
		// (float)tanf(CC_DEGREES_TO_RADIANS(_skewX)), 1, 0, 0,
		// 0, 0, 1, 0,
		// 0, 0, 0, 1);
		//
		// _transform = _transform * skewMatrix;
		//
		// // adjust anchor point
		// if (!_anchorPointInPoints.equals(Vec2::ZERO))
		// {
		// // XXX: Argh, Mat4 needs a "translate" method.
		// // XXX: Although this is faster than multiplying a vec4 * mat4
		// _transform.m[12] += _transform.m[0] * -_anchorPointInPoints.x +
		// _transform.m[4] * -_anchorPointInPoints.y;
		// _transform.m[13] += _transform.m[1] * -_anchorPointInPoints.x +
		// _transform.m[5] * -_anchorPointInPoints.y;
		// }
		// }
		//
		// if (_useAdditionalTransform)
		// {
		// _transform = _transform * _additionalTransform;
		// }
		//
		// _transformDirty = false;
		// }

	}
	
	public void setTransform(float width, float height, GPVector2f anthorPoint,
			GPVector2f pos, float rotation, float scaleX,
			float scaleY) {
		setTransform(width, height, anthorPoint, pos, rotation, rotation, scaleX, scaleY);
	}
}
