package com.gust.system;

public interface GPGraphics {
	public static enum PixmapFormat{
		ARGB8888,ARGB4444,RGB565
	}
	
	public GPPixmap newPixmap(String fileName,PixmapFormat format);
	
	public void clear(int color);
	
	public void drawPixel(int x, int y, int color);
	
	public void drawLine(int x1, int y1, int x2, int y2, int color);
	
	public void drawRect(int x, int y, int width, int height, int color);
	
	public void drawPixmap(GPPixmap pixmap, int x, int y, int srcX, int srcY, int SrcWidth, int srcHeight);
	
	public void drawPixmap(GPPixmap pixmap, int x, int y);
	
	public int getWidth();
	
	public int getHeight();
}
