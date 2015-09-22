package com.gust.common.math;

import java.util.HashSet;

import android.opengl.Matrix;
import android.util.FloatMath;

public class GPVector3f {
	private static final float[] matrix = new float[16];
	private static final float[] inVec = new float[4];
	private static final float[] outVec = new float[4];

	public static final GPVector3f zero = new GPVector3f();

	public float x, y, z;

	public GPVector3f() {
	}

	public GPVector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public GPVector3f(GPVector3f vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}

	public GPVector3f clone() {
		return new GPVector3f(x, y, z);
	}

	public GPVector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public GPVector3f set(GPVector3f vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
		return this;
	}

	public void addTo(GPVector3f vector) {
		this.x += vector.x;
		this.y += vector.y;
		this.z += vector.z;
	}

	public void addTo(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public void subFrom(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

	public void subFrom(GPVector3f vector) {
		this.x -= vector.x;
		this.y -= vector.y;
		this.z -= vector.z;
	}

	public void mulWith(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
	}

	public GPVector3f add(float x, float y, float z) {
		return new GPVector3f(this.x + x, this.y + y, this.z + z);
	}

	public GPVector3f add(GPVector3f vector) {
		return new GPVector3f(vector.x + x, vector.y + y, vector.z + z);
	}

	public GPVector3f sub(float x, float y, float z) {
		return new GPVector3f(this.x - x, this.y - y, this.z - z);
	}

	public GPVector3f sub(GPVector3f vector) {
		return new GPVector3f(x - vector.x, y - vector.y, z - vector.z);
	}

	public GPVector3f mul(float scalar) {
		return new GPVector3f(this.x * scalar, this.y * scalar, this.z * scalar);
	}

	public float len() {
		return FloatMath.sqrt(x * x + y * y + z * z);
	}

	public GPVector3f getNormalVector(GPVector3f v1, GPVector3f v2) {
		GPVector3f result = v1.crossMul(v2);
		result.normalize();
		return v2;

	}

	public static GPVector3f getAverageVector(HashSet<GPVector3f> temp) {
		GPVector3f v = new GPVector3f();
		for (GPVector3f v0 : temp) {
			v.addTo(v0);
		}
		return v;
	}

	public float dotMul(GPVector3f v) {
		float x = this.x * v.x;
		float y = this.y * v.y;
		float z = this.z * v.z;
		return x + y + z;
	}

	public GPVector3f crossMul(GPVector3f v) {
		float x = this.y * v.z - this.z * v.y;
		float y = this.z * v.x - this.x * v.z;
		float z = this.x * v.y - this.y * v.x;
		GPVector3f xV = new GPVector3f(1, 0, 0);
		GPVector3f yV = new GPVector3f(0, 1, 0);
		GPVector3f zV = new GPVector3f(0, 0, 1);
		GPVector3f result = xV.mul(x).add(yV.mul(y).add(zV.mul(z)));
		return result;
	}

	public GPVector3f normalize() {
		float len = len();
		if (len != 0) {
			x /= len;
			y /= len;
			z /= len;
		}
		return this;
	}

	public void rotateTo(float angle, float axisX, float axisY, float axisZ) {
		inVec[0] = x;
		inVec[1] = y;
		inVec[2] = z;
		inVec[3] = 1;
		Matrix.setIdentityM(matrix, 0);
		Matrix.rotateM(matrix, 0, angle, axisX, axisY, axisZ);
		Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);

		x = outVec[0];
		y = outVec[1];
		z = outVec[2];
	}

	public GPVector3f rotate(float angle, float axisX, float axisY, float axisZ) {
		inVec[0] = x;
		inVec[1] = y;
		inVec[2] = z;
		inVec[3] = 1;
		Matrix.setIdentityM(matrix, 0);
		Matrix.rotateM(matrix, 0, angle, axisX, axisY, axisZ);
		Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);

		return new GPVector3f(outVec[0], outVec[1], outVec[2]);
	}

	public float dist(GPVector3f vector) {
		float distX = x - vector.x;
		float distY = y - vector.y;
		float distZ = z - vector.z;
		return FloatMath.sqrt(distX * distX + distY * distY + distZ * distZ);
	}

	public float dist(float x, float y, float z) {
		float distX = this.x - x;
		float distY = this.y - y;
		float distZ = this.z - z;
		return FloatMath.sqrt(distX * distX + distY * distY + distZ * distZ);
	}

	public float distSquared(GPVector3f vector) {
		float distX = x - vector.x;
		float distY = y - vector.y;
		float distZ = z - vector.z;
		return distX * distX + distY * distY + distZ * distZ;
	}

	public float distSquared(float x, float y, float z) {
		float distX = this.x - x;
		float distY = this.y - y;
		float distZ = this.z - z;
		return distX * distX + distY * distY + distZ * distZ;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "x = " + x + ", y = " + y + ", z = " + z;
	}

	public boolean equals(GPVector3f other) {
		return (this.x == other.x && this.y == other.y && this.z == other.z);
	}
}
