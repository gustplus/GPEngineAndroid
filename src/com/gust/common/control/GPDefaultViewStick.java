package com.gust.common.control;

import java.util.List;

import com.gust.common.math.GPVector2f;
import com.gust.system.GPInput.GPTouch;
/**
 * 默认使用的视角控制器，控制区域为全屏
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
	 * @return Vector3f 表示镜头旋转的向量
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
					//因为当result.x为正数时，旋转方向为负角度
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
