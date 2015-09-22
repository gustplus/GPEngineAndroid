package com.gust.common.control;

import com.gust.common.math.GPVector2f;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.scene2D.GPSprite;
import com.gust.system.GPInput.GPTouch;

/**
 * ◊Û–Èƒ‚“°∏À£¨∂‡”√”⁄øÿ÷∆∑ΩœÚ
 * 
 * @author gustplus
 * 
 */
public class GPJoyStick extends GPSprite {
	public static final int PRESSDOWN = 0;
	public static final int DRAGGING = 1;
	public static final int FREE = 2;
	private int state;
	
	private GPVector2f moveDirection;
	private float speedRate;
	public boolean controlable = true;
	private GPSprite joyPad;
	private final GPVector2f basePosition;
	private float radius;
	/**
	 * 
	 * @param camera
	 * @param batcher
	 * @param joyStickTexture“°∏ÀÃ˘Õº
	 * @param joyPad“°∏À±˙œ‡”¶µƒÃ˘Õº∑∂Œß
	 * @param joyBase“°∏À±≥æ∞œ‡”¶µƒÃ˘Õº∑∂Œß
	 */
	public GPJoyStick(GPTextureRegion joyPad, GPTextureRegion joyBase,
	/* Bound enableArea, */float x, float y, GPShader shader) {
		super(joyBase, x, y, shader);
		this.state = FREE;
		moveDirection = new GPVector2f(0, 0);
		basePosition = new GPVector2f(getContentBound().width / 2,
				getContentBound().height / 2);
		
		this.joyPad = new GPSprite(joyPad, basePosition.x, basePosition.y,
				shader);
		this.speedRate = 1;
		this.addChild(this.joyPad);
		this.selectMode = true;
		radius = basePosition.x > basePosition.y ? basePosition.y : basePosition.x;
	}

	public void setSpeed(float speedRate) {
		this.speedRate = speedRate;
	}

	@Override
	public boolean onTouchDown(GPTouch touch, float deltaTime) {
		GPVector2f pos = new GPVector2f(touch.x, touch.y);
		state = PRESSDOWN;
		// if (!enableArea.isPointIn(pos)) {
		// return false;
		// }
		calMovement(pos);
		return true;
	}

	@Override
	public void onTouchDrag(GPTouch touch, float deltaTime) {
		GPVector2f pos = new GPVector2f(touch.x, touch.y);
		state = DRAGGING;
		calMovement(pos);
	}

	@Override
	public void onTouchUp(GPTouch touch, float deltaTime) {
		state = FREE;
		moveDirection.set(0, 0);
		joyPad.setPosition(basePosition);
	}

	private void calMovement(GPVector2f touchPosition) {
		GPVector2f result = touchPosition.sub(getPositionRef());

		if(result.len() > radius){
			result.normalize().mulWith(radius);
		}
		
		touchPosition = result.add(basePosition);
		joyPad.setPosition(touchPosition);
		moveDirection.set(result.x, result.y);
		moveDirection.mulWith(speedRate);
	}

	/**
	 * ÷ÿ÷√
	 */
	public void reset() {
		visible = false;
		pointers.clear();
		moveDirection.set(0, 0);
	}

	public int getState() {
		return state;
	}

	public GPVector2f getMoveDirection() {
		return moveDirection;
	}

}
