package com.gust.system_implement;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * ���ٶȴ���Ԫ
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
	 * �����������ı�ʱ����
	 */
	public void onSensorChanged(SensorEvent event)
	{
		// TODO Auto-generated method stub
		accelX = event.values[0];
		accelY = event.values[1];
		accelZ = event.values[2];
	}

	/**
	 * ���������ȸı�ʱ����
	 */
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * ȡ��X����ٶ�
	 * @return float-X����ٶ�
	 */
	public float getAccelX()
	{
		return accelX;
	}

	/**
	 * ȡ��Y����ٶ�
	 * @return float-Y����ٶ�
	 */
	public float getAccelY()
	{
		return accelY;
	}

	/**
	 * ȡ��Z����ٶ�
	 * @return float-Z����ٶ�
	 */
	public float getAccelZ()
	{
		return accelZ;
	}

}
