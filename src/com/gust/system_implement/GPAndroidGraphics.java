package com.gust.system_implement;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.gust.system.GPGraphics;
import com.gust.system.GPPixmap;

public class GPAndroidGraphics implements GPGraphics {
	Bitmap frameBuffer;
	AssetManager assets;
	Canvas canvas;
	Paint paint;
	// 资源矩形
	Rect srcRect = new Rect();
	// 目标矩形
	Rect dstRect = new Rect();

	public GPAndroidGraphics(AssetManager assets, Bitmap frameBuffer)
	{
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
	}

	public GPPixmap newPixmap(String fileName, PixmapFormat format)
	{
		// TODO Auto-generated method stub
		Config config = null;
		if (format == PixmapFormat.ARGB4444)
			config = Config.ARGB_4444;
		if (format == PixmapFormat.RGB565)
			config = Config.RGB_565;
		if (format == PixmapFormat.ARGB8888)
			config = Config.ARGB_8888;

		Options options = new Options();
		options.inPreferredConfig = config;

		InputStream inputStream = null;
		Bitmap bitmap = null;

		try {
			inputStream = assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(inputStream, null, options);
			if (bitmap == null)
				throw new RuntimeException("can't load bitmap from asset '"
						+ fileName);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		if (bitmap.getConfig() == Config.ARGB_4444)
			format = PixmapFormat.ARGB4444;
		else if (bitmap.getConfig() == Config.ARGB_8888)
			format = PixmapFormat.ARGB8888;
		else
			format = PixmapFormat.RGB565;
		return new GPAndroidPixmap(bitmap, format);

	}

	public void clear(int color)
	{
		// TODO Auto-generated method stub
		canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
				(color & 0xff));
	}

	public void drawPixel(int x, int y, int color)
	{
		// TODO Auto-generated method stub
		paint.setColor(color);
		canvas.drawPoint(x, y, paint);
	}

	public void drawLine(int x1, int y1, int x2, int y2, int color)
	{
		// TODO Auto-generated method stub
		paint.setColor(color);
		canvas.drawLine(x1, y1, x2, y2, paint);

	}

	public void drawRect(int x, int y, int width, int height, int color)
	{
		// TODO Auto-generated method stub
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
	}

	public void drawPixmap(GPPixmap pixmap, int x, int y, int srcX, int srcY,
			int SrcWidth, int srcHeight)
	{
		// TODO Auto-generated method stub
		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.bottom = srcY + srcHeight - 1;
		srcRect.right = srcX + SrcWidth - 1;

		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + SrcWidth - 1;
		dstRect.bottom = y + srcHeight - 1;

		canvas.drawBitmap(((GPAndroidPixmap) pixmap).bitmap, srcRect, dstRect,
				null);
	}

	public void drawPixmap(GPPixmap pixmap, int x, int y)
	{
		// TODO Auto-generated method stub
		canvas.drawBitmap(((GPAndroidPixmap) pixmap).bitmap, x, y, null);
	}

	public int getWidth()
	{
		// TODO Auto-generated method stub
		return frameBuffer.getWidth();
	}

	public int getHeight()
	{
		// TODO Auto-generated method stub
		return frameBuffer.getHeight();
	}

}
