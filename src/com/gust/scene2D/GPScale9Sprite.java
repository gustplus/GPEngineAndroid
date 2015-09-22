package com.gust.scene2D;

import com.gust.common.math.GPVector2f;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.GPTextureRegion;

public class GPScale9Sprite extends GPSpriteBatchNode {
	private GPTextureRegion region;
	private int widthScale;
	private int heightScale;

	private GPShader shader;

	private GPVector2f WH;

	private boolean verticalVisible; 		// 垂直是否创建
	private boolean horizontalVisible; 		// 水平是否创建

	public GPScale9Sprite(GPTextureRegion bg, int widthScale, int heightScale,
			GPShader shader) {
		super(shader);
		this.widthScale = widthScale;
		this.heightScale = heightScale;
		this.region = bg;
		if (region.getWidth() / 2 < widthScale) {
			this.widthScale = region.getWidth() / 2 - 1;
		}
		if (region.getHeight() / 2 < heightScale) {
			this.widthScale = region.getHeight() / 2 - 1;
		}
		this.shader = shader;
		this.anthorPoint.set(0.5f, 0.5f);
		WH = new GPVector2f();
		this.verticalVisible = true;
		this.horizontalVisible = true;
		
//		name = "Scale9";
	}

	public GPScale9Sprite(GPTextureRegion bg, float widthScaleRate,
			float heightScaleRate, GPShader shader) {
		super(shader);
		this.widthScale = (int) (widthScaleRate * bg.getWidth());
		this.heightScale = (int) (heightScaleRate * bg.getHeight());
		this.region = bg;
		this.shader = shader;
		this.anthorPoint.set(0.5f, 0.5f);
		this.verticalVisible = true;
		this.horizontalVisible = true;
//		name = "Scale9";
	}

	public void setSize(float width, float height) {
		if (width != this.WH.x || height != this.WH.y) {
			WH.set(width, height);
			removeAllChildren();
			if (width == region.getWidth() && height == region.getHeight()) {
				addChild(new GPSprite(region, 0, 0, shader));
			} else {
				if (width / 2 < widthScale) {
					verticalVisible = false;
				}
				if (height / 2 < heightScale) {
					horizontalVisible = false;
				}
				
				int left = region.getLeftX() + widthScale;
				int right = region.getRightX() - widthScale;
				int top = region.getTopY() + heightScale;
				int bottom = region.getBottomY() - heightScale;

				int midWidth = region.getWidth() - 2 * widthScale;
				int midHeight = region.getHeight() - 2 * heightScale;

				float midWidthSize = width - 2 * widthScale;
				float midHeightSize = height - 2 * heightScale;

				GPTextureRegion topLeftRegion = new GPTextureRegion("TL",
						region.getTexture(), region.getLeftX(),
						region.getTopY(), widthScale, heightScale);
				GPSprite topLeft = new GPSprite(topLeftRegion, widthScale, height- heightScale,
						shader);
				topLeft.setAnthorPoint(new GPVector2f(1, 0));
				addChild(topLeft);

				if (verticalVisible) {
					GPTextureRegion topRegion = new GPTextureRegion(
							"T", region.getTexture(), left, region.getTopY(),
							midWidth, heightScale);
					GPSprite topS = new GPSprite(topRegion, width / 2, height
							- heightScale, shader);
					topS.setContentSize(midWidthSize, heightScale);
					topS.setAnthorPoint(new GPVector2f(0.5f, 0));
					addChild(topS);
				}

				GPTextureRegion topRightRegion = new GPTextureRegion(
						"TR", region.getTexture(), region.getRightX() - widthScale,
						region.getTopY(), widthScale, heightScale);
				GPSprite topRight = new GPSprite(topRightRegion, width - widthScale, height - widthScale,
						shader);
				topRight.setAnthorPoint(new GPVector2f(0, 0));
				addChild(topRight);

				if (horizontalVisible) {
					GPTextureRegion leftRegion = new GPTextureRegion(
							"L", region.getTexture(), region.getLeftX(), top,
							widthScale, midHeight);
					GPSprite leftS = new GPSprite(leftRegion, widthScale,
							height / 2, shader);
					leftS.setContentSize(widthScale, midHeightSize);
					leftS.setAnthorPoint(new GPVector2f(1, 0.5f));
					addChild(leftS);
				}

				if (verticalVisible && horizontalVisible) {
					GPTextureRegion midRegion = new GPTextureRegion(
							"M", region.getTexture(), left, top, midWidth, midHeight);
					GPSprite midS = new GPSprite(midRegion, width / 2,
							height / 2, shader);
					midS.setContentSize(midWidthSize, midHeightSize);
					addChild(midS);
				}
				if (horizontalVisible) {
					GPTextureRegion rightRegion = new GPTextureRegion(
							"R", region.getTexture(), right, top, widthScale,
							midHeight);
					GPSprite rightS = new GPSprite(rightRegion, width
							- widthScale, height / 2, shader);
					rightS.setContentSize(widthScale, midHeightSize);
					rightS.setAnthorPoint(new GPVector2f(0,  0.5f));
					addChild(rightS);
				}
				GPTextureRegion bottomLeftRegion = new GPTextureRegion(
						"BL", region.getTexture(), region.getLeftX(),
						region.getBottomY() - heightScale, widthScale,
						heightScale);
				GPSprite bottomLeft = new GPSprite(bottomLeftRegion, widthScale, heightScale,
						shader);
				bottomLeft.setAnthorPoint(new GPVector2f(1, 1));
				addChild(bottomLeft);

				if (verticalVisible) {
					GPTextureRegion bootomRegion = new GPTextureRegion(
							"B", region.getTexture(), left, bottom, midWidth,
							heightScale);
					GPSprite bottomS = new GPSprite(bootomRegion, width / 2,
							heightScale, shader);
					bottomS.setContentSize(midWidthSize, heightScale);
					bottomS.setAnthorPoint(new GPVector2f(0.5f, 1));
					addChild(bottomS);
				}

				GPTextureRegion bottomRightRegion = new GPTextureRegion(
						"BR", region.getTexture(), region.getRightX() - widthScale,
						region.getBottomY() - heightScale, widthScale,
						heightScale);
				GPSprite bottomRight = new GPSprite(bottomRightRegion, width - widthScale,
						heightScale, shader);
				bottomRight.setAnthorPoint(new GPVector2f(0, 1));
				addChild(bottomRight);
			}
		}
	}

	@Override
	protected GPVector2f getWH() {
		return WH;
	}

	@Override
	public void setScaleX(float scaleX) {
		modelIsCurrent = false;
	}

	@Override
	public void setScaleY(float scaleY) {
		modelIsCurrent = false;
	}

}
