package com.gust.physics2D;

import com.gust.common.math.GPConstants;
import com.gust.common.math.GPVector2f;

public class GPLine {
	private GPVector2f startPoint;
	private GPVector2f endPoint;
	
	private GPVector2f normal;
	
	public GPLine(GPVector2f startPoint) {
		this.startPoint = startPoint;
	}
	
	public GPLine(GPVector2f startPoint, GPVector2f endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}
	
	public void setEndPoint(GPVector2f endPoint){
		this.endPoint = endPoint;
	}
	
	public GPVector2f getEndPoint(){
		return endPoint;
	}
	
	public float distanceWithPiont(GPVector2f point){
		normal = getNormal();
		float distance = normal.dotMul(startPoint);
		distance = normal.dotMul(point) - distance;
		if(distance < GPConstants.almostZero && distance > -GPConstants.almostZero){
			return 0;
		}
		
		return distance;
	}
	
	public GPLine clone(){
		return new GPLine(startPoint, endPoint);
	}

	public GPVector2f getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(GPVector2f startPoint) {
		this.startPoint = startPoint;
	}
	
	public GPVector2f getNormal(){
		if(normal == null){
			normal = endPoint.sub(startPoint);
			normal.set(-normal.y, normal.x);
			normal.normalize();
		}
		return normal;
	}
}
