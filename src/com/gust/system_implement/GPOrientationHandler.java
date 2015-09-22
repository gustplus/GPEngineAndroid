package com.gust.system_implement;

import com.gust.common.math.GPConstants;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GPOrientationHandler implements SensorEventListener{
	private Sensor orientation;
	private float yaw;
	private float pitch;
	private float roll;
	private float range = 0.2f;
	private float preYaw;
	private float prePitch;
	private float preRoll;
	
	public GPOrientationHandler(Context context)
	{
		// TODO Auto-generated constructor stub
		SensorManager manager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		this.orientation = manager.getDefaultSensor(SensorManager.SENSOR_ORIENTATION);
		manager.registerListener(this, orientation, SensorManager.SENSOR_DELAY_GAME);
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event)
	{
		// TODO Auto-generated method stub
		preYaw = yaw;
		prePitch = pitch;
		preRoll = roll;
		yaw = event.values[0];
		pitch = event.values[1];
		roll = event.values[2];
	}
	
	public void setRange(float range){
		this.range = range;
	}
	
	public float[] getOrientation(){
		return new float[]{yaw*GPConstants.TO_DEGREES*range, pitch*GPConstants.TO_DEGREES*range, roll*GPConstants.TO_DEGREES*range};
	}
	
	public float[] getOrientationChange(){
		return new float[]{(yaw-preYaw)*GPConstants.TO_DEGREES*range,(pitch-prePitch)*GPConstants.TO_DEGREES*range,(roll-preRoll)*GPConstants.TO_DEGREES*range};
	}
}
