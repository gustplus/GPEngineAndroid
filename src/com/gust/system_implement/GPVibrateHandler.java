package com.gust.system_implement;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;

public class GPVibrateHandler {
	Vibrator vibrator;
	public GPVibrateHandler(Context context)
	{
		// TODO Auto-generated constructor stub
		vibrator = (Vibrator)context.getSystemService(Activity.VIBRATOR_SERVICE);
	}
	
	public void vibrate(long[] pattern,int repeat){
		vibrator.vibrate(pattern, repeat);
	}
	
	public void vibrate(long milliseconds){
		vibrator.vibrate(milliseconds);
	}
}
