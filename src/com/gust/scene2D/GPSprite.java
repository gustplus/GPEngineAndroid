package com.gust.scene2D;

import java.util.ArrayList;
import java.util.List;

import com.gust.common.game_util.GPLogger;
import com.gust.common.game_util.GPPool;
import com.gust.common.game_util.GPPool.PoolObjectFactory;
import com.gust.common.math.GPConstants;
import com.gust.common.math.GPVector2f;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.GPTexture2D;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.render_engine.core.GPVertexBuffer3;
import com.gust.system.GPInput.GPTouch;

/**
 * @author gustplus
 */
public class GPSprite extends GPLayer2D {
	protected GPTextureRegion presentation;
	protected SpriteRect spriteRect;
	protected GPVector2f size;

	public boolean selectMode;

	protected float origin_width;
	protected float origin_height;

	protected boolean autoSize; // 是否根据图片的大小改变大小
	
	public static GPPool<SpriteRect> quadPool;

	public GPSprite(GPTextureRegion presentation, float x, float y,
			final GPShader shader) {
		super(x, y, presentation.getWidth(), presentation.getHeight());
		this.anthorPoint = new GPVector2f(0.5f, 0.5f);
		this.presentation = presentation;
		this.selectMode = false;
		this.swallow = false;
		this.size = new GPVector2f((int) Math.abs((this.presentation.getWidth())), (int) Math.abs((this.presentation.getHeight())));

		this.setWidth(size.x);
		this.setHeight(size.y);

		this.origin_width = (int) size.x;
		this.origin_height = (int) size.y;

		this.contentBound.lowerLeft.set(x - size.x / 2, y - size.y / 2);
		this.contentBound.width = size.x;
		this.contentBound.height = size.y;
		
		if(quadPool == null){
			PoolObjectFactory<SpriteRect> factory = new PoolObjectFactory<SpriteRect>() {
				public SpriteRect createObject() {
					GPLogger.log("", "create");
					return new SpriteRect(shader);
				}
			};
			quadPool = new GPPool<SpriteRect>(factory, 10);
		}

		this.spriteRect = quadPool.newObject();
		this.spriteRect.setGPTextureRegion(presentation);
		autoSize = true;
//		name = "Sprite";
		
	}

	public GPSprite(GPTexture2D texture, float x, float y, final GPShader shader) {
		super(x, y, 0, 0);
		this.anthorPoint = new GPVector2f(0.5f, 0.5f);
		this.presentation = new GPTextureRegion("full pic", texture);
		this.selectMode = false;
		this.swallow = false;
		this.size = new GPVector2f(texture.width, texture.height);
		this.setWidth(size.x);
		this.setHeight(size.y);
		this.origin_width = (int) size.x;
		this.origin_height = (int) size.y;

		this.contentBound.lowerLeft.set(x - size.x / 2, y - size.y / 2);
		this.contentBound.width = size.x;
		this.contentBound.height = size.y;
		
		if(quadPool == null){
			PoolObjectFactory<SpriteRect> factory = new PoolObjectFactory<SpriteRect>() {
				public SpriteRect createObject() {
					return new SpriteRect(shader);
				}
			};
			quadPool = new GPPool<SpriteRect>(factory, 10);
		}

		this.spriteRect = quadPool.newObject();
		this.spriteRect.setGPTextureRegion(presentation);
		autoSize = true;
		
//		name = "Sprite";
	}

	public GPSprite(float x, float y, final GPShader shader) {
		super(x, y, 0, 0);
		this.anthorPoint = new GPVector2f(0.5f, 0.5f);
		this.presentation = null;
		this.selectMode = false;
		this.swallow = false;
		this.size = new GPVector2f();
		this.setWidth(size.x);
		this.setHeight(size.y);
		this.origin_width = (int) size.x;
		this.origin_height = (int) size.y;

		this.contentBound.lowerLeft.set(x - size.x / 2, y - size.y / 2);
		this.contentBound.width = size.x;
		this.contentBound.height = size.y;

		if(quadPool == null){
			PoolObjectFactory<SpriteRect> factory = new PoolObjectFactory<SpriteRect>() {
				public SpriteRect createObject() {
					return new SpriteRect(shader);
				}
			};
			quadPool = new GPPool<SpriteRect>(factory, 10);
		}

		this.spriteRect = quadPool.newObject();
//		this.spriteRect.setGPTextureRegion(presentation);
		autoSize = true;
		
//		name = "Sprite";
	}

	public void setContentSize(float width, float height) {
		this.contentBound.width = width;
		this.contentBound.height = height;
		this.origin_height = Math.abs(height);
		this.origin_width = Math.abs(width);
		this.size = new GPVector2f(width, height);
		if (parent != null) {
			parent.modelIsCurrent = false;
		}
		modelIsCurrent = false;
		autoSize = false;
	}

	public void setTransparent(float transparent) {
		super.setTransparent(transparent);
		if (spriteRect != null) {
			spriteRect.setAlpha(transparent);
		}
	}

	public void changeToColor(float[] color, float time) {
		if (spriteRect != null) {
			spriteRect.changeToColor(color, time);
		}
	}

	public void setFunctionID(int functionID) {
		this.spriteRect.getVerticeBuffer().setFunctionID(functionID);
	}

	protected GPVector2f getWH() {
		return size;
	}

