package com.gust.scene3D;

import java.util.ArrayList;

import com.gust.common.math.GPMatrix4;
import com.gust.physics3D.GPAABB;
import com.gust.physics3D.GPPlane;

public class GPCuller3D {
	private ArrayList<GPPlane> planes;

	public GPCuller3D() {
		planes = new ArrayList<GPPlane>(6);
	}

	public void addPlane(GPPlane plane) {
		planes.add(plane);
	}

	public void addPlane(GPPlane plane, String tag) {
		plane.setTag(tag);
		planes.add(plane);
	}

	public GPPlane getPlane(GPPlane plane, int index) {
		return planes.get(index);
	}

	public GPPlane getPlane(String tag) {
		int len = planes.size();
		for (int i = 0; i < len; ++i) {
			GPPlane tmp = planes.get(i);
			if (tmp.getTag().equals(tag)) {
				return tmp;
			}
		}
		return null;
	}

	public void removePlane(int index) {
		planes.remove(index);
	}

	public void removePlane(GPPlane plane) {
		planes.remove(plane);
	}

	public void removePlane(String tag) {
		int len = planes.size();
		for (int i = 0; i < len; ++i) {
			GPPlane tmp = planes.get(i);
			if (tmp.getTag().equals(tag)) {
				planes.remove(i);
			}
		}
	}

	public boolean cull(GPAABB aabb) {
		int len = planes.size();
		for (int i = 0; i < len; ++i) {
			GPPlane plane = planes.get(i);
			if (!plane.isPostive(aabb)) {
				return false;
			}
		}
		return true;
	}
	
	public void ratate(GPMatrix4 transform){
		int len = planes.size();
		for (int i = 0; i < len; ++i) {
			GPPlane plane = planes.get(i);
			plane.rotate(transform);
		}
	}

public void transform(GPMatrix4 transform){
	int len = planes.size();
	for (int i = 0; i < len; ++i) {
		GPPlane plane = planes.get(i);
		plane.transform(transform);
	}
}
}
