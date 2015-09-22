package com.gust.action;

import com.gust.render_engine.base.GPSpatial;

public class GPFollowAction extends GPActionImmediate {

	public void follow(GPSpatial obj, GPSpatial follow){
		follow.position = obj.position;
	}
}
