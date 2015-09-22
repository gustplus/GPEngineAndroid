package com.gust.system_implement;

import java.util.List;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

import com.gust.system.GPInput;

/**
 * ȡ�ó����û�����������,�������̣����أ����ٶȴ����������򴫸��� 
 * @author gustplus
 *
 */
public class GPAndroidInput implements GPInput {
	GPAccelerometerHandler accelHandler;
	GPKeyboardHandler keyboardHandler;
	GPTouchHandler touchHandler;
	GPOrientationHandler orientationHandler;

	/**
	 * @param context
	 * @param view
	 * @param scaleX���űȣ�Open GL����Ϊ1���ɣ�
	 * @param scaleY���űȣ�Open GL����Ϊ1���ɣ�
	 */
	public GPAndroidInput(Context context, View view, float scaleX, float scaleY)
	{
		accelHandler = new GPAccelerometerHandler(context);
		keyboardHandler = new GPKeyboardHandler(view);
		orientationHandler = new GPOrientationHandler(context);
		if (Integer.parseInt(VERSION.SDK) < 5)
			;
		else
			touchHandler = new GPMultiTouchHandler(view, scaleX, scaleY);
	}

	/**
	 * �ж϶�Ӧ��ŵİ����Ƿ���
	 * @param keyCode
	 */
	public boolean isKeyPressed(int keyCode)
	{
		// TODO Auto-generated method stub
		return keyboardHandler.isKeyPressed(keyCode);
	}

	/**
	 * �ж϶�Ӧ��ŵ���ָ�Ƿ���
	 * @param pointer
	 */
	public boolean isTouchDown(int pointer)
	{
		// TODO Auto-generated method stub
		return touchHandler.isTouchDown(pointer);
	}

	public float getTouchX(int pointer)
	{
		// TODO Auto-generated method stub
		return touchHandler.getTouchX(pointer);
	}

	public float getTouchY(int pointer)
	{
		// TODO Auto-generated method stub
		return touchHandler.getTouchY(pointer);
	}

	public float getAccelX()
	{
		// TODO Auto-generated method stub
		return accelHandler.getAccelX();
	}

	public float getAccelY()
	{
		// TODO Auto-generated method stub
		return accelHandler.getAccelY();
	}

	public float getAccelZ()
	{
		// TODO Auto-generated method stub
		return accelHandler.getAccelZ();
	}

	public List<GPKeyEvent> getKeyEvents()
	{
		// TODO Auto-generated method stub
		return keyboardHandler.getKeyEvents();
	}

	public List<GPTouch> getTouches()
	{
		// TODO Auto-generated method stub
		return touchHandler.getTouches();
	}
	
	public float[] getOrientations(){
		return orientationHandler.getOrientation();
	}
	
	public float[] getOrientationChange(){
		return orientationHandler.getOrientationChange();
	}

	public void setTouchEnable(boolean touch) {
		// TODO Auto-generated method stub
		touchHandler.setTouchEnable(touch);
	}
}
