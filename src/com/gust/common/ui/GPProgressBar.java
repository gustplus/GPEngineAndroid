package com.gust.common.ui;

import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.scene2D.GPScale9Sprite;

public class GPProgressBar extends GPScale9Sprite {
	private float progress;
	private float width;
	private float height;

	/**
	 * @param presentation��Ӧ����ͼ��Χ
	 * @param x�ؼ�������
	 * @param y�ؼ�������
	 * @param width�ؼ��Ŀ��
	 * @param height�ռ�ĸ߶�
	 * @param widthScale�ؼ��Ĳ��п��
	 * @param heightScale�ռ�Ĳ��и߶�
	 * @param shader��ɫ��
	 */
	public GPProgressBar(GPTextureRegion presentation, float x, float y,
			float width, float height, int widthScale, int heightScale,
			GPShader shader) {
		super(presentation, widthScale, heightScale, shader);
		this.width = width;
		this.height = height;
	}

	/**
	 * ���õ�ǰ��ʾ�Ľ���
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
	 * �ƽ�����
	 * 
	 * @param progress��Ҫ�ƽ��Ľ���
	 */
	public void moveforwardsProgress(float progress) {
		this.progress += progress;
		if (this.progress > 100) {
			this.progress = 100;
		}
		setSize(width, height * this.progress);
	}

	/**
	 * ȡ�õ�ǰ�Ľ���
	 * 
	 * @return��ǰ�Ľ���
	 */
	public float getProgress() {
		return this.progress;
	}
}
