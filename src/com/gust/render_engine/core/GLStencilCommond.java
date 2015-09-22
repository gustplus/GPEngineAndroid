package com.gust.render_engine.core;

import com.gust.common.game_util.GPLogger;
import com.gust.physics2D.Bound_Rectangle;

public class GLStencilCommond implements GLCommond {
	private Bound_Rectangle stencilRect;
	private boolean shouldCal = true;
	
	public GLStencilCommond(Bound_Rectangle rect) {
		this.stencilRect = rect.clone();
	}
	
	public void excute() {
		GPOpenGLStateManager manager = GPOpenGLStateManager.getInstance();
		if(shouldCal){
			stencilRect.intersectWith(manager.getScissorRect());
			shouldCal = false;
		}
		manager.enableScissor(true);
		manager.scissor(stencilRect);
	}

	public void revoke() {
		GPOpenGLStateManager manager = GPOpenGLStateManager.getInstance();
		manager.enableScissor(false);
	}

}
