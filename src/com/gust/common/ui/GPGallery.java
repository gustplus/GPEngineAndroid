package com.gust.common.ui;

import java.util.ArrayList;
import java.util.List;

import android.opengl.GLES20;

import com.gust.action.GPTranslateAction;
import com.gust.common.control.GPDragManager;
import com.gust.common.math.GPConstants;
import com.gust.common.math.GPVector2f;
import com.gust.common.math.GPVector3f;
import com.gust.scene2D.GPLayer2D;
import com.gust.scene2D.GPSprite;
import com.gust.system.GPInput.GPTouch;

/**
 * 2013.4.30
 * 
 * @author gustplus
 * 
 */
public class GPGallery extends GPLayer2D {

	ArrayList<GPSprite> components; // 用于存放添加的组件

	private int orientation; // 控件的转动方向
	private GPDragManager dragListener;
	private int itemsNum = 0; // 空间的数目
	private int selectedItem = 0; // 当前选择的控件索引
	private float rotateVelocity = 0; // 旋转角速度
	private float rotateAngle = 0; // 旋转角（相对于Z正半轴）
	private final static float range = 1f; // 单位角速度
	private float preComponentAngle = 0; // 每个控件相隔的角度
	private GPVector3f center;
	private float radius; // 控件的旋转半径
	private final static float friction = 70f; // 旋转阻力
	float movedAngle;

	private boolean hasInited;

	private boolean inAnimation;

	private GPTranslateAction action;

	public GPGallery(float centerX, float centerY) {
		// TODO Auto-generated constructor stub
		super(centerX, centerY, 0, 0);
		components = new ArrayList<GPSprite>();
		orientation = GPConstants.HORIZONTAL;
		center = new GPVector3f(centerX, centerY, -10);
		hasInited = false;
		inAnimation = false;
		this.action = new GPTranslateAction();
		this.anthorPoint = new GPVector2f(0.5f, 0.5f);
		this.swallow = false;
	}

	/**
	 * 设置方向，参数可为Constants.HORIZONTAL（水平）或Constants.VERTICAL（垂直），默认为Constants.
	 * HORIZONTAL
	 * 
	 * @param orientation
	 */
	public void setOrientation(int orientation) {
		this.orientation = orientation;
		hasInited = false;
	}

	/**
	 * 为gallery添加一个子控件
	 * 
	 * @param component子控件
	 */
	public void addComponent(GPSprite component) {
		component.setAnthorPoint(new GPVector2f(0.5f, 0.5f));
		components.add(component);
		++itemsNum;
		hasInited = false;
	}

	/**
	 * 移除控件
	 * 
	 * @param index需要移除的控件的索引值
	 */
	public void removeComponent(int index) {
		components.remove(index);
		--itemsNum;
		hasInited = false;
	}

	/**
	 * 在控件设置完毕后调用,生成相应初始化信息
	 */
	private void init() {
		preComponentAngle = 360 / itemsNum;
		radius = calculateRadius();
		float width = components.get(0).getWidth();
		float height = components.get(0).getHeight();
		// 根据计算出的半径算出控件新的bound
		if (orientation == GPConstants.HORIZONTAL) {
			this.getBoundBox().width = 2 * radius + width;
			this.getBoundBox().height = height;
			this.getBoundBox().lowerLeft.x = center.x;
			this.getBoundBox().lowerLeft.y = center.y;
		} else {
			this.getBoundBox().width = width;
			this.getBoundBox().height = 2 * radius + height;
			this.getBoundBox().lowerLeft.x = center.x;
			this.getBoundBox().lowerLeft.y = center.y;
		}
		setAnthorPoint(new GPVector2f(0.5f, 0.5f));
		hasInited = true;
		dragListener = new GPDragManager(this.getBoundBox());
	}

