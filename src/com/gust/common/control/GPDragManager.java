package com.gust.common.control;

//import java.util.ArrayList;
import java.util.List;

import com.gust.common.math.GPVector2f;
import com.gust.physics2D.Bound;
import com.gust.system.GPInput.GPTouch;

/**
 * ���ڼ�����Ļ����ָ�Ļ�����Ϣ
 * @author gustplus
 *
 */
public class GPDragManager {
	private int pointer = -1;			//����Ҫ�����ָ��id
	private GPVector2f firstPosition;
	private GPVector2f secondPosition;
	private GPVector2f dragVector;
	private Bound bound;				//��������Ļ����
	private boolean isTouchDown;
	private boolean isDrag;
//	private List<Vector2f> dragTrack;	//��¼�����켣
	
	public GPDragManager(Bound bound)
	{
		this.bound = bound;
		this.dragVector = new GPVector2f();
//		dragTrack = new ArrayList<Vector2f>();
	}

	/**
	 * ȡ��ÿһ֡��ҵ���ָ������Ϣ
	 * 
	 * @param toucheventsϵͳȡ�õĴ�����Ϣ
	 * @param removable�Ƿ�ȡ������ָ���������Ƶ�Ӱ��
	 * @return �����ָ��������Ļ����
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
	 *  ȡ����ҵ���ָ�Ӱ��µ�̧��Ļ�����Ϣ
	 * @param touchEventsϵͳȡ�õĴ�����Ϣ
	 * @param removable�Ƿ�ȡ������ָ���������Ƶ�Ӱ��
	 * @return�����ָ��������Ļ����
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
	 * �Ƿ�����ָ����
	 * @return boolean
	 */
	public boolean isTouchDown(){
		return isTouchDown;
	}
	
	/**
	 * �Ƿ�����ָ�ڻ���
	 * @return boolean
	 */
	public boolean isDrag(){
		return isDrag;
	}
	
	/**
	 * ȡ�ô���ָ���µ�̧���Ļ����켣
	 * @return �����켣
	 */
//	public List<Vector2f> getDragTrack(){
//		return dragTrack;
//	}
	
}
