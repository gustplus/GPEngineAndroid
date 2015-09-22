package com.gust.system;

import com.gust.system.GPGraphics.PixmapFormat;

public interface GPPixmap {
	public int getWidth();
	
	public int getHeight();
	
	public PixmapFormat getFormat();
	
	public void dispose();
}