	public void update(List<GPTouch> touchEvents, 
			float deltaTime) {
		if (!visible) {
			return;
		}
		// 若未初始化，则初始化
		if (!hasInited)
			init();
		// 取得玩家的手指滑动信息
		GPVector2f drag = dragListener.getDragInfo(touchEvents, swallow);

		float dragDist;

		if (orientation == GPConstants.HORIZONTAL)
			dragDist = drag.x;
		else
			dragDist = drag.y;

		if (dragListener.isTouchDown()) {
			inAnimation = false;
			action.stop();
			// 判断手指是否在最小识别范围内，如果小于该范围则认为没有滑动
			if (dragDist < 0.05f && dragDist > -0.05f && !dragListener.isDrag())
				rotateVelocity = 0;
			else
				rotateVelocity += dragDist * range * GPConstants.TO_DEGREES;
		}
		// 如果手指在滑动，且手指未离开屏幕，则让gallery跟随手指的滑动而转动
		if (dragListener.isDrag() && dragListener.isTouchDown()) {
			inAnimation = false;
			action.stop();
			rotateAngle += dragDist * 0.1f * GPConstants.TO_DEGREES;
			movedAngle += dragDist * 0.1f * GPConstants.TO_DEGREES;
			if (dragDist < 0.5f && dragDist > -0.5f)
				rotateVelocity = 0;
		}
		// 在gallery自由转动时
		if (!dragListener.isTouchDown() && rotateVelocity != 0) {
			inAnimation = false;
			action.stop();
			float tempVelocity = rotateVelocity;
			// 若某控件转动了各控件相隔的角度，则施加一个阻力以减速
			if (Math.abs(movedAngle) - preComponentAngle > 0) {
				if (rotateVelocity > 0) {
					rotateVelocity -= friction;
					movedAngle -= preComponentAngle;
				} else {
					rotateVelocity += friction;
					movedAngle += preComponentAngle;
				}
			}
			rotateAngle += rotateVelocity * deltaTime;
			movedAngle += rotateVelocity * deltaTime;
			// 当前一帧的速度与当前的速度相反时停止转动
			if (rotateVelocity * tempVelocity < 0) {
				rotateVelocity = 0;
			}
		}
		// 当速度过小时，停止转动
		if (Math.abs(rotateVelocity) < 50) {
			inAnimation = false;
			action.stop();
			rotateVelocity = 0;
		}
		// 根据角度信息计算出选中的子控件索引
		if (rotateVelocity == 0) {
			inAnimation = false;
			action.stop();
			selectedItem = -(int) Math.rint((double) rotateAngle
					/ preComponentAngle);
			while (selectedItem < 0)
				selectedItem += itemsNum;
			while (selectedItem >= itemsNum)
				selectedItem -= itemsNum;
		}

		if (rotateAngle > 360) {
			rotateAngle -= 360;
		}
		if (rotateAngle < 0) {
			rotateAngle += 360;
		}

		if (!dragListener.isTouchDown() && rotateVelocity == 0) {
			float allAngle = (rotateAngle) % preComponentAngle;
			if (allAngle > preComponentAngle / 2) {
				allAngle = preComponentAngle - allAngle;
			} else {
				allAngle = -allAngle;
			}
			if (allAngle != 0 && !inAnimation) {
				action.set(allAngle, Math.abs(allAngle) / 90 * 0.7f);
				action.start();
				inAnimation = true;
			}
		}

		action.update(deltaTime);

		if (inAnimation) {
			rotateAngle += action.getTranslate();
			if (action.isDone())
				action.stop();
			inAnimation = false;
		}

		// 更新各组件的位置
		float componentAngle = 0;
		GPVector3f position;
		if (orientation == GPConstants.HORIZONTAL)
			for (int i = 0; i < itemsNum; ++i) {
				componentAngle = rotateAngle + preComponentAngle * i;
				position = new GPVector3f(radius
						* (float) Math.sin(componentAngle
								* GPConstants.TO_RADIANS), 0, radius
						* (float) Math.cos(componentAngle
								* GPConstants.TO_RADIANS));
				//position.addTo(center);
				GPSprite temp = components.get(i);
				temp.depthZ = position.z - radius;
				temp.setPosition(new GPVector2f(getBoundBox().width/2 + position.x
						, getBoundBox().height/2 + position.y
						));
				// temp.bound.lowerLeft.x = position.x - temp.bound.width / 2;
				// temp.bound.lowerLeft.y = position.y - temp.bound.height / 2;
			}
		else {
			for (int i = 0; i < itemsNum; ++i) {
				componentAngle = rotateAngle + preComponentAngle * i;
				position = new GPVector3f(0, radius
						* (float) Math.sin(componentAngle
								* GPConstants.TO_RADIANS), radius
						* (float) Math.cos(componentAngle
								* GPConstants.TO_RADIANS));
				//position.addTo(center);
				GPSprite temp = components.get(i);
				temp.depthZ = position.z - radius;
				temp.setPosition(new GPVector2f(getBoundBox().width/2 + position.x
						, getBoundBox().height/2 + position.y
						));
				// temp.bound.lowerLeft.x = position.x - temp.bound.width / 2;
				// temp.bound.lowerLeft.y = position.y - temp.bound.height / 2;
			}
		}
		selectedItem = -(int) Math.rint((double) rotateAngle
				/ preComponentAngle);
		while (selectedItem < 0)
			selectedItem += itemsNum;
		while (selectedItem >= itemsNum)
			selectedItem -= itemsNum;
	}

	@Override
	public void setPosition(GPVector2f position) {
		// TODO Auto-generated method stub
		this.center = new GPVector3f(position.x, position.y, center.z);
		super.setPosition(position);
	}

	public void drawself(float offset) {
		// TODO Auto-generated method stub
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		for (GPSprite component : components) {
			component.setScaleX(0.8f + 0.2f * ((component.depthZ + radius) / (2 * radius)));
			component.setScaleY(0.8f + 0.2f * ((component.depthZ + radius) / (2 * radius)));
			component.draw();
		}
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
	}

	public void drawself() {
		this.drawself(0);
	}

	private float calculateRadius() {
		float radius;
		if (orientation == GPConstants.HORIZONTAL)
			radius = components.get(0).getWidth()
					/ 2
					/ (float) Math.sin(preComponentAngle / 2
							* GPConstants.TO_RADIANS);
		else
			radius = components.get(0).getHeight()
					/ 2
					/ (float) Math.sin(preComponentAngle / 2
							* GPConstants.TO_RADIANS);
		return radius;
	}

	public int getSelectedIndex() {
		return selectedItem;
	}

	public GPSprite getSelectedComponent() {
		return components.get(selectedItem);
	}

}
