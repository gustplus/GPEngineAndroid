package com.gust.common.math;

import android.util.FloatMath;

public class GPVector2f {
	public float x;
	public float y;
	public static final GPVector2f zero = new GPVector2f();

	private float len = -1;

	public GPVector2f() {
		this.x = 0;
		this.y = 0;
	}

	public GPVector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public GPVector2f(GPVector2f other) {
		this.x = other.x;
		this.y = other.y;
	}

	public GPVector2f clone() {
		GPVector2f clone = new GPVector2f(x, y);
		clone.len = len;
		return clone;
	}

	public GPVector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		this.len = -1;
		return this;
	}

	public GPVector2f set(GPVector2f other) {
		this.x = other.x;
		this.y = other.y;
		this.len = -1;
		return this;
	}

	public void addTo(float x, float y) {
		this.x += x;
		this.y += y;
		this.len = -1;
	}

	public GPVector2f add(float x, float y) {
		return new GPVector2f(this.x + x, this.y + y);
	}

	public void addTo(GPVector2f other) {
		this.x += other.x;
		this.y += other.y;
		this.len = -1;
	}

	public GPVector2f add(GPVector2f other) {
		return new GPVector2f(this.x + other.x, this.y + other.y);
	}

	public void subFrom(float x, float y) {
		this.x -= x;
		this.y -= y;
		this.len = -1;
	}

	public GPVector2f sub(float x, float y) {
		return new GPVector2f(this.x - x, this.y - y);
	}

	public void subFrom(GPVector2f other) {
		this.x -= other.x;
		this.y -= other.y;
		this.len = -1;
	}

	public GPVector2f sub(GPVector2f other) {
		return new GPVector2f(this.x - other.x, this.y - other.y);
	}

	public GPVector2f mul(float rate) {
		return new GPVector2f(this.x * rate, this.y * rate);
	}

	public void mulWith(float rate) {
		this.x *= rate;
		this.y *= rate;
		len = len() * rate;
	}

	public GPVector2f mul(GPVector2f rate) {
		return new GPVector2f(this.x * rate.x, this.y * rate.y);
	}

	public GPVector2f mul(float x, float y) {
		return new GPVector2f(this.x * x, this.y * y);
	}

	public float dotMul(GPVector2f point) {
		return this.x * point.x + this.y * point.y;
	}

	public void mulWith(GPVector2f rate) {
		this.x *= rate.x;
		this.y *= rate.y;
		this.len = -1;
	}

	public void mulWith(float x, float y) {
		this.x *= x;
		this.y *= y;
		this.len = -1;
	}

	public float len() {
		if (len == -1) {
			len = FloatMath.sqrt(x * x + y * y);
		}
		return len;
	}

	public float angle() {
		float rad = (float) Math.atan2(this.y, this.x);
		float angle = rad * GPConstants.TO_DEGREES;
		return GPConstants.translateAngleInround(angle);
	}

	public GPVector2f rotate(float angle) {
		float rad = angle * GPConstants.TO_RADIANS;
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);
		float newX = this.x * cos - this.y * sin;
		float newY = this.x * sin + this.y * cos;

		this.x = newX;
		this.y = newY;

		return this;
	}

	public float dist(GPVector2f to) {
		float distX = this.x - to.x;
		float distY = this.y - to.y;
		return FloatMath.sqrt(distX * distX + distY * distY);
	}

	public float dist(float x, float y) {
		float distX = this.x - x;
		float distY = this.y - y;
		return FloatMath.sqrt(distX * distX + distY * distY);
	}

	public float distSquared(GPVector2f to) {
		float distX = this.x - to.x;
		float distY = this.y - to.y;
		return distX * distX + distY * distY;
	}

	public float distSquared(float x, float y) {
		float distX = this.x - x;
		float distY = this.y - y;
		return distX * distX + distY * distY;
	}

	public GPVector2f normalize() {
		float len = len();
		if (len != 0) {
			this.x /= len;
			this.y /= len;
		}
		return this;
	}

	public GPVector2f velocityToSpeed() {
		float x1 = x > 0 ? x : -x;
		float y1 = y > 0 ? y : -y;
		return new GPVector2f(x1, y1);
	}

	public GPVector2f getVerticalVector() {
		return new GPVector2f(y, -x).normalize();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "x = " + x + ", y = " + y;
	}
}
