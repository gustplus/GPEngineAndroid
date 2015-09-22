package com.gust.system_implement;

import android.content.Context;

/**
 * ������Ϸ����ҵĽ�������
 * @author gustplus
 *
 */
public class GPAndroidInteraction {
	GPVibrateHandler vibrateHandler;//�豸�𶯴���Ԫ
	public GPAndroidInteraction(Context context)
	{
		// TODO Auto-generated constructor stub
		vibrateHandler = new GPVibrateHandler(context);
	}
	
	/**
	 * ������ģʽ
	 * @param pattern��ģʽ
	 * @param repeat�Ƿ��ظ���0����no��1����yes��
	 */
	public void vibrate(long[] pattern,int repeat){
		vibrateHandler.vibrate(pattern, repeat);
	}
	
	/**
	 * ������ʱ��
	 * @param milliseconds��ʱ�䣨���룩
	 */
	public void vibrate(long milliseconds){
		vibrateHandler.vibrate(milliseconds);
	}
}
