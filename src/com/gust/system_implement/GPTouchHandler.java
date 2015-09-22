package com.gust.system_implement;

import java.util.List;

import com.gust.system.GPInput.GPTouch;

import android.view.View.OnTouchListener;

public interface GPTouchHandler extends OnTouchListener {

	public boolean isTouchDown(int pointer);
	
	public int getTouchX(int pointer);
	
	public int getTouchY(int pointer);
	
	public List<GPTouch> getTouches();
	
	public void setTouchEnable(boolean touchEnable);
		

}
