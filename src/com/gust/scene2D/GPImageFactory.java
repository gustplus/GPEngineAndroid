package com.gust.scene2D;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 用于生成位图
 * 
 * @author gustplus
 * 
 */
public class GPImageFactory {
	Canvas canvas;
	Bitmap bitmap;
	int width;
	int height;
	Paint paint;

	/**
	 * 根据参数生成位图模版供绘制
	 * @param width位图的宽度,必须为2^x
	 * @param height位图的高度必须为2^x
	 * @param RGBA位图模版的背景颜色
	 */
	public void setup(int width, int height, float RGBA[])
	{
		// TODO Auto-generated constructor stub
		this.width = width;
		this.height = height;
		int colors[] = new int[4];
		colors[0] = (int) (RGBA[0] * 255);
		colors[1] = (int) (RGBA[1] * 255);
		colors[2] = (int) (RGBA[2] * 255);
		colors[3] = (int) (RGBA[3] * 255);
		bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		canvas.drawARGB(colors[3], colors[0], colors[1], colors[2]);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	/**
	 * 根据参数生成位图模版供绘制，背景颜色为透明色
	 * @param width位图的宽度
	 * @param height位图的高度
	 */
	public void setup(int width, int height)
	{
		// TODO Auto-generated constructor stub
		this.width = width;
		this.height = height;
		bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		canvas.drawARGB(0, 1, 1, 1);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	/**
	 * 根据参数生成位图模版供绘制，背景为指定的位图
	 * @param width生成的位图的宽度
	 * @param height生成的位图的高度
	 * @param bitmap作为背景的位图
	 */
	public void setup(int width, int height, Bitmap bitmap)
	{
		// TODO Auto-generated constructor stub
		this.width = width;
		this.height = height;
		canvas = new Canvas(bitmap);
	}

	/**
	 * 绘制文字
	 * @param text绘制的文本内容
	 * @param RGBA文本的颜色
	 * @param textSize文本的字体大小
	 * @param centerX文本绘制的中心位置
	 * @param centerY文本绘制的中心位置
	 */
	public void drawText(String text, float RGBA[], float textSize,
			float centerX, float centerY)
	{

		int colors[] = new int[4];
		colors[0] = (int) (RGBA[0] * 255);
		colors[1] = (int) (RGBA[1] * 255);
		colors[2] = (int) (RGBA[2] * 255);
		colors[3] = (int) (RGBA[3] * 255);
		paint.setARGB(colors[3], colors[0], colors[1], colors[2]);
		Rect bounds = new Rect();
		paint.setTextSize(textSize);
		paint.getTextBounds(text, 0, text.length(), bounds);
		int textWidth = bounds.width();
		int textHeight = bounds.height();
		canvas.drawText(text, centerX - textWidth / 2,
				centerY + textHeight / 2, paint);
	}

	/**
	 * 在图片中心绘制文字
	 * @param text绘制的文本内容
	 * @param RGBA文本的颜色
	 * @param textSize文本的字体大小
	 */
	public void drawText(String text, float RGBA[], float textSize)
	{

		int colors[] = new int[4];
		colors[0] = (int) (RGBA[0] * 255);
		colors[1] = (int) (RGBA[1] * 255);
		colors[2] = (int) (RGBA[2] * 255);
		colors[3] = (int) (RGBA[3] * 255);
		paint.setARGB(colors[3], colors[0], colors[1], colors[2]);
		Rect bounds = new Rect();
		paint.setTextSize(textSize);
		paint.getTextBounds(text, 0, text.length(), bounds);
		int textWidth = bounds.width();
		int textHeight = bounds.height();
		canvas.drawText(text, width / 2 - textWidth / 2, height / 2
				+ textHeight / 2, paint);
	}

	/**
	 * 绘制位图
	 * @param bitmap位图资源
	 * @param centerX位图绘制的中心位置
	 * @param centerY位图绘制的中心位置
	 */
	public void drawBitmap(Bitmap bitmap, int centerX, int centerY)
	{
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Rect dst = new Rect(centerX - width / 2, centerY - height / 2, centerX
				+ width / 2, centerY + height / 2);
		canvas.drawBitmap(bitmap, null, dst, this.paint);
	}

	/**
	 * 在控件上添加位图
	 * @param bitmap位图
	 * @param distLeft位图的目标绘制区域的左边坐标
	 * @param distTop位图的目标绘制区域的上边坐标
	 * @param srcLeft位图资源的左边坐标
	 * @param srcTop位图资源的上边坐标
	 * @param SrcWidth位图宽度
	 * @param srcHeight位图高度
	 */
	public void drawPartBitmap(Bitmap bitmap, int distLeft, int distTop,
			int srcLeft, int srcTop, int SrcWidth, int srcHeight)
	{
		Rect srcRect = new Rect();
		srcRect.left = srcLeft;
		srcRect.top = srcTop;
		srcRect.bottom = srcTop + srcHeight - 1;
		srcRect.right = srcLeft + SrcWidth - 1;

		Rect dstRect = new Rect();
		dstRect.left = distLeft;
		dstRect.top = distTop;
		dstRect.right = distLeft + SrcWidth - 1;
		dstRect.bottom = distTop + srcHeight - 1;

		canvas.drawBitmap(bitmap, srcRect, dstRect, this.paint);
	}

	/**
	 * 绘制矩形
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 * @param RGBA
	 */
	public void drawRect(int left, int top, int width, int height, float RGBA[])
	{
		int colors[] = new int[4];
		colors[0] = (int) (RGBA[0] * 255);
		colors[1] = (int) (RGBA[1] * 255);
		colors[2] = (int) (RGBA[2] * 255);
		colors[3] = (int) (RGBA[3] * 255);
		paint.setARGB(colors[3], colors[0], colors[1], colors[2]);
		canvas.drawRect(left, top, left + width - 1, top + height - 1, paint);
	}

	/**
	 * 绘制点
	 * 
	 * @param x
	 * @param y
	 * @param RGBA
	 */
	public void drawPixel(int x, int y, float RGBA[])
	{
		// TODO Auto-generated method stub
		int colors[] = new int[4];
		colors[0] = (int) (RGBA[0] * 255);
		colors[1] = (int) (RGBA[1] * 255);
		colors[2] = (int) (RGBA[2] * 255);
		colors[3] = (int) (RGBA[3] * 255);
		paint.setARGB(colors[3], colors[0], colors[1], colors[2]);
		canvas.drawPoint(x, y, paint);
	}

	/**
	 * 绘制线
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param RGBA
	 */
	public void drawLine(int x1, int y1, int x2, int y2, float RGBA[])
	{
		// TODO Auto-generated method stub
		int colors[] = new int[4];
		colors[0] = (int) (RGBA[0] * 255);
		colors[1] = (int) (RGBA[1] * 255);
		colors[2] = (int) (RGBA[2] * 255);
		colors[3] = (int) (RGBA[3] * 255);
		paint.setARGB(colors[3], colors[0], colors[1], colors[2]);
		canvas.drawLine(x1, y1, x2, y2, paint);
	}

	/**
	 * 清除图片
	 * 
	 * @param RGBA清除所用颜色
	 */
	public void clear(float RGBA[])
	{
		// TODO Auto-generated method stub
		int colors[] = new int[4];
		colors[0] = (int) (RGBA[0] * 255);
		colors[1] = (int) (RGBA[1] * 255);
		colors[2] = (int) (RGBA[2] * 255);
		colors[3] = (int) (RGBA[3] * 255);
		canvas.drawARGB(colors[3], colors[0], colors[1], colors[2]);
	}

	/**
	 * 取得生成的位图
	 * 
	 * @return 生成的位图
	 */
	public Bitmap getBitmap()
	{
		return bitmap;
	}
}
