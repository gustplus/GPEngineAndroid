package com.gust.scene2D;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * ��������λͼ
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
	 * ���ݲ�������λͼģ�湩����
	 * @param widthλͼ�Ŀ��,����Ϊ2^x
	 * @param heightλͼ�ĸ߶ȱ���Ϊ2^x
	 * @param RGBAλͼģ��ı�����ɫ
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
	 * ���ݲ�������λͼģ�湩���ƣ�������ɫΪ͸��ɫ
	 * @param widthλͼ�Ŀ��
	 * @param heightλͼ�ĸ߶�
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
	 * ���ݲ�������λͼģ�湩���ƣ�����Ϊָ����λͼ
	 * @param width���ɵ�λͼ�Ŀ��
	 * @param height���ɵ�λͼ�ĸ߶�
	 * @param bitmap��Ϊ������λͼ
	 */
	public void setup(int width, int height, Bitmap bitmap)
	{
		// TODO Auto-generated constructor stub
		this.width = width;
		this.height = height;
		canvas = new Canvas(bitmap);
	}

	/**
	 * ��������
	 * @param text���Ƶ��ı�����
	 * @param RGBA�ı�����ɫ
	 * @param textSize�ı��������С
	 * @param centerX�ı����Ƶ�����λ��
	 * @param centerY�ı����Ƶ�����λ��
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
	 * ��ͼƬ���Ļ�������
	 * @param text���Ƶ��ı�����
	 * @param RGBA�ı�����ɫ
	 * @param textSize�ı��������С
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
	 * ����λͼ
	 * @param bitmapλͼ��Դ
	 * @param centerXλͼ���Ƶ�����λ��
	 * @param centerYλͼ���Ƶ�����λ��
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
	 * �ڿؼ������λͼ
	 * @param bitmapλͼ
	 * @param distLeftλͼ��Ŀ�����������������
	 * @param distTopλͼ��Ŀ�����������ϱ�����
	 * @param srcLeftλͼ��Դ���������
	 * @param srcTopλͼ��Դ���ϱ�����
	 * @param SrcWidthλͼ���
	 * @param srcHeightλͼ�߶�
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
	 * ���ƾ���
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
	 * ���Ƶ�
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
	 * ������
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
	 * ���ͼƬ
	 * 
	 * @param RGBA���������ɫ
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
	 * ȡ�����ɵ�λͼ
	 * 
	 * @return ���ɵ�λͼ
	 */
	public Bitmap getBitmap()
	{
		return bitmap;
	}
}
