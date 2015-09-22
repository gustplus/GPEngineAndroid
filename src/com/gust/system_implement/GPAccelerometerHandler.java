package com.gust.system_implement;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 加速度处理单元
 * @author gustplus
 *
 */
public class GPAccelerometerHandler implements SensorEventListener {

	private float accelX;
	private float accelY;
	private float accelZ;

	public GPAccelerometerHandler(Context context)
	{
		
		SensorManager sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0){
			Sensor accelerometer = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER)
					.get(0);
			sensorManager.registerListener(this, accelerometer,
					SensorManager.SENSOR_DELAY_GAME);
		}
	}

	/**
	 * 传感器参数改变时调用
	 */
	public void onSensorChanged(SensorEvent event)
	{
		// TODO Auto-generated method stub
		accelX = event.values[0];
		accelY = event.values[1];
		accelZ = event.values[2];
	}

	/**
	 * 传感器精度改变时调用
	 */
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * 取得X轴加速度
	 * @return float-X轴加速度
	 */
	public float getAccelX()
	{
		return accelX;
	}

	/**
	 * 取得Y轴加速度
	 * @return float-Y轴加速度
	 */
	public float getAccelY()
	{
		return accelY;
	}

	/**
	 * 取得Z轴加速度
	 * @return float-Z轴加速度
	 */
	public float getAccelZ()
	{
		return accelZ;
	}

}
