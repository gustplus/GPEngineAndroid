package com.gust.scene2D;

import java.util.ArrayList;

import com.gust.system.GPInput.GPTouch;

public interface GPTouchDelegate {
	public boolean onTouchDown(GPTouch touch, float deltaTime);
	public void onTouchDrag(GPTouch touch, float deltaTime);
	public void onTouchUp(GPTouch touch, float deltaTime);
	
	public void onTouchesDown(ArrayList<GPTouch> touches, float deltaTime);
	public void onTouchesDrag(ArrayList<GPTouch> touches, float deltaTime);
	public void onToucheshUp(ArrayList<GPTouch> touches, float deltaTime);
}
