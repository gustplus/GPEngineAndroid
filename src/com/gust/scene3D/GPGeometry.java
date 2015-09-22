package com.gust.scene3D;

import com.gust.common.game_util.GPLogger;
import com.gust.common.math.GPVector3f;
import com.gust.physics3D.GPAABB;

public class GPGeometry extends GPSpatial {

	public GPDisplayObject3D object;

	public GPGeometry(GPVector3f position, GPDisplayObject3D object) {
		// TODO Auto-generated constructor stub
		super(position);
		this.object = object;
		if (object != null) {
			this.AABB = this.object.getAABB().clone();
		}
		parent = null;
	}

	// public void drawSelf(int primitiveMode) {
	// // TODO Auto-generated method stub
	// object.bind();
	// GPMatrixState.setShader(object.getShader());
	// GPMatrixState.setTotalMatrix();
	// object.getShader().setModelMatrix(this.worldTransform);
	// GPMatrixState.setCameraMatrix();
	// object.draw(primitiveMode);
	// }
	
	public void draw(int primitiveMode){
		object.bind();
		object.draw(primitiveMode);
	}

	@Override
	public void updateWorldBound() {
		// TODO Auto-generated method stub
		this.AABB = this.object.getAABB().transform(worldTransform);
	}

	@Override
	public GPAABB getBound() {
		// TODO Auto-generated method stub
		return this.AABB;
	}

	@Override
	public void removeChild(GPSpatial child) {
		// TODO Auto-generated method stub
		GPLogger.log("GPGeometry", "GPGeometry don't have child",
				GPLogger.Warning);
	}

	@Override
	public void removeChildByTag(int tag) {
		// TODO Auto-generated method stub
		GPLogger.log("GPGeometry", "GPGeometry don't have child",
				GPLogger.Warning);
	}
}