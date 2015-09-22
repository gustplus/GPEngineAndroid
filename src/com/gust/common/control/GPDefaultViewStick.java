package com.gust.common.control;

import java.util.List;

import com.gust.common.math.GPVector2f;
import com.gust.system.GPInput.GPTouch;
/**
 * Ĭ��ʹ�õ��ӽǿ���������������Ϊȫ��
 * @author gustplus
 *
 */
public class GPDefaultViewStick extends GPViewStick{
//	public static final int MODE_ARROUND = 0; 
//	public static final int MODE_CENTER = 1;
//	public int mode;

	public GPDefaultViewStick()
	{
		this.viewDirection = new GPVector2f(0, 0);
	}

	/**
	 * 
	 * @param touchEvents
	 * @return Vector3f ��ʾ��ͷ��ת������
	 */
	public GPVector2f controlView(List<GPTouch> touchEvents)
	{
		viewDirection.set(0, 0);
		int len = touchEvents.size();
		GPTouch event;
		for (int i = 0; i < len; i++) {
			event = touchEvents.get(i);if(event!= null){
			if (event.type == GPTouch.TOUCH_DOWN) {
				pointer = event.pointer;
				firstPosition = firstPosition.set(event.x, event.y);
				secondPosition = firstPosition.clone();
			}
			if (event.type == GPTouch.TOUCH_UP) {
				if (event.pointer == this.pointer) {
					pointer = -1;
				}
			}
			if (event.type == GPTouch.TOUCH_DRAGGED) {
				if (event.pointer == this.pointer) {
					secondPosition = secondPosition.set(event.x, event.y);
					GPVector2f result = secondPosition.sub(firstPosition);
					//��Ϊ��result.xΪ����ʱ����ת����Ϊ���Ƕ�
					viewDirection.set(-result.x,result.y);
					viewDirection.mulWith(20);
					viewDirection.mulWith(range);
					firstPosition = secondPosition;
				}
			}}
		}
		return viewDirection;
	}
}
