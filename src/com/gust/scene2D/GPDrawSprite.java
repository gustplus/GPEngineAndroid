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
 * ����UI����ĸ���
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
	 * �����ؼ�����ʾģ�� ������addText�Ȳ���ǰִ�б���������Ч
	 */
	private void init() {
		this.creater = new GPImageFactory();
		this.creater.setup((int) origin_width, (int) origin_height);
		inited = true;
	}

	/**
	 * Ϊ�ؼ�������֣�������ִ�б�����ǰȷ����ִ��initImageFactory����
	 * 
	 * @param name��Ҫ��ӵ��ı�����
	 * @param RGBA�ı�����ɫ
	 * @param textSize�ı��������С
	 * @param relativeCenterX�ı����е�λ����ͼ���ȵı�ֵ
	 *            ��<=1,�������µ�����
	 * @param relativeCenterY�ı����е�λ����ͼ��߶ȵı�ֵ
	 *            ��<=1���������µ�����
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
	 * �ڿؼ������Ĵ��������,�������������finish()����
	 * 
	 * @param text��Ҫ��ӵ��ı�����
	 * @param RGBA�ı�����ɫ
	 * @param textSize�ı��������С
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
	 * �ڿؼ������λͼ
	 * 
	 * @param bitmapλͼ
	 * @param relativeCenterXλͼ���е�λ����ͼ���ȵı�ֵ
	 *            ��<=1�������£�
	 * @param relativeCenterYλͼ���е�λ����ͼ��߶ȵı�ֵ
	 *            ��<=1�������ң�
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
	 * �ڿؼ������λͼ
	 * 
	 * @param bitmapλͼ
	 * @param distLeftλͼ��Ŀ�����������������
	 * @param distTopλͼ��Ŀ�����������ϱ�����
	 * @param srcLeftλͼ��Դ���������
	 * @param srcTopλͼ��Դ���ϱ�����
	 * @param SrcWidthλͼ���
	 * @param srcHeightλͼ�߶�
	 */
	public void drawPartBitmap(Bitmap bitmap, int distLeft, int distTop,
			int srcLeft, int srcTop, int SrcWidth, int srcHeight) {
		if (!inited){
			init();}
		creater.drawPartBitmap(bitmap, distLeft, distTop, srcLeft, srcTop,
				SrcWidth, srcHeight);
	}

	/**
	 * ��־�ؼ�������ɣ����������ú���ã�
	 * 
	 */
//	public void finish() {
//		GPTextureRegion tegion = new GPTexture2D("bitmap", creater.getBitmap(), false);
//		creater.getBitmap().recycle();
//	}
//
//	/**
//	 * �����ı���λͼ��(��ǰ��)
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
//	 * ����ȫ��
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
