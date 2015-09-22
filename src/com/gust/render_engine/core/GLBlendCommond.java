package com.gust.render_engine.core;

public class GLBlendCommond implements GLCommond {
	int blennSrc;
	int blendDst;
	
	public GLBlendCommond(int blendSrc, int blendDst) {
		this.blendDst = blendDst;
		this.blennSrc = blendSrc;
	}
	
	public void excute() {
		GPOpenGLStateManager manager = GPOpenGLStateManager.getInstance();
		manager.enableBlend(true);
		manager.useBlendFunc(blennSrc, blendDst);
	}

	public void revoke() {
		GPOpenGLStateManager manager = GPOpenGLStateManager.getInstance();
		manager.enableBlend(false);

	}

}
