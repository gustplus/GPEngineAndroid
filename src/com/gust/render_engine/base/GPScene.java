package com.gust.render_engine.base;


import com.gust.common.math.GPVector3f;

public class GPScene extends GPNode {
	
	public GPScene(GPVector3f position)
	{
		// TODO Auto-generated constructor stub
		super(position);
	}
	
	public GPScene(){
		this(new GPVector3f(0,0,0));
	}

}
