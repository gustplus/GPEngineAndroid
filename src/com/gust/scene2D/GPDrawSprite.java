package com.gust.scene2D;

import java.util.List;

import android.graphics.Bitmap;

import com.gust.common.math.GPVector2f;
import com.gust.engine.core.GPShader;
import com.gust.physics2D.Collision_System2D;
import com.gust.render_engine.core.GPSpriteBatcher;
import com.gust.render_engine.core.GPTexture2D;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.render_engine.core.GPTexture_20;
import com.gust.system.GPInput.GPTouch;

/**
 * 所有UI组件的父类
 * 
 * @author gustplus
 */
public class GPDrawSprite extends GPSprite {
	protected GPImageFactory creater;
	protected boolean inited;

	public GPDrawSprite(float x, float y, int width, int height, GPShader shader) {
		super(x, y, shader);
		this.origin_width = width;
		this.origin_height = height;
		this.selectMode = false;
		this.swallow = true;
		inited = false;
	}

	/**
	 * 创建控件的显示模版 必须在addText等操作前执行本方法才有效
	 */
	private void init() {
		this.creater = new GPImageFactory();
		this.creater.setup((int) origin_width, (int) origin_height);
		inited = true;
	}

	/**
	 * 为控件添加文字，必须在执行本操作前确保已执行initImageFactory操作
	 * 
	 * @param name需要添加的文本内容
	 * @param RGBA文本的颜色
	 * @param textSize文本的字体大小
	 * @param relativeCenterX文本的中点位置与图像宽度的比值
	 *            （<=1,从上向下递增）
	 * @param relativeCenterY文本的中点位置与图像高度的比值
	 *            （<=1，从上向下递增）
	 */
	public void addText(String name, float[] RGBA, float textSize,
			float relativeCenterX, float relativeCenterY) {
		if (!inited) {
			init();
		}
		creater.drawText(name, RGBA, textSize, origin_width * relativeCenterX,
				origin_height * relativeCenterY);
	}

	/**
	 * 在控件的中心处添加文字,设置完后必须调用finish()方法
	 * 
	 * @param text需要添加的文本内容
	 * @param RGBA文本的颜色
	 * @param textSize文本的字体大小
	 */
	public void addText(String text, float[] RGBA, float textSize) {
		if (!inited) {
			init();
		}
		creater.drawText(text, RGBA, textSize);
	}

	public void scale(float scale) {
		this.getBoundBox().width *= scale;
		this.getBoundBox().height *= scale;
	}

	/**
	 * 在控件上添加位图
	 * 
	 * @param bitmap位图
	 * @param relativeCenterX位图的中点位置与图像宽度的比值
	 *            （<=1自上向下）
	 * @param relativeCenterY位图的中点位置与图像高度的比值
	 *            （<=1自左向右）
	 */
	public void drawBitmap(Bitmap bitmap, float relativeCenterX,
			float relativeCenterY) {
		if (!inited) {
			init();
		}
		int centerX = (int) (relativeCenterX * creater.width);
		int centerY = (int) (relativeCenterY * creater.height);
		creater.drawBitmap(bitmap, centerX, centerY);
	}

	/**
	 * 在控件上添加位图
	 * 
	 * @param bitmap位图
	 * @param distLeft位图的目标绘制区域的左边坐标
	 * @param distTop位图的目标绘制区域的上边坐标
	 * @param srcLeft位图资源的左边坐标
	 * @param srcTop位图资源的上边坐标
	 * @param SrcWidth位图宽度
	 * @param srcHeight位图高度
	 */
	public void drawPartBitmap(Bitmap bitmap, int distLeft, int distTop,
			int srcLeft, int srcTop, int SrcWidth, int srcHeight) {
		if (!inited){
			init();}
		creater.drawPartBitmap(bitmap, distLeft, distTop, srcLeft, srcTop,
				SrcWidth, srcHeight);
	}

	/**
	 * 标志控件设置完成（仅需在设置后调用）
	 * 
	 */
//	public void finish() {
//		GPTextureRegion tegion = new GPTexture2D("bitmap", creater.getBitmap(), false);
//		creater.getBitmap().recycle();
//	}
//
//	/**
//	 * 绘制文本，位图等(即前景)
//	 */
//	private void drawComponent(float offset) {
//		if (frontgound != null) {
//			GPTextureRegion te = new GPTextureRegion(frontgound, 0, 0,
//					widthInPixel, heightInPixel);
//			batcher.beginBatch(frontgound);
//			batcher.drawSprite3D(worldPosition.x + getBoundBox().width / 2,
//					worldPosition.y + getBoundBox().height / 2, offset,
//					getContentBound().width, getContentBound().height, scaleX,
//					scaleY, 0, te);
//			batcher.endBatch();
//		}
//	}
//
//	/**
//	 * 绘制全部
//	 * 
//	 * @param glGame
//	 */
//	public void drawself() {
//		if (texture != null) {
//			batcher.beginBatch(this.texture);
//			batcher.drawSprite3D(getBoundBox().width / 2,
//					getBoundBox().height / 2, depthZ, getBoundBox().width,
//					getBoundBox().height, 1, 1, this.presentation);
//			batcher.endBatch();
//		}
//		drawComponent(depthZ);
//	}

	public int compareTo(GPSprite other) {
		return this.depthZ < other.depthZ ? -1 : 1;
	}

}
