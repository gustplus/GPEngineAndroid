package com.gust.common.control;

//import java.util.ArrayList;
import java.util.List;

import com.gust.common.math.GPVector2f;
import com.gust.physics2D.Bound;
import com.gust.system.GPInput.GPTouch;

/**
 * 用于监听屏幕上手指的滑动信息
 * @author gustplus
 *
 */
public class GPDragManager {
	private int pointer = -1;			//符合要求的手指的id
	private GPVector2f firstPosition;
	private GPVector2f secondPosition;
	private GPVector2f dragVector;
	private Bound bound;				//监听的屏幕区域
	private boolean isTouchDown;
	private boolean isDrag;
//	private List<Vector2f> dragTrack;	//记录滑动轨迹
	
	public GPDragManager(Bound bound)
	{
		this.bound = bound;
		this.dragVector = new GPVector2f();
//		dragTrack = new ArrayList<Vector2f>();
	}

	/**
	 * 取得每一帧玩家的手指滑动信息
	 * 
	 * @param touchevents系统取得的触控信息
	 * @param removable是否取消该手指对其它控制的影响
	 * @return 玩家手指滑动的屏幕向量
	 */
	public GPVector2f getDragInfo(List<GPTouch> touchEvents, boolean removable)
	{
		int len = touchEvents.size();
		GPTouch event;
		GPVector2f dragDir = new GPVector2f();
		GPVector2f point;
		for (int i = 0; i < len; i++) {
			event = touchEvents.get(i);
			if (event != null) {
				point = new GPVector2f(event.x, event.y);
				if (event.type == GPTouch.TOUCH_DOWN
						&& bound.isPointIn(point)) {
//					dragTrack.clear();
					pointer = event.pointer;
					firstPosition = point;
					secondPosition = firstPosition.clone();
					if (removable) {
						touchEvents.remove(i);
						--len;
						--i;
					}
					isTouchDown = true;
					isDrag = false;
				}
				if (event.type == GPTouch.TOUCH_DRAGGED
						&& event.pointer == pointer) {
					isDrag = true;
					secondPosition = point;
					dragDir = secondPosition.sub(firstPosition);
//					dragDir.add(dragDir);
					firstPosition = secondPosition;
					if (removable) {
						touchEvents.remove(i);
						--len;
						--i;
					}
				}
				if (event.type == GPTouch.TOUCH_UP
						&& event.pointer == pointer) {
					secondPosition = point;
					dragDir = secondPosition.sub(firstPosition);
//					dragDir.add(dragDir);
					firstPosition = null;
					pointer = -1;
					if (removable) {
						touchEvents.remove(i);
						--len;
						--i;
					}
					isDrag = false;
					isTouchDown = false;
				}
			}
			
		}
		return dragDir;
	}
	
	/**
	 *  取得玩家的手指从按下到抬起的滑动信息
	 * @param touchEvents系统取得的触控信息
	 * @param removable是否取消该手指对其它控制的影响
	 * @return玩家手指滑动的屏幕向量
	 */
	 
	public GPVector2f getTotalDragInfo(List<GPTouch> touchEvents, boolean removable){
		int len = touchEvents.size();
		GPTouch event;
		GPVector2f point;
		for (int i = 0; i < len; i++) {
			event = touchEvents.get(i);
			if (event != null) {
				point = new GPVector2f(event.x, event.y);
				if (event.type == GPTouch.TOUCH_DOWN
						&& bound.isPointIn(point)) {
					pointer = event.pointer;
					firstPosition = point;
					secondPosition = firstPosition.clone();
//					dragVector.set(0, 0);
					if (removable) {
						touchEvents.remove(i);
						--len;
						--i;
					}
					isTouchDown = true;
					isDrag = false;
				}
				if (event.type == GPTouch.TOUCH_DRAGGED
						&& event.pointer == pointer) {
					isDrag = true;
					secondPosition = point;
					dragVector.addTo(secondPosition.sub(firstPosition));
					firstPosition = secondPosition;
					if (removable) {
						touchEvents.remove(i);
						--len;
						--i;
					}
				}
				if (event.type == GPTouch.TOUCH_UP
						&& event.pointer == pointer) {
					secondPosition = point;
					dragVector.addTo(secondPosition.sub(firstPosition));
					firstPosition = null;
					pointer = -1;
					if (removable) {
						touchEvents.remove(i);
						--len;
						--i;
					}
					isDrag = false;
					isTouchDown = false;
				}
			}
		}
		if(isTouchDown||isDrag){
			return new GPVector2f();
		}else{
			GPVector2f temp = dragVector.clone();
			dragVector.set(0, 0);
			return temp;
			}
	}
	
	/**
	 * 是否有手指按下
	 * @return boolean
	 */
	public boolean isTouchDown(){
		return isTouchDown;
	}
	
	/**
	 * 是否有手指在滑动
	 * @return boolean
	 */
	public boolean isDrag(){
		return isDrag;
	}
	
	/**
	 * 取得从手指按下到抬起间的滑动轨迹
	 * @return 滑动轨迹
	 */
//	public List<Vector2f> getDragTrack(){
//		return dragTrack;
//	}
	
}
