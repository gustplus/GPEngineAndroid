package com.gust.system_implement;

import android.content.Context;

/**
 * 管理游戏与玩家的交互的类
 * @author gustplus
 *
 */
public class GPAndroidInteraction {
	GPVibrateHandler vibrateHandler;//设备震动处理单元
	public GPAndroidInteraction(Context context)
	{
		// TODO Auto-generated constructor stub
		vibrateHandler = new GPVibrateHandler(context);
	}
	
	/**
	 * 设置震动模式
	 * @param pattern震动模式
	 * @param repeat是否重复（0——no，1——yes）
	 */
	public void vibrate(long[] pattern,int repeat){
		vibrateHandler.vibrate(pattern, repeat);
	}
	
	/**
	 * 设置震动时间
	 * @param milliseconds震动时间（毫秒）
	 */
	public void vibrate(long milliseconds){
		vibrateHandler.vibrate(milliseconds);
	}
}
