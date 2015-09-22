package com.gust.common.control;

import java.util.List;

import com.gust.common.math.GPVector2f;
import com.gust.system.GPCamera2d;
import com.gust.system.GPInput.GPTouch;

/**
 * 模拟FPS游戏的右摇杆，多用于控制视角（可用区域为右半屏幕）
 * 
 * @author gustplus
 * 
 */
public class GPFPSViewStick extends GPViewStick {
	// public static final int MODE_ARROUND = 0;
	// public static final int MODE_CENTER = 1;
	// public int mode;

	private GPCamera2d camera;

	public GPFPSViewStick(GPCamera2d camera)
	{
		// this.mode = mode;
		viewDirection = new GPVector2f();
		this.camera = camera;
	}

	public void setRange(float range)
	{
		this.range = range;
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
			event = touchEvents.get(i);
			if (event != null) {
				if (event.type == GPTouch.TOUCH_DOWN
						&& event.x >= camera.getfrusumWidth() / 2) {
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
						// 因为当result.x为正数时，旋转方向为负角度
						viewDirection.set(-result.x, result.y);
						viewDirection.mulWith(20);
						viewDirection.mulWith(range);
						firstPosition = secondPosition;
					}
				}
			}
		}
		return viewDirection;
	}
}
