package com.gust.system;

import com.gust.render_engine.base.GPGLScreen;

public interface GPGame {
	public GPInput getInput();
	
	public GPGraphics getGraphics();
	
//	public void setScreen(GPScreen screen);
//	
//	public GPScreen getCurrentScreen();
	
	public GPGLScreen getStartScreen();
	
	//public void setOrientation(boolean isLandscape);
	
	public void setTransparentBackgroudEnable();
	
	public void setTransparentBackgroudDisable();
}
