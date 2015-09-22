package com.gust.common.ui;

import com.gust.common.math.GPVector2f;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.GLStencilCommond;
import com.gust.render_engine.core.GPOpenGLStateManager;
import com.gust.scene2D.GPLayer2D;
import com.gust.system.GPInput.GPTouch;
import com.gust.system_implement.GPTextInputHandler;

public class GPUIEditText extends GPLayer2D {

	private GPLabel label;
	private boolean hasOffset;

	// private GPVector2f movement;

	public GPUIEditText(String text, float x, float y, float width, float height,
			float textSize, GPShader shader) {
		super(x, y, width, height);
		this.setcontentSize(bound.clone());
		label = new GPLabel(text, "", textSize, x, y, shader);
		label.setAnthorPoint(new GPVector2f(0, 0.5f));
		label.setPosition(0, height / 2);
		this.addChild(label);
		// movement = new GPVector2f(0, 0);
		hasOffset = false;
	}

	public void setText(String text) {
		label.setText(text);
		float offset = getContentBound().width - label.getContentBound().width;
		if (offset < 0) {
			label.setPosition(offset, label.getPositionRef().y);
			hasOffset = true;
		} else {
			if (hasOffset) {
				label.setPosition(0, label.getPositionRef().y);
				hasOffset = false;
			}
		}
	}

	public String getText() {
		return label.getText();
	}

	public boolean onTouchDown(GPTouch touch, float deltaTime) {
		if (pointers.size() == 0) {
			GPTextInputHandler.getInstance().setTarget(this);
			GPTextInputHandler.getInstance().callToShowInput();
			// movement.set(touch.x, 0);
		}
		return true;
	}

	public void onTouchDrag(GPTouch touch, float deltaTime) {
		// movement.subFrom(touch.x, 0);
		// movement.mulWith(-1);

	}

	public void onTouchUp(GPTouch touch, float deltaTime) {

	}

	public void beforeDraw() {
//		GPOpenGLStateManager.getInstance().enableScissor(true);
//		GPOpenGLStateManager.getInstance().scissor(contentBound);
		GPOpenGLStateManager.getInstance().pushCommond(new GLStencilCommond(contentBound));
	}

	public void afterDraw() {
		GPOpenGLStateManager.getInstance().popCommond();
	}

	// private void fixBound(){
	// }
}
