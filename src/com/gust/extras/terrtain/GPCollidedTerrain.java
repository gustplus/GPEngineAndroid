package com.gust.extras.terrtain;


import com.gust.common.math.GPConstants;
import com.gust.common.math.GPVector2f;
import com.gust.common.math.GPVector3f;
import com.gust.game_entry3D.GPGameObject3D;

public class GPCollidedTerrain {
	protected float heights[][];
	protected GPTerrainHeightData data;
	protected float perWidth;
	protected float perHeight;
	protected GPVector2f rotate;
	protected int rowNum;
	protected int colNum;
	protected GPVector3f position;
	protected float maxRotation = 15f;

	public void getDatas(GPTerrain terrain) {
		this.data = terrain.getDatas();
		this.heights = this.data.heights;
		this.perHeight = data.perBound.y;
		this.perWidth = data.perBound.x;
		this.rotate = new GPVector2f();
		this.position = terrain.position.clone();

	}

	public void setMaxRotation(float maxRotation) {
		this.maxRotation = maxRotation;
	}
	
	public void setPosition(GPVector3f position){
		this.position = position.clone();
	}
	
	public void move(GPVector3f movement){
		this.position.subFrom(movement);
	}

	public void getSituation(GPGameObject3D object) {
		if (!isIn(object))
			return;
		GPVector3f position = object.position;
		float x = position.x - this.position.x;
		float y = position.z - this.position.z;

		colNum = (int) ((x + data.halfBound.x) / perWidth);
		rowNum = (int) ((y + data.halfBound.y) / perHeight);

		float pointX = (x + data.halfBound.x) % perWidth;
		float pointY = (y + data.halfBound.y) % perHeight;

		float result = pointY / (perWidth - pointX) * perWidth;
		if (result <= perHeight) {
			result = lower(pointX, pointY);
			rotate = rotateUpper(object);
		} else {
			result = upper(pointX, pointY);
			rotate = rotateLower(object);
		}
		object.position.y = result + data.offset.y
				- object.getAABB().getRight_Bottom_Behind().y;
	}

	public void getSituation(GPGameObject3D object, GPVector3f movement,
			GPVector3f direction) {
		GPVector3f tempPostion = object.position.add(movement);

		float x = tempPostion.x - this.position.x;
		float y = tempPostion.z - this.position.z;

		colNum = (int) ((x + data.halfBound.x) / perWidth);
		rowNum = (int) ((y + data.halfBound.y) / perHeight);

		float pointX = (x + data.halfBound.x) % perWidth;
		float pointY = (y + data.halfBound.y) % perHeight;
		GPVector2f rotation = new GPVector2f();
		float result = pointY / (perWidth - pointX) * perWidth;
		if (result <= perHeight) {
			result = lower(pointX, pointY);
			rotation = rotateUpper(object);
		} else {
			result = upper(pointX, pointY);
			rotation = rotateLower(object);
		}

//		if (movement.z > 0) {
//			rotation.x = -rotation.x;
//		}

		if (rotation.x < maxRotation && rotation.y < 25) {
			rotate = rotation;
			
			
		} else {
			movement = GPVector3f.zero.clone();
//			GPVector2f dir = new GPVector2f(direction.x, -direction.z);
//			float changeAngle = dir.angle() - 90;
			// float len = movement.
//			if (changeAngle > 0 && changeAngle < 90) {
//				movement = new GPVector3f(-1, 0, 1).mul(0.1f);
//			}
//			if (changeAngle > -90 && changeAngle <= 0) {
//				movement = new GPVector3f(1, 0, 0).mul(0.1f);
//			}
		}
		
		object.position.addTo(movement);
		object.position.y = result + data.offset.y
				- object.getAABB().getRight_Bottom_Behind().y;
	}

	public float getExpectHeight(GPGameObject3D object) {
		if (!isIn(object))
			return 0;
		GPVector3f position = object.position;
		float x = position.x - this.position.x;
		float y = position.z - this.position.z;

		colNum = (int) ((x + data.halfBound.x) / perWidth);
		rowNum = (int) ((y + data.halfBound.y) / perHeight);

		float pointX = (x + data.halfBound.x) % perWidth;
		float pointY = (y + data.halfBound.y) % perHeight;

		float result = pointY / (perWidth - pointX) * perWidth;
		if (result <= perHeight) {
			result = lower(pointX, pointY);
			rotate = rotateUpper(object);
		} else {
			result = upper(pointX, pointY);
			rotate = rotateLower(object);
		}
		return result + data.offset.y - object.getAABB().getRight_Bottom_Behind().y;
	}

	public GPVector2f getRotatation() {
		return rotate;
	}

	private GPVector2f rotateLower(GPGameObject3D object) {
		GPVector2f rotate = new GPVector2f();
		object.object.rotateOffsetFromCenter.y = object.getAABB().getRight_Bottom_Behind().y;
		float tanW = (heights[rowNum + 1][colNum] - heights[rowNum + 1][colNum + 1])
				/ perWidth;
		float tanH = (heights[rowNum][colNum + 1] - heights[rowNum + 1][colNum + 1])
				/ perHeight;
		rotate.set((float) Math.atan(tanH) * GPConstants.TO_DEGREES,
				-(float) Math.atan(tanW) * GPConstants.TO_DEGREES);
		return rotate;
	}

	private GPVector2f rotateUpper(GPGameObject3D object) {
		GPVector2f rotate = new GPVector2f();
		object.object.rotateOffsetFromCenter.y = object.getAABB().getRight_Bottom_Behind().y;
		float tanW = (heights[rowNum][colNum + 1] - heights[rowNum][colNum])
				/ perWidth;
		float tanH = (heights[rowNum + 1][colNum] - heights[rowNum][colNum])
				/ perHeight;
		rotate.set(-(float) Math.atan(tanH) * GPConstants.TO_DEGREES,
				(float) Math.atan(tanW) * GPConstants.TO_DEGREES);
		return rotate;
	}

	private float lower(float pointX, float pointY) {
		// TODO Auto-generated method stub
		float x1 = (1 - pointX / perWidth)
				* (heights[rowNum + 1][colNum] - heights[rowNum + 1][colNum + 1]);
		float x2 = (1 - pointY / perHeight)
				* (heights[rowNum][colNum + 1] - heights[rowNum + 1][colNum + 1]);
		return heights[rowNum + 1][colNum + 1] + (x2 + x1);
	}

	private float upper(float pointX, float pointY) {
		// TODO Auto-generated method stub
		float x1 = (pointX / perWidth)
				* (heights[rowNum][colNum + 1] - heights[rowNum][colNum]);
		float x2 = (pointY / perHeight)
				* (heights[rowNum + 1][colNum] - heights[rowNum][colNum]);
		return heights[rowNum][colNum] + (x1 + x2);
	}

	public boolean isIn(GPGameObject3D object) {
		// TODO Auto-generated method stub
		float x = object.position.x - this.position.x;
		float y = object.position.z - this.position.z;

		if (x > -data.halfBound.x && x < data.halfBound.x
				&& y > -data.halfBound.y && y < data.halfBound.y)
			return true;
		else
			return false;
	}
}
