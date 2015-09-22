package com.gust.render_engine.core;

import com.gust.common.math.GPVector3f;

public class GPParticle {
	GPVector3f position;
	GPVector3f velocity;
//	private int state;
	
	public final static int PARTICLE_STATE_LIVE = 0;
	public final static int PARTICLE_STATE_DEAD = 1;
	
	public GPParticle()
	{
		// TODO Auto-generated constructor stub
		position = new GPVector3f();
		velocity = new GPVector3f();
//		state = PARTICLE_STATE_DEAD;
	}
	
	public GPParticle(GPVector3f position, GPVector3f velocity){
		this.position = position;
		this.velocity = velocity;
//		this.state = PARTICLE_STATE_LIVE;
	}
	
	public void draw(){

	}
}
