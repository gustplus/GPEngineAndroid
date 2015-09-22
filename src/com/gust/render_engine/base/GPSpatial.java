package com.gust.render_engine.base;

import com.gust.common.math.GPMatrix4;
import com.gust.common.math.GPVector3f;

public class GPSpatial {
	public GPVector3f position;
	public String tag;
	public GPVector3f angles;   // x,y,z·Ö±ðÎªpitch,yaw,roll;
	protected GPMatrix4 worldTransform;

	public GPSpatial(GPVector3f position) {
		this.position = position;
		this.angles = new GPVector3f();
	}

	public GPSpatial() {
		this.position = new GPVector3f();
		this.angles = new GPVector3f();
	}
	
}