	@Override
	public void setScaleX(float scaleX) {
		super.setScaleX(scaleX);
		size.y = Math.abs(origin_width * scaleX);
	}

	@Override
	public void setScaleY(float scaleY) {
		super.setScaleY(scaleY);
		size.y = Math.abs(origin_height * scaleY);
	}

	public void drawself() {
		if (presentation != null) {
			this.presentation.getTexture().bind();
			this.spriteRect.bind();
//			MatrixHelper.getInstance().setModelMatrix(worldTransform);
			this.spriteRect.draw();
		}
	}
	
	public void reloadSelf() {
		this.spriteRect.reloadVBO();
	}

	public GPVertexBuffer3 getVerticeBuffer() {
		return spriteRect.getVerticeBuffer();
	}

	public void update(List<GPTouch> touches, float deltaTime) {
		super.update(touches, deltaTime);
	}

	protected GPTexture2D getTexture() {
		return presentation.getTexture();
	}

	protected GPShader getShader() {
		return spriteRect.getShader();
	}

	protected GPTextureRegion getTextureRegion() {
		return presentation;
	}

	public void setTextureRegion(GPTextureRegion newRegion) {
		spriteRect.setGPTextureRegion(newRegion);
		if (newRegion.getWidth() == origin_width
				&& newRegion.getHeight() == origin_height) {
			return;
		}
		if(this.presentation != null){
			this.presentation.release();
		}
		this.presentation = newRegion;
		newRegion.retain();
		size.x = (int) Math.abs((this.presentation.getWidth()));
		size.y = (int) Math.abs((this.presentation.getHeight()));
		if (autoSize) {
			this.setContentSize(newRegion.getWidth(), newRegion.getHeight());
			autoSize = true;
		}
		this.modelIsCurrent = false;
		if (anthorPoint.x != 0 || anthorPoint.y != 0) {
			this.worldIsCurrent = false;
		}
	}

	public void setTexture(GPTexture2D tex) {
		setTextureRegion(new GPTextureRegion("full pic", tex));
	}

	public void fixPixelWH() {
		this.origin_width = presentation.getWidth();
		this.origin_height = presentation.getHeight();
		this.contentBound.width = origin_width;
		this.contentBound.height = origin_height;
		size.x = Math.abs(origin_width * scaleX);
		size.y = Math.abs(origin_height * scaleY);
		autoSize = true;
	}

	protected void calculateBoundingBox() {
		if (!modelIsCurrent || !worldIsCurrent) {
			GPVector2f lowleft = new GPVector2f(0, 0);
			ArrayList<GPVector2f> points = new ArrayList<GPVector2f>(4);
			float[] vertices = new float[16];
			float width = origin_width * scaleX;
			float height = origin_height * scaleY;
			GPVector2f pointLL = worldTransform.transform(lowleft);
			vertices[0] = pointLL.x;
			vertices[1] = pointLL.y;
			vertices[2] = 0;
			points.add(pointLL);
			if (worldRotation < GPConstants.almostZero
					|| worldRotation > -GPConstants.almostZero) {
				GPVector2f pointLR = pointLL.add(width, 0);
				vertices[3] = pointLR.x;
				vertices[4] = pointLR.y;
				vertices[5] = 0;
				points.add(pointLR);

				GPVector2f pointTR = pointLR.add(0, height);
				vertices[6] = pointTR.x;
				vertices[7] = pointTR.y;
				vertices[8] = 0;
				points.add(pointTR);

				GPVector2f pointTL = pointTR.add(-width, 0);
				vertices[9] = pointTL.x;
				vertices[10] = pointTL.y;
				vertices[11] = 0;
				points.add(pointTL);
			} else {
				pointLL = worldTransform
						.transform(lowleft.add(origin_width, 0));
				vertices[3] = pointLL.x;
				vertices[4] = pointLL.y;
				vertices[5] = 0;
				points.add(pointLL);
				pointLL = worldTransform.transform(lowleft.add(origin_width,
						origin_height));
				vertices[6] = pointLL.x;
				vertices[7] = pointLL.y;
				vertices[8] = 0;
				points.add(pointLL);
				pointLL = worldTransform.transform(lowleft
						.add(0, origin_height));
				vertices[9] = pointLL.x;
				vertices[10] = pointLL.y;
				vertices[11] = 0;
				points.add(pointLL);
			}
			this.spriteRect.setVertices(vertices);
			this.contentBound.createWithPoints(points);
			this.bound = this.contentBound.clone();
			if (children != null) {
				int size = children.size();
				for (int i = 0; i < size; ++i) {
					this.bound.include(children.get(i).bound);
				}
			}
			if (parent != null) {
				parent.modelIsCurrent = false;
			}
			
			if(parent != null && parent instanceof GPSpriteBatchNode){
				spriteRect.getVerticeBuffer().setInfoChange(false);
			}
		}
	}

	public GPSprite clone() {
		GPSprite sprite = new GPSprite(this.presentation,
				(int) this.position.x, (int) this.position.y,
				this.spriteRect.getShader());
		sprite.setContentSize(origin_width, origin_height);
		sprite.anthorPoint = this.anthorPoint.clone();
		sprite.autoSize = autoSize;
		return sprite;
	}
	
	protected void finalize() throws Throwable {
		super.finalize();
		dispose();
	}

	public void dispose(){
		quadPool.free(spriteRect);
		spriteRect = null;
	}
}
