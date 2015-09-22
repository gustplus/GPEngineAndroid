package com.gust.common.animation2D;

import com.gust.render_engine.core.GPTextureRegion;

public class GPSpriteFrames {
	public GPTextureRegion[] regions;
	public float frameDuration;

	private GPSpriteFrames(float frameDuration, GPTextureRegion... regions) {
		this.regions = regions;
		this.frameDuration = frameDuration;
	}

	public static GPSpriteFrames create(float frameDuration,
			GPTextureRegion... regions) {
		return new GPSpriteFrames(frameDuration, regions);
	}

	public GPTextureRegion update(float startTime, boolean loop) {
		int numFrame = (int) (startTime / frameDuration); // 当前时间对应的帧动画索引
		if (loop) {
			numFrame = numFrame % regions.length;
		} else {
			numFrame = numFrame > regions.length - 1 ? regions.length - 1
					: numFrame;
		}
		return regions[numFrame];
	}
	
	public int getFramesNum(){
		return regions.length;
	}
}
