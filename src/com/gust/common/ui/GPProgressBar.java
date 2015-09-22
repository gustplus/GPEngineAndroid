package com.gust.common.ui;

import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.scene2D.GPScale9Sprite;

public class GPProgressBar extends GPScale9Sprite {
	private float progress;
	private float width;
	private float height;

	/**
	 * @param presentation相应的贴图范围
	 * @param x控件的坐标
	 * @param y控件的坐标
	 * @param width控件的宽度
	 * @param height空间的高度
	 * @param widthScale控件的裁切宽度
	 * @param heightScale空间的裁切高度
	 * @param shader着色器
	 */
	public GPProgressBar(GPTextureRegion presentation, float x, float y,
			float width, float height, int widthScale, int heightScale,
			GPShader shader) {
		super(presentation, widthScale, heightScale, shader);
		this.width = width;
		this.height = height;
	}

	/**
	 * 设置当前显示的进度
	 * 
	 * @param progress
	 */
	public void setProgress(float progress) {
		this.progress = progress;
		if (this.progress > 100) {
			this.progress = 100;
		}
		setSize(width, height * this.progress);
	}

	/**
	 * 推进进度
	 * 
	 * @param progress需要推进的进度
	 */
	public void moveforwardsProgress(float progress) {
		this.progress += progress;
		if (this.progress > 100) {
			this.progress = 100;
		}
		setSize(width, height * this.progress);
	}

	/**
	 * 取得当前的进度
	 * 
	 * @return当前的进度
	 */
	public float getProgress() {
		return this.progress;
	}
}
