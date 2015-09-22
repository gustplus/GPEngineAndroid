package com.gust.game_entry2D;

import com.gust.render_engine.core.GPTextureRegion;

/**
 * 2D动画类
 * @author gustplus
 *
 */
public class GPAnimation2D {
	public static final int ANIMATION_LOOPING = 0;
	public static final int ANIMATION_NONLOOPING = 1;

	public GPTextureRegion[] keyFrames;			//动画关键帧对应的贴图范围数组
	float frameDuration;

	public GPAnimation2D(float frameDurtion, GPTextureRegion... keyFrames)
	{
		this.frameDuration = frameDurtion;
		this.keyFrames = keyFrames;
	}

	/**
	 * 根据时间取得对应的帧图片
	 * @param startTime动画开始后经过的时间
	 * @param mode
	 * @return	相应的贴图范围
	 */
	public GPTextureRegion getKeyFrame(float startTime, int mode)
	{
		int numFrame = (int) (startTime / frameDuration);	//当前时间对应的帧动画索引
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
