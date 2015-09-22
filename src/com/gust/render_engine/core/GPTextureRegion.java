package com.gust.render_engine.core;

import com.gust.engine.core.GPSource;


/**
 * ���ڴ����ͼ��Χ����
 * 
 * @author gustplus
 * 
 */
public class GPTextureRegion extends GPSource{
	public GPTexture2D texture;
	private int topY;
	private int leftX;
	private int width;
	private int height;
	public float u1, v1;
	public float u2, v2;

	private boolean isRotated;

	/**
	 * �������ʼ����
	 * 
	 * @param texture
	 * @param leftX��ʹ�õ��������ʼ����
	 *            (���Ͻ�Ϊ��0��0����
	 * @param topY��ʹ�õ��������ʼ����
	 * @param width��ʹ�õ�����Ŀ��
	 * @param height��ʹ�õ�����ĸ߶�
	 */
	public GPTextureRegion(String fileName, GPTexture2D texture, int leftX, int topY, int width,
			int height) {
		super(fileName);
		this.texture = texture;
		texture.retain();
		this.width = width;
		this.height = height;
		this.leftX = leftX;
		this.topY = topY;
		this.u1 = (float) leftX / texture.width;
		this.v1 = (float) topY / texture.height;
		this.u2 = (float) (leftX + width) / texture.width;
		this.v2 = (float) (topY + height) / texture.height;
		isRotated = false;
	}
	
	public GPTextureRegion(GPTexture2D texture, int leftX, int topY, int width,
			int height) {
		this("", texture, leftX, topY, width, height);
	}

	public GPTextureRegion(String fileName, GPTexture2D texture) {
		super(fileName);
		this.texture = texture;
		texture.retain();
		this.width = texture.width;
		this.height = texture.height;
		this.u1 = (float) 0f;
		this.v1 = (float) 0f;
		this.u2 = (float) 1f;
		this.v2 = (float) 1f;
		isRotated = false;
	}

	// public void setRegion(int leftX, int topY){
	// this.topY = topY;
	// this.leftX = leftX;
	// this.u1 = (float)leftX/texture.width;
	// this.v1 = (float)topY/texture.height;
	// this.u2 = (float)(leftX+width)/texture.width;
	// this.v2 = (float)(topY+height)/texture.height;
	// }

	// public void setRegion(int leftX, int topY, int width, int height){
	// this.topY = topY;
	// this.leftX = leftX;
	// this.width = width;
	// this.height = height;
	// this.u1 = (float)leftX/texture.width;
	// this.v1 = (float)topY/texture.height;
	// this.u2 = (float)(leftX+width)/texture.width;
	// this.v2 = (float)(topY+height)/texture.height;
	// }

	public void setRotated(boolean isRotated) {
		if (this.isRotated != isRotated) {
			this.width += this.height;
			this.height = this.width - this.height;
			this.width -= this.height;
		}
		this.isRotated = isRotated;
	}

	public boolean isRotated() {
		return isRotated;
	}

	public int getTopY() {
		return topY;
	}

	public int getLeftX() {
		return leftX;
	}
	
	public int getBottomY() {
		return topY + height;
	}

	public int getRightX() {
		return leftX + width;
	}

	public float[] toArray() {
		return new float[] { u1, v1, u2, v1, u1, v2, u2, v2 };
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		WHtoUV();
	}

	public GPTexture2D getTexture() {
		return texture;
	}

	public void setTexture(GPTexture2D texture) {
		this.texture = texture;
		WHtoUV();
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		WHtoUV();
	}

	public void WHtoUV() {
		this.u1 = (float) leftX / texture.width;
		this.v1 = (float) topY / texture.height;
		this.u2 = (float) (leftX + width) / texture.width;
		this.v2 = (float) (topY + height) / texture.height;
	}

	public void UVtoWH() {
		leftX = (int) (texture.width * u1);
		topY = (int) (texture.height * v1);
		width = (int) (texture.width * u2 - leftX);
		height = (int) (texture.height * u2 - topY);
	}

	@Override
	public void dispose() {
		texture.release();
	}
}
