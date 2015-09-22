package com.gust.common.ui;

import com.gust.action.GPAction2D;
import com.gust.action.GPActionMoveBy2D;
import com.gust.action.GPActionSpawn;
import com.gust.common.game_util.GPLogger;
import com.gust.common.math.GPConstants;
import com.gust.common.math.GPVector2f;
import com.gust.render_engine.core.GLStencilCommond;
import com.gust.render_engine.core.GPOpenGLStateManager;
import com.gust.scene2D.GPLayer2D;
import com.gust.scene2D.GPNode2D;
import com.gust.system.GPInput.GPTouch;

public class GPScrollView extends GPLayer2D {

	protected GPVector2f[] pressdownPositions;
	protected int touchNum;
	protected float touchTime; // 在某一点按下不移动的时间
	protected float zoomSize;
	protected float origionZoomSize;
	protected GPNode2D container;

	protected GPVector2f direction;
	protected GPVector2f dragVector;
	protected GPVector2f movement;
	protected int accel = -1800;
	protected float oneOfSccel = -1f / 1800;
	protected float velocity;

	protected boolean shouldAnimate = true;

	protected boolean isAnimated = false;

	protected GPActionSpawn fixAction;

	private GPVector2f touchCenter;

	private GPVector2f PositionOffset;

	protected boolean isContainerWidthBigger;
	protected boolean isContainerHeightBiger;

	public enum ScrollType {
		ScrollType_V, ScrollType_H, ScrollType_VH
	};

	protected ScrollType type;

	private GPScrollViewAdpter scrollAdpter;

	public static interface GPScrollViewAdpter {
		public void onScroll(GPScrollView view);

		public void onZoom(GPScrollView view);

		public void onFix(GPScrollView view);
	}

	// private final float maxSwllowTime = 0.3f;

	private static final int maxMove = 200; // container最大移动距离

	public GPScrollView(float x, float y, float width, float height,
			ScrollType type) {
		super(x, y, width, height);
		this.type = type;
		this.setcontentSize(bound.clone());
		pressdownPositions = new GPVector2f[] { new GPVector2f(),
				new GPVector2f() };
		touchNum = 0;
		zoomSize = 1;
		container = null;
		direction = new GPVector2f();
		dragVector = new GPVector2f();
		movement = new GPVector2f();
		velocity = 0;
		isContainerHeightBiger = false;
		isContainerWidthBigger = false;
	}

	public void setContainer(GPNode2D container) {
		if (this.container != null) {
			this.removeAllChildren();
		}
		this.container = container;
		container.setAnthorPoint(new GPVector2f());
		this.addChild(container);

		if (container.getBoundBox().width > contentBound.width) {
			isContainerWidthBigger = true;
		}
		if (container.getBoundBox().height > contentBound.height) {
			isContainerHeightBiger = true;
		}

		// fixContainerOffset();
	}

	public void drawself() {
	}

	public boolean onTouchDown(GPTouch touch, float deltaTime) {
		touchTime = 0;
		if (touchNum > 1) {
			return false;
		}
		pressdownPositions[touchNum].set(touch.x, touch.y);
		++touchNum;

		if (isAnimated) {
			isAnimated = false;
			velocity = 0;
			direction.set(0, 0);
			dragVector.set(0, 0);
		}

		if (isAnimated) {
			isAnimated = false;
		}

		if (touchNum == 2) {
			// touchCenter = pressdownPositions[1].sub(pressdownPositions[0]);
			// origionZoomSize = touchCenter.len();
			// touchCenter = pressdownPositions[0].add(touchCenter.mul(0.5f));
			// PositionOffset =
			// touchCenter.sub(position).sub(container.getPosition());
		}

		return true;
	}

	public void setScrollAdpter(GPScrollViewAdpter scrollAdpter) {
		this.scrollAdpter = scrollAdpter;
	}

	public void onTouchDrag(GPTouch touch, float deltaTime) {
		touchTime += deltaTime;

		if (touchNum == 1) { // 滑动
			if (container != null) {
				direction = new GPVector2f(touch.x, touch.y)
						.sub(pressdownPositions[0]);
				if (type == ScrollType.ScrollType_V) {
					direction.x = 0;
				}
				if (type == ScrollType.ScrollType_H) {
					direction.y = 0;
				}
				dragVector.addTo(direction);
				if (direction.len() < 10) {
					dragVector.set(0, 0);
				}

				container.move(direction);
				GPVector2f fixVector = getFixVector();
				float x = Math.abs(fixVector.x);
				float y = Math.abs(fixVector.y);

				if (x > maxMove) {
					x = maxMove;
				}
				if (y > maxMove) {
					y = maxMove;
				}

				x = x / maxMove;
				y = y / maxMove;

				if (type == ScrollType.ScrollType_V) {
					x = 0;
				}
				if (type == ScrollType.ScrollType_H) {
					y = 0;
				}
				movement.set(direction.mul(-x, -y));
				container.move(movement);

				if (scrollAdpter != null) {
					scrollAdpter.onScroll(this);
				}
			}
		}

		int index = pointers.indexOf(touch.pointer);
		pressdownPositions[index].set(touch.x, touch.y);

		// if(touchTime > maxSwllowTime){
		// if(moveLength.len > 50){
		//
		// }
		// }

		if (touchNum == 2) { // 放缩
		// float zoom =
		// (pressdownPositions[1].sub(pressdownPositions[0])).len()/origionZoomSize;
		// container.setScaleX(zoom);
		// container.setScaleY(zoom);
		// touchCenter =
		// (pressdownPositions[1].add(pressdownPositions[0])).mul(0.5f);
		// container.setPosition(touchCenter.sub(position).sub(PositionOffset.mul(zoom)));
			if (scrollAdpter != null) {
				scrollAdpter.onZoom(this);
			}
		}
	}

