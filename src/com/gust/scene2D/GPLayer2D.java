package com.gust.scene2D;

import java.util.ArrayList;

import com.gust.common.math.GPConstants;
import com.gust.common.math.GPVector2f;
import com.gust.physics2D.Bound_Rectangle;
import com.gust.system.GPInput.GPTouch;

public class GPLayer2D extends GPNode2D {
	private float origin_width;
	private float origin_height;

	public GPLayer2D(float x, float y, float width, float height) {
		super(x, y, width, height);
		touchChildren = true;
		origin_width = width;
		origin_height = height;
//		name = "Layer";
	}

	public void setAnthorPoint(GPVector2f anthorPoint) {
		if (anthorPoint.x > 1) {
			anthorPoint.x = 1;
		}
		if (anthorPoint.x < 0) {
			anthorPoint.x = 0;
		}
		if (anthorPoint.y > 1) {
			anthorPoint.y = 1;
		}
		if (anthorPoint.y < 0) {
			anthorPoint.y = 0;
		}
		this.anthorPoint.set(anthorPoint);
		worldIsCurrent = false;
		
//		postUpdateFlagToParent();
	}
	
	public void setAnthorPoint(float x, float y) {
		if (x > 1) {
			x = 1;
		}
		if (x < 0) {
			x = 0;
		}
		if (y > 1) {
			y = 1;
		}
		if (y < 0) {
			y = 0;
		}
		this.anthorPoint.set(x, y);
		worldIsCurrent = false;
		
//		postUpdateFlagToParent();
	}

	public void setcontentSize(Bound_Rectangle contentBound) {
		this.contentBound = contentBound;
		if(parent != null){
			parent.modelIsCurrent = false;
		}
	}

	public void setBoundBox(Bound_Rectangle bound) {
		this.bound = bound;
		if(parent != null){
			parent.modelIsCurrent = false;
		}
	}
	
	protected GPVector2f getWH() {
		return new GPVector2f(origin_width, origin_height);
	}

	protected void calculateBoundingBox() {
		if (!modelIsCurrent || !worldIsCurrent) {
			GPVector2f lowleft = new GPVector2f(0, 0);
			ArrayList<GPVector2f> points = new ArrayList<GPVector2f>(4);
			float[] vertices = new float[16];
			GPVector2f pointLL = worldTransform.transform(lowleft);
			vertices[0] = pointLL.x;
			vertices[1] = pointLL.y;
			vertices[2] = 0;
			points.add(pointLL);
			float width = origin_width * scaleX;
			float height = origin_height * scaleY;
			if (worldRotation < GPConstants.almostZero
					|| worldRotation > -GPConstants.almostZero) {
				GPVector2f pointLR = pointLL.add(width, 0);
				vertices[3] = pointLR.x;
				vertices[4] = pointLR.y;
				vertices[5] = 0;
				points.add(pointLR);

				GPVector2f pointTR = pointLR.add(0, height);
				vertices[6] = pointTR.x;
				vertices[7] = pointTR.y;
				vertices[8] = 0;
				points.add(pointTR);

				GPVector2f pointTL = pointTR.add(-width, 0);
				vertices[9] = pointTL.x;
				vertices[10] = pointTL.y;
				vertices[11] = 0;
				points.add(pointTL);
			} else {
				pointLL = worldTransform
						.transform(lowleft.add(origin_width, 0));
				vertices[3] = pointLL.x;
				vertices[4] = pointLL.y;
				vertices[5] = 0;
				points.add(pointLL);
				pointLL = worldTransform.transform(lowleft.add(origin_width,
						origin_height));
				vertices[6] = pointLL.x;
				vertices[7] = pointLL.y;
				vertices[8] = 0;
				points.add(pointLL);
				pointLL = worldTransform.transform(lowleft
						.add(0, origin_height));
				vertices[9] = pointLL.x;
				vertices[10] = pointLL.y;
				vertices[11] = 0;
				points.add(pointLL);
			}
			this.contentBound.createWithPoints(points);
			this.bound = this.contentBound.clone();

			int size = children.size();
			for (int i = 0; i < size; ++i) {
				this.bound.include(children.get(i).bound);
			}
			if (parent != null) {
				parent.modelIsCurrent = false;
			}
		}
	}

	@Override
	public void drawself() {
	}

	public boolean onTouchDown(GPTouch touch, float deltaTime) {
		return swallow;
	}

	public void onTouchDrag(GPTouch touch, float deltaTime) {
	}

	public void onTouchUp(GPTouch touch, float deltaTime) {
	}

	public void onTouchesDown(ArrayList<GPTouch> touches, float deltaTime) {
	}

	public void onTouchesDrag(ArrayList<GPTouch> touches, float deltaTime) {
	}

	public void onToucheshUp(ArrayList<GPTouch> touches, float deltaTime) {
	}

}
