package com.gust.common.math;

import com.gust.common.game_util.GPLogger;
import com.gust.physics2D.Bound_Rectangle;

public class GPTransform2D {
	private float[] rotation;
	private float translationX;
	private float translationY;
	private float scaleX;
	private float scaleY;

	private boolean isIdentity;
	private boolean isScale; // 仅仅射击缩放
	private boolean isRotate; // 仅仅涉及旋转
	private boolean isTranslate; // 仅仅涉及平移

	private boolean isUniformScale;

	public GPTransform2D() {
		rotation = new float[] { 1, 0, 0, 1 };
		translationX = 0;
		translationY = 0;
		scaleX = 1;
		scaleY = 1;
		isIdentity = true;
		isRotate = false;
		isScale = false;
		isTranslate = false;
		isUniformScale = true;
	}

	public void rotate(float angle) {
		if (angle == 0) {
			isRotate = false;
			rotation[0] = 1;
			rotation[1] = 0;
			rotation[2] = 0;
			rotation[3] = 1;
			if (!isScale && !isTranslate) {
				isIdentity = true;
			}
			return;
		}
		float radians = angle * GPConstants.TO_RADIANS;
		rotation[0] = (float) Math.cos(radians);
		rotation[1] = (float) Math.sin(radians);
		rotation[2] = -rotation[1];
		rotation[3] = rotation[0];
		isRotate = true;
		isIdentity = false;
	}

	public void setRotateData(float[] data) {
		this.rotation[0] = data[0];
		this.rotation[1] = data[1];
		this.rotation[2] = data[2];
		this.rotation[3] = data[3];
		isIdentity = false;
		isRotate = true;
	}

	public void translate(float x, float y) {
		this.translationX = x;
		this.translationY = y;
		if (x == 0 && y == 0) {
			isTranslate = false;
			if (!isScale && !isRotate) {
				isIdentity = true;
			}
		} else {
			isTranslate = true;
			isIdentity = false;
		}
	}

	public void translate(GPVector2f translation) {
		this.translationX = translation.x;
		this.translationY = translation.y;
		if (translation.x == 0 && translation.y == 0) {
			isTranslate = false;
			if (!isScale && !isRotate) {
				isIdentity = true;
			}
		} else {
			isTranslate = true;
			isIdentity = false;
		}
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
		if (scaleX == scaleY) {
			isUniformScale = true;
		} else {
			isUniformScale = false;
		}
		if (scaleX == 1) {
			isScale = false;
			if (!isTranslate && !isRotate) {
				isIdentity = true;
			}
		} else {
			isScale = true;
			isIdentity = false;
		}
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
		if (scaleX == scaleY) {
			isUniformScale = true;
		} else {
			isUniformScale = false;
		}
		if (scaleY == 1) {
			isScale = false;
			if (!isTranslate && !isRotate) {
				isIdentity = true;
			}
		} else {
			isScale = true;
			isIdentity = false;
		}
	}

	public GPTransform2D scale(float scaleX, float scaleY) {
		GPTransform2D tmp = new GPTransform2D();
		tmp.rotation[0] = rotation[0];
		tmp.rotation[1] = rotation[1];
		tmp.rotation[2] = rotation[2];
		tmp.rotation[3] = rotation[3];
		
		tmp.scaleX = scaleX;
		tmp.scaleY = scaleY;
		
		if (scaleX == scaleY) {
			isUniformScale = true;
		} else {
			isUniformScale = false;
		}
		
		tmp.translationX = translationX;
		tmp.translationY = translationY;
		return tmp;
	}

	public void scaleWith(float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		if (scaleX == scaleY) {
			isUniformScale = true;
		} else {
			isUniformScale = false;
		}
		if(scaleX == 1 && scaleY == 1){
			isScale = false;
		}else{
			isScale = true;
		}
	}

	public GPTransform2D product(GPTransform2D transform) {
		GPTransform2D tmp = new GPTransform2D();
		if (isIdentity) {
			return transform;
		}
		if (transform.isIdentity) {
			return this;
		}
		GPLogger.log("", "in");
//		if (!isTranslate && !transform.isTranslate) {
			if (isUniformScale) {
				tmp.rotation = matricesConcat(rotation, transform.rotation);
				float[] v = matrixConcatVector(rotation, new float[] {
						transform.translationX, transform.translationY });
				v[0] = v[0] * scaleX + translationX;
				v[1] = v[1] * scaleY + translationY;
				if (transform.isUniformScale) {
					float scale = scaleX * transform.scaleX;
					tmp.scaleWith(scale, scale);
				} else {
					tmp.scaleWith(scaleX * transform.scaleX, scaleY * transform.scaleY);
				}
				return tmp;
			}
//		}
		float[] m1 = new float[] { rotation[0], rotation[1], rotation[2],
				rotation[3] };
//		if (!isTranslate && isScale) {
			m1[0] = m1[0] * scaleX;
			m1[1] = m1[1] * scaleX;
			m1[2] = m1[2] * scaleY;
			m1[3] = m1[3] * scaleY;
//		}

		float[] m2 = new float[] { transform.rotation[0],
				transform.rotation[1], transform.rotation[2],
				transform.rotation[3] };
//		if (!isTranslate && isScale) {
			m2[0] = m2[0] * scaleX;
			m2[1] = m2[1] * scaleX;
			m2[2] = m2[2] * scaleY;
			m2[3] = m2[3] * scaleY;
//		}

		tmp.rotation = matricesConcat(m1, m2);
		float[] v = new float[2];
		v = matrixConcatVector(rotation, new float[] { transform.translationX,
				transform.translationY });
		tmp.translate(v[0] + translationX, v[1] + translationY);
		
		return tmp;
	}

