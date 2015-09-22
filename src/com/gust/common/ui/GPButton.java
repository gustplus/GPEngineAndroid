package com.gust.common.ui;

import java.util.ArrayList;

import com.gust.common.math.GPVector2f;
import com.gust.engine.core.GPShader;
import com.gust.physics2D.Collision_System2D;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.scene2D.GPSprite;
import com.gust.scene2D.GPTouchEvent;
import com.gust.system.GPInput.GPTouch;

public class GPButton extends GPSprite {
	private GPTextureRegion pressed;
	private GPTextureRegion normal;

	/**
	 * @param normalΪ����ʱ��Ӧ����ͼ��Χ
	 * @param bepressed����ʱ��Ӧ����ͼ��Χ
	 * @param x�ؼ�������
	 * @param y�ؼ�������
	 * @param shader��ɫ��
	 */
	public GPButton(GPTextureRegion normal, GPTextureRegion bepressed, float x,
			float y, GPShader shader) {
		super(normal, x, y, shader);
		this.pressed = bepressed;
		this.normal = normal;
		this.selectMode = true;
		this.visible = true;
		this.swallow = true;
	}

	private void pressed() {
		setTextureRegion(pressed);
	}

	private void pressUp() {
		setTextureRegion(normal);
	}

	@Override
	public boolean onTouchDown(GPTouch touch, float deltaTime) {
		if (selectMode) {
			pressed();
			dispatchEvent(GPTouchEvent.PRESS_DOWN);
			return swallow;
		}
		return false;
	}

	public void onTouchDrag(GPTouch touch, float deltaTime) {}

	public void onTouchUp(GPTouch touch, float deltaTime) {
		if (selectMode) {
			GPVector2f point = new GPVector2f(touch.x, touch.y);
			if (Collision_System2D.pointInRectangle(getContentBound(), point)) {
				dispatchEvent(GPTouchEvent.CLICK);
			}
			pressUp();
			dispatchEvent(GPTouchEvent.PRESS_UP);
		}
	}

	public void onTouchesDown(ArrayList<GPTouch> touches, float deltaTime) {}

	public void onTouchesDrag(ArrayList<GPTouch> touches, float deltaTime) {}

	public void onToucheshUp(ArrayList<GPTouch> touches, float deltaTime) {}

}