	/*
	 * i must override this function because if i add the check code in "calBoundingbox" function, 
	 * the "modelIsCurrent" flag can also be changed by the change of child's position,
	 * but we only should recheck when child's bound is changed
	 */
	protected void updateWorldPosition() {
		super.updateWorldPosition();
		if (!modelIsCurrent) {
			if (container.getBoundBox().width > contentBound.width) {
				isContainerWidthBigger = true;
			} else {
				isContainerWidthBigger = false;
			}
			if (container.getBoundBox().height > contentBound.height) {
				isContainerHeightBiger = true;
			} else {
				isContainerHeightBiger = false;
			}
		}
	}

	public void onTouchUp(GPTouch touch, float deltaTime) {
		if (touchNum == 1) { // 滑动
			pressdownPositions[0].set(0, 0);
		}

		if (touchNum == 2) { // 放缩
		// origionZoomSize =
		// (pressdownPositions[1].sub(pressdownPositions[0])).len()/origionZoomSize;
		}
		--touchNum;
		int index = pointers.indexOf(touch.pointer);
		if (index == 0) {
			pressdownPositions[0].set(pressdownPositions[1]);
		}
		pressdownPositions[1].set(-1, -1);

		velocity = dragVector.len() / touchTime;
		direction.normalize();
		if (velocity > -GPConstants.almostZero) {
			velocity *= 10;
			if (velocity > 1000) {
				velocity = 1000;
			}
			isAnimated = true;
		} else {
			movement.set(0, 0);
		}
	}

	public void update(float deltaTime) {
		if (velocity < 0) {
			velocity = 0;
			fixContainerOffset(getFixVector());
			movement.set(0, 0);
			isAnimated = false;
		}
		if (isAnimated) {
			movement.set(direction.mul(velocity * deltaTime));
			container.move(movement);
			velocity += accel * deltaTime;

			if (scrollAdpter != null) {
				scrollAdpter.onScroll(this);
			}
		}
	}

	public GPVector2f getContentOffset() {
		return container.getPosition();
	}

	public void beforeDraw() {
//		GPOpenGLStateManager.getInstance().enableScissor(true);
//		GPOpenGLStateManager.getInstance().scissor(contentBound);
		GPOpenGLStateManager.getInstance().pushCommond(new GLStencilCommond(contentBound));
	}

	public void afterDraw() {
		GPOpenGLStateManager.getInstance().popCommond();
	}

	private void fixContainerOffset(GPVector2f fixVector) {
		if (fixVector.x != 0 || fixVector.y != 0) {
			isAnimated = false;
		}
		if (fixAction == null) {
			fixAction = new GPActionSpawn(new GPAction2D[] {
					new GPActionMoveBy2D(new GPVector2f(fixVector.x, 0),
							Math.abs(fixVector.x * oneOfSccel)),
					new GPActionMoveBy2D(new GPVector2f(0, fixVector.y),
							Math.abs(fixVector.y * oneOfSccel)) });
		} else {
			fixAction.setActions(new GPAction2D[] {
					new GPActionMoveBy2D(new GPVector2f(fixVector.x, 0), Math
							.abs(fixVector.x * oneOfSccel)),
					new GPActionMoveBy2D(new GPVector2f(0, fixVector.y), Math
							.abs(fixVector.y * oneOfSccel)) });
		}
		container.runAction(fixAction);
	}

	private GPVector2f getFixVector() {
		float x = 0;
		float y = 0;
		float offsetL = contentBound.lowerLeft.x
				- container.getBoundBox().lowerLeft.x;
		float offsetR = offsetL + contentBound.width
				- container.getBoundBox().width;
		if (isContainerWidthBigger) {
			if (offsetL < 0) {
				x = offsetL;
			}
			if (offsetR > 0) {
				x = offsetR;
			}
		} else {
			if (offsetL > 0) {
				x = offsetL;
			}
			if (offsetR < 0) {
				x = offsetR;
			}
		}

		offsetL = contentBound.lowerLeft.y
				- container.getBoundBox().lowerLeft.y;
		offsetR = offsetL + contentBound.height
				- container.getBoundBox().height;
		if (isContainerHeightBiger) {
			if (offsetL < 0) {
				y = offsetL;
			}
			if (offsetR > 0) {
				y = offsetR;
			}
		} else {
			if (offsetL > 0) {
				y = offsetL;
			}
			if (offsetR < 0) {
				y = offsetR;
			}
		}
		return new GPVector2f(x, y);
	}

}
