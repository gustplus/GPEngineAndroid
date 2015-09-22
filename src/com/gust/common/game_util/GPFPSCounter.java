package com.gust.common.game_util;

import android.util.Log;
/**
 * 用于显示当前程序的运行速度（fps）
 * @author gustplus
 *
 */
public class GPFPSCounter {
	static long startTime = System.nanoTime();
	static int frame = 0;
	static int lastFPS = 0;
	
	/**
	 * 读取运行时的FPS
	 */
	public static void loadFPS(){
		frame++;
		if(System.nanoTime() - startTime > 1000000000){
			Log.d("FPS", "FPS : "+ frame);
			startTime = System.nanoTime();
			frame = 0;
		}
	}
	
	 /**
	  * 取得运行时的FPS
	  * @return int FPS
	  */
	public static int getFPS(){
		frame++;
		if(System.nanoTime() - startTime > 1000000000){
			lastFPS = frame;
			startTime = System.nanoTime();
			frame = 0;
			
		}
		return lastFPS;
	}
}
