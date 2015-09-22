package com.gust.system_implement;

import android.graphics.Bitmap;

import com.gust.system.GPPixmap;
import com.gust.system.GPGraphics.PixmapFormat;

public class GPAndroidPixmap implements GPPixmap {
	Bitmap bitmap;
	PixmapFormat format;
	
	public GPAndroidPixmap(Bitmap bitmap, PixmapFormat format){
		this.bitmap = bitmap;
		this.format = format;
	}
	
	public int getWidth()
	{
		// TODO Auto-generated method stub
		return bitmap.getWidth();
	}

	public int getHeight()
	{
		// TODO Auto-generated method stub
		return bitmap.getHeight();
	}

	public PixmapFormat getFormat()
	{
		// TODO Auto-generated method stub
		return format;
	}

	public void dispose()
	{
		// TODO Auto-generated method stub
		bitmap.recycle();
		format = null;
	}

}
