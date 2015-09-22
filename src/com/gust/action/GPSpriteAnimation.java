package com.gust.action;

import com.gust.common.animation2D.GPSpriteFrames;
import com.gust.common.game_util.GPLogger;
import com.gust.render_engine.core.GPTexture2D;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.scene2D.GPSprite;

public class GPSpriteAnimation extends GPAction2D {
	public GPSpriteFrames frames;
	public GPTexture2D texture;
	public float pastTime;
	private boolean loop;
	private GPTextureRegion region;
	private boolean isDone;
	private boolean isStop;

	public GPSpriteAnimation(GPTexture2D texture, float totalTime,
			GPTextureRegion... regions) {
		frames = GPSpriteFrames.create(totalTime / regions.length, regions);
		this.texture = texture;
		pastTime = 0;
		region = regions[0];
		isDone = false;
		isStop = false;
	}

	public void play() {
		isStop = false;
	}

	public void stop() {
		isStop = true;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public void update(float deltaTime) {
		if (!isDone && !isStop) {
			pastTime += deltaTime;
			region = frames.update(pastTime, loop);
			
			if (this.component instanceof GPSprite) {
				GPSprite sprite = (GPSprite) this.component;
				sprite.setTextureRegion(region);
			}else{
				GPLogger.log("SpriteAnimation", "component should be sprite");
			}
			
			if (pastTime > frames.frameDuration && !loop) {
				isDone = true;
			}
		}
	}

	public GPTextureRegion getCurRegion() {
		return region;
	}

	public boolean isDone() {
		return isDone;
	}

	public void reset() {
		this.isDone = false;
		this.isStop = false;
		this.pastTime = 0;
		update(0);
	}

	public void goAndPlay(int index) {
		isStop = false;
		pastTime = frames.frameDuration * index / frames.getFramesNum();
	}

	public void goAndStop(int index) {
		isStop = true;
		pastTime = frames.frameDuration * index / frames.getFramesNum();
	}

	@Override
	public GPAction clone() {
		// TODO Auto-generated method stub
		return null;
	}
}
