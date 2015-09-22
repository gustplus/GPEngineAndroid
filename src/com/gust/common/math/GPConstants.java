package com.gust.common.math;

public class GPConstants {
	public final static float TO_RADIANS = (1 / 180f) * (float) Math.PI;
	public final static float TO_DEGREES = (1 / (float) Math.PI) * 180;
	public final static int VERTICAL = 0; // ´¹Ö±
	public final static int HORIZONTAL = 1; // Ë®Æ½
	
	public final static float almostZero = 0.000001f;

	public static int abs(float num1, float num2) {
		float temp = Math.abs(num1);
		if (temp >= num2)
			return 1;
		else
			return -1;
	}

	public static float translateAngleInround(float angle) {
		while (angle > 360)
			angle -= 360;
		while (angle < 0)
			angle += 360;
		return angle;
	}

	public static float translateInBound(float temp, float min, float max) {
		if (temp > max) {
			temp = max;
		}
		if (temp < min) {
			temp = min;
		}
		
		return temp;
	}
}
