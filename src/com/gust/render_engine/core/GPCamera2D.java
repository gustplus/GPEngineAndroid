package com.gust.render_engine.core;

import java.util.List;

import android.opengl.GLES20;

import com.gust.common.math.GPVector2f;
import com.gust.render_engine.base.GPMatrixState;
import com.gust.render_engine.base.GPRuntimeVaribles;
import com.gust.system.GPInput.GPTouch;

/**
 * 用于2D游戏显示或GUI
 * 
 * @author gustplus
 * 
 */
public class GPCamera2D extends Camera {
	private GPVector2f position;
	private float frusumwidth;
	private float frusumheight;
	private float zoom;
	private float width;
	private float height;
	GPMatrixState matrices;
	
	public enum FixResolution{no_border, match};

	public GPCamera2D()
	{
		position = new GPVector2f();
		this.zoom = 1.0f;
		this.width = GPRuntimeVaribles.SCREENWIDTH;
		this.height = GPRuntimeVaribles.SCREENHEIGHT;
//		this.frusumwidth = width;
//		this.frusumheight = height;
	}
	
	public void setDesignResolutionSize(float frusumwidth, float frusumheight, FixResolution resolution){
		position.set(frusumwidth / 2, frusumheight / 2);
		this.frusumwidth = frusumwidth;
		this.frusumheight = frusumheight;
	}

	/**
	 * 用于搭建相机环境，调用后即可显示游戏环境
	 */
	public void setup()
	{
		this.width = GPRuntimeVaribles.SCREENWIDTH;
		this.height = GPRuntimeVaribles.SCREENHEIGHT;
//		this.frusumwidth = width;
//		this.frusumheight = height;
		projectionMat.loadIdentity();
		projectionMat.orthof(position.x - frusumwidth * zoom / 2, position.x
				+ frusumwidth * zoom / 2, position.y - frusumheight * zoom / 2,
				position.y + frusumheight * zoom / 2, -10, 100);
		viewMat.loadIdentity();
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
	}

	/**
	 * 用于处理触摸事件，针对各设备转换触摸事件，若需要采集触摸数据，则必须先调用此方法
	 * 
	 * @param touchEvents
	 */
	public void transformTouchs(List<GPTouch> touchEvents)
	{
		int len = touchEvents.size();
		GPTouch event;
		for (int i = 0; i < len; i++) {
			event = touchEvents.get(i);
			if (event != null) {
				GPVector2f touch = new GPVector2f(event.x, event.y);
				touchInWorld(touch);
				event.x = touch.x;
				event.y = touch.y;
			}
		}
	}

	public void moveCamera(GPVector2f to)
	{
		position.addTo(to);
	}

	public void moveCamera(float x, float y)
	{
		position.addTo(x, y);
	}

	public void setZoom(float zoom)
	{
		this.zoom = zoom;
	}

	public void setPosition(GPVector2f position)
	{
		this.position = position;
	}

	public void touchInWorld(GPVector2f touchPoint)
	{
			touchPoint.x = touchPoint.x / width * frusumwidth * zoom;
			touchPoint.y = (1 - touchPoint.y / height) * frusumheight * zoom;
			touchPoint.addTo(position);
			touchPoint.subFrom(frusumwidth * zoom / 2,
					frusumheight * zoom / 2);
	}

	public float getfrusumWidth()
	{
		// TODO Auto-generated method stub
		return frusumwidth;
	}

	public float getfrusumHeight()
	{
		// TODO Auto-generated method stub
		return frusumheight;
	}
}
