package com.gust.action;

import com.gust.scene3D.GPSpatial;


public abstract class GPAction3D extends GPAction {
	protected GPSpatial actor;

	public void initWithComponent(GPSpatial object) {
		// TODO Auto-generated method stub
		actor = object;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public GPAction clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
