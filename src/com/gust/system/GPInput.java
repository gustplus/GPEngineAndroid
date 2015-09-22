package com.gust.system;

import java.util.List;

public interface GPInput {
	public static class GPKeyEvent{
		public static final int KEY_DOWN = 0;
		public static final int KEY_UP = 1;
		
		public int type;
		public int keyCode;
		public char keyChar;
	}
	
	public static class GPTouch{
		public static final int TOUCH_DOWN = 0;
		public static final int TOUCH_UP = 1;
		public static final int TOUCH_DRAGGED = 2;
		
		public int type;
		public float x,y;
		public int pointer;
		
		public boolean equals(GPTouch o) {
			return pointer == o.pointer;
		}
	}
	
	public boolean isKeyPressed(int keyCode);
	
	public boolean isTouchDown(int pointer);
	
	public float getTouchX(int pointer);
	
	public float getTouchY(int pointer);
	
	public float getAccelX();
	
	public float getAccelY();
	
	public float getAccelZ();
	
	public List<GPKeyEvent> getKeyEvents();
	
	public List<GPTouch> getTouches();
	
	public float[] getOrientations();
	
	public float[] getOrientationChange();
	
	public void setTouchEnable(boolean touch);
	
//	public void touchInWorld(List<TouchEvent> touchEvents, Camera2D_20 guiCamera);
}
