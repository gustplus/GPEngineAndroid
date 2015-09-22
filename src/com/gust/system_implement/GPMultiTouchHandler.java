package com.gust.system_implement;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import com.gust.common.game_util.GPPool;
import com.gust.common.game_util.GPPool.PoolObjectFactory;
import com.gust.system.GPInput.GPTouch;

/**
 * 多点触摸处理单元
 * 
 * @author gustplus
 * 
 */
public class GPMultiTouchHandler implements GPTouchHandler {

	GPPool<GPTouch> touchEventPool;
	List<GPTouch> touchEventsBuffer = new ArrayList<GPTouch>();
	List<GPTouch> touchEvents = new ArrayList<GPTouch>();

	boolean[] isTouched = new boolean[20];
	int[] touchX = new int[20];
	int[] touchY = new int[20];

	float scaleX;
	float scaleY;

	private boolean touchEnable;

	public GPMultiTouchHandler(View view, float scaleX, float scaleY) {
		PoolObjectFactory<GPTouch> factory = new PoolObjectFactory<GPTouch>() {
			public GPTouch createObject() {
				return new GPTouch();
			}
		};
		touchEventPool = new GPPool<GPTouch>(factory, 100);
		view.setOnTouchListener(this);
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.touchEnable = true;
	}

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (touchEnable) {
			synchronized (this) {
				GPTouch touchEvent = null;
				int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
				int action = event.getAction() & MotionEvent.ACTION_MASK;
				int pointerID = event.getPointerId(pointerIndex);
				switch (action) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:
					while (touchEvent == null)
						touchEvent = touchEventPool.newObject();
					touchEvent.pointer = pointerID;
					touchEvent.type = GPTouch.TOUCH_DOWN;
					touchEvent.x = touchX[pointerID] = (int) (event
							.getX(pointerIndex) * scaleX);
					touchEvent.y = touchY[pointerID] = (int) (event
							.getY(pointerIndex) * scaleY);
					isTouched[pointerID] = true;
					touchEventsBuffer.add(touchEvent);
					break;

				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_POINTER_UP:
					while (touchEvent == null)
						touchEvent = touchEventPool.newObject();
					touchEvent.pointer = pointerID;
					touchEvent.type = GPTouch.TOUCH_UP;
					touchEvent.x = touchX[pointerID] = (int) (event
							.getX(pointerIndex) * scaleX);
					touchEvent.y = touchY[pointerID] = (int) (event
							.getY(pointerIndex) * scaleY);
					isTouched[pointerID] = true;
					touchEventsBuffer.add(touchEvent);
					break;

				case MotionEvent.ACTION_MOVE:
					int pointerCount = event.getPointerCount();
					for (int i = 0; i < pointerCount; i++) {
						pointerIndex = i;
						pointerID = event.getPointerId(pointerIndex);
						touchEvent = touchEventPool.newObject();
						while (touchEvent == null)
							touchEvent = touchEventPool.newObject();
						touchEvent.pointer = pointerID;
						touchEvent.type = GPTouch.TOUCH_DRAGGED;
						touchEvent.x = touchX[pointerID] = (int) (event
								.getX(pointerIndex) * scaleX);
						touchEvent.y = touchY[pointerID] = (int) (event
								.getY(pointerIndex) * scaleY);
						touchEventsBuffer.add(touchEvent);
					}
					break;
				}
			}
		}

		return true;
	}

	public boolean isTouchDown(int pointer) {
		synchronized (this) {
			if (pointer > 0 && pointer < 20)
				return isTouched[pointer];
			return false;
		}
	}

	public int getTouchX(int pointer) {
		synchronized (this) {
			if (pointer > 0 && pointer < 20)
				return touchX[pointer];
			return 0;
		}
	}

	public int getTouchY(int pointer) {
		synchronized (this) {
			if (pointer > 0 && pointer < 20)
				return touchY[pointer];
			return 0;
		}
	}

	public List<GPTouch> getTouches() {
		// TODO Auto-generated method stub
		synchronized (this) {
			int len = touchEvents.size();
			for (int i = 0; i < len; i++)
				touchEventPool.free(touchEvents.get(i));
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}

	public void setTouchEnable(boolean touchEnable) {
		// TODO Auto-generated method stub
		this.touchEnable = touchEnable;
	}

}
