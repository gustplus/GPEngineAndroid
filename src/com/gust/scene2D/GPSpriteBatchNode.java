package com.gust.scene2D;

import com.gust.common.game_util.GPLogger;
import com.gust.common.math.GPVector2f;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.base.MatrixHelper;
import com.gust.render_engine.core.GPTexture2D;

public class GPSpriteBatchNode extends GPNode2D {

	private GPTexture2D tex;
	private GPSpriteBuffer buffer;
	protected GPShader shader;
	
	private boolean onlyChildChange;			//只有子节点的包围盒发生改变，子节点数量没改变
	private boolean onlyPositionChange;

	public GPSpriteBatchNode(GPShader shader) {
		super();
		tex = null;
		this.shader = shader;
		
		onlyPositionChange = false;
		onlyChildChange = false;
	}

	@Override
	public void drawself() {
		if (buffer != null) {
			buffer.draw();
		}
	}

	@Override
	public void reloadSelf() {
		buffer.reloadVBO();
	}

	public void setFunctionID(int functionID) {
		buffer.setFunctionID(functionID);
	}

	public void changeToColor(float[] color, float time) {
		buffer.changeToColor(color, time);
	}

	@Override
	protected void calculateBoundingBox() {
		super.calculateBoundingBox();
		
		if (!worldIsCurrent || !modelIsCurrent || buffer == null) {
			this.contentBound = bound.clone();
			int size = numOfChildren();
			if (buffer != null && buffer.getMaxSpriteNum() >= size) {
				buffer.clear();
			} else {
				GPLogger.log("", "create buffer");
				buffer = new GPSpriteBuffer(size, shader);
			}
			for (int i = 0; i < size; ++i) {
				GPSprite sprite = (GPSprite) children.get(i);
				buffer.addSprite(sprite.getVerticeBuffer());
			}
			buffer.setTexture(tex);
			buffer.setAlpha(transparent);
			buffer.flipToSize();
		}
	}

	@Override
	public void addChild(GPNode2D child) {
		if (child instanceof GPSprite) {
			if (numOfChildren() == 0) {
				GPSprite sprite = (GPSprite) child;
				if(tex != null&& tex != sprite.getTexture()){
					tex.release();
				}
				tex = sprite.getTexture();
				tex.retain();
			}
			super.addChild(child);
		} else {
			GPLogger.log("SpriteBatchNode",
					"spriteBatchNode's child must be sprite");
		}
	}

	public void setAnthorPoint(GPVector2f anthorPoint) {
		if (anthorPoint.x > 1) {
			anthorPoint.x = 1;
		}
		if (anthorPoint.x < 0) {
			anthorPoint.x = 0;
		}
		if (anthorPoint.y > 1) {
			anthorPoint.y = 1;
		}
		if (anthorPoint.y < 0) {
			anthorPoint.y = 0;
		}
		this.anthorPoint.set(anthorPoint);
		worldIsCurrent = false;
		
//		postUpdateFlagToParent();
	}

	@Override
	public void addChild(GPNode2D child, int tag) {
		if (child instanceof GPSprite) {
			if (numOfChildren() == 0) {
				GPSprite sprite = (GPSprite) child;
				if(tex != null&& tex != sprite.getTexture()){
					tex.release();
				}
				tex = sprite.getTexture();
				tex.retain();
			}
			super.addChild(child, tag);
		} else {
			GPLogger.log("SpriteBatchNode",
					"spriteBatchNode's child must be sprite");
		}
	}
	
}
