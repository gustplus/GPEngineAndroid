package com.gust.game_entry3D;

import com.gust.common.math.GPVector3f;
import com.gust.physics3D.GPAABB;
import com.gust.render_engine.base.GPNode;
import com.gust.render_engine.base.GPSpatial;

public abstract class GPGameObject3D extends GPSpatial {
	public GPVector3f accelaration;
	public GPVector3f velocity;
	public GPNode object;
	public GPAABB AABB;
	public GPVector3f angles;   // x,y,z·Ö±ðÎªpitch,yaw,roll;

	public GPGameObject3D(GPVector3f position) {
		this.position = position.clone();
	}

	public GPGameObject3D(GPVector3f position, GPNode object) {
		this.position = position.clone();
		this.object = object;
		this.object.position = this.position;
		this.velocity = new GPVector3f();
		this.accelaration = new GPVector3f();
		this.AABB = object.getAABB();
	}

	public void setObject(GPNode object) {
		this.object = object;
		this.object.position = position;
		this.velocity = new GPVector3f();
		this.accelaration = new GPVector3f();
		this.AABB = object.getAABB();
	}

	public void setAABB(GPAABB AABB) {
		this.object.AABB = AABB;
		this.AABB = AABB;
	}

	public GPAABB getAABB() {
		return this.object.getAABB();
	}

	public void setAngleX(float angle) {
		object.angles.x = angle;
	}

	public void setAngleY(float angle) {
		object.angles.y = angle;
	}

	public void setAngleZ(float angle) {
		object.angles.z = angle;
	}
	
	public abstract void update(float deltaTime);
}
