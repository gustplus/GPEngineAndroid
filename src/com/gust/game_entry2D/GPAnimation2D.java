package com.gust.game_entry2D;

import com.gust.render_engine.core.GPTextureRegion;

/**
 * 2D������
 * @author gustplus
 *
 */
public class GPAnimation2D {
	public static final int ANIMATION_LOOPING = 0;
	public static final int ANIMATION_NONLOOPING = 1;

	public GPTextureRegion[] keyFrames;			//�����ؼ�֡��Ӧ����ͼ��Χ����
	float frameDuration;

	public GPAnimation2D(float frameDurtion, GPTextureRegion... keyFrames)
	{
		this.frameDuration = frameDurtion;
		this.keyFrames = keyFrames;
	}

	/**
	 * ����ʱ��ȡ�ö�Ӧ��֡ͼƬ
	 * @param startTime������ʼ�󾭹���ʱ��
	 * @param mode
	 * @return	��Ӧ����ͼ��Χ
	 */
	public GPTextureRegion getKeyFrame(float startTime, int mode)
	{
		int numFrame = (int) (startTime / frameDuration);	//��ǰʱ���Ӧ��֡��������
		if (mode == ANIMATION_LOOPING) {
			numFrame = numFrame % keyFrames.length;
		}
		if (mode == ANIMATION_NONLOOPING) {
			numFrame = numFrame > keyFrames.length - 1 ? keyFrames.length - 1
					: numFrame;
		}
		return keyFrames[numFrame];
	}
}
