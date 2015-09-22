package com.gust.common.game_util;

import com.gust.render_engine.base.GPRuntimeVaribles;

import android.util.Log;

public class GPLogger {
	private static int avilable = 3;
	
	public static final int Debug = 3;
	public static final int Warning = 2;
	public static final int Error = 1;
	public static final int RUNTIME_VAR = 0;

	public static void log(String tag, String msg) {
		if (GPRuntimeVaribles.debug) {
			if (avilable >= Debug) {
				Log.e(tag, msg);
			}
		}
	}
	
	public static void setLogLevel(int priority){
		GPLogger.avilable = priority;
	}
	
	public static void log(String tag, String msg, int priority){
		if (GPRuntimeVaribles.debug) {
			if (avilable <= priority) {
				Log.e(tag, msg);
			}
		}
	}

}