	public void productWith(GPTransform2D transform) {
		if (isIdentity) {
			this.setRotateData(transform.rotation);
			this.scaleX = transform.scaleX;
			this.scaleY = transform.scaleY;
			this.translationX = transform.translationX;
			this.translationY = transform.translationY;
			this.isIdentity = transform.isIdentity;
			this.isRotate = transform.isRotate;
			this.isScale = transform.isScale;
			this.isTranslate = transform.isTranslate;
			this.isUniformScale = transform.isUniformScale;
		}
		if (transform.isIdentity) {
			return;
		}

//		if (!isTranslate && !transform.isTranslate) {
			if (isUniformScale) {
				rotation = matricesConcat(rotation, transform.rotation);
				float[] v = matrixConcatVector(rotation, new float[] {
						transform.translationX, transform.translationY });
				v[0] = v[0] * scaleX + translationX;
				v[1] = v[1] * scaleY + translationY;
				if (transform.isUniformScale) {
					scaleX = scaleY = scaleX * transform.scaleX;
				} else {
					scaleX = scaleX * transform.scaleX;
					scaleY = scaleY * transform.scaleY;
				}

				return;
			}
//		}

		float[] m1 = new float[] { rotation[0], rotation[1], rotation[2],
				rotation[3] };
		if (!isTranslate) {
			m1[0] = m1[0] * scaleX;
			m1[1] = m1[1] * scaleX;
			m1[2] = m1[2] * scaleY;	
			m1[3] = m1[3] * scaleY;
		}

		float[] m2 = new float[] { transform.rotation[0],
				transform.rotation[1], transform.rotation[2],
				transform.rotation[3] };
		if (!isTranslate) {
			m2[0] = m2[0] * scaleX;
			m2[1] = m2[1] * scaleX;
			m2[2] = m2[2] * scaleY;
			m2[3] = m2[3] * scaleY;
		}

		rotation = matricesConcat(m1, m2);
		float[] v = new float[2];
		v = matrixConcatVector(rotation, new float[] { transform.translationX,
				transform.translationY });
		translationX = v[0] + translationX;
		translationY = v[1] + translationY;
	}

	public GPVector2f transform(GPVector2f point) {
		GPVector2f tmp = point.mul(scaleX, scaleY);
		float x = rotation[0] * tmp.x + rotation[2] * tmp.y + translationX;
		float y = rotation[1] * tmp.x + rotation[3] * tmp.y + translationY;
//		float x = point.x * scaleX;
//		float y = point.y * scaleY;
		tmp.x = x;
		tmp.y = y;
		return tmp;
	}

	public Bound_Rectangle transformRect(Bound_Rectangle rect) {
		float top = rect.lowerLeft.y + rect.height;
		float left = rect.lowerLeft.x;
		float right = rect.lowerLeft.x + rect.width;
		float bottom = rect.lowerLeft.y;

		GPVector2f topLeft = new GPVector2f(top, left);
		GPVector2f topRight = new GPVector2f(top, right);
		GPVector2f bottomLeft = new GPVector2f(bottom, left);
		GPVector2f bottomRight = new GPVector2f(bottom, right);

		this.transform(topLeft);
		this.transform(topRight);
		this.transform(bottomLeft);
		this.transform(bottomRight);

		float minX = Math.min(Math.min(topLeft.x, topRight.x),
				Math.min(bottomLeft.x, bottomRight.x));
		float maxX = Math.max(Math.max(topLeft.x, topRight.x),
				Math.max(bottomLeft.x, bottomRight.x));
		float minY = Math.min(Math.min(topLeft.y, topRight.y),
				Math.min(bottomLeft.y, bottomRight.y));
		float maxY = Math.max(Math.max(topLeft.y, topRight.y),
				Math.max(bottomLeft.y, bottomRight.y));

		rect.lowerLeft.set(minX, minY);
		rect.width = maxX - minX;
		rect.height = maxY - minY;
		return rect;
	}

	private static float[] matricesConcat(float[] m1, float[] m2) {
		float[] rotation = new float[4];
		rotation[0] = m1[0] * m2[0] + m1[2] * m2[1];
		rotation[1] = m1[1] * m2[0] + m1[3] * m2[1];
		rotation[2] = m1[0] * m2[2] + m1[2] * m2[3];
		rotation[3] = m1[1] * m2[2] + m1[3] * m2[3];
		return rotation;
	}

	private static float[] matrixConcatVector(float[] m, float[] v) {
		float[] rV = new float[2];
		rV[0] = m[0] * v[0] + m[2] * v[1];
		rV[0] = m[1] * v[0] + m[3] * v[1];
		return rV;
	}

	public GPTransform2D clone() {
		GPTransform2D tmp = new GPTransform2D();
		tmp.setRotateData(rotation);
		tmp.translationX = translationX;
		tmp.translationY = translationY;
		tmp.scaleX = scaleX;
		tmp.scaleY = scaleY;
		tmp.isIdentity = isIdentity;
		tmp.isRotate = isRotate;
		tmp.isScale = isScale;
		tmp.isTranslate = isTranslate;
		return tmp;
	}
	
	public GPMatrix4 toMatrix(){
		GPMatrix4 matrix = new GPMatrix4(new float[]{rotation[0] * scaleX, rotation[1] * scaleX, 0, 0, 
													 rotation[2] * scaleY, rotation[3] * scaleY, 0, 0, 
													 0,                  0,                      0, 0, 
													 translationX,         translationY,         0, 1});
		return matrix;
	}
}
