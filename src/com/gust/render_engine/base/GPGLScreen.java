package com.gust.render_engine.base;

import java.util.ArrayList;

import com.gust.action.GPActionManager;
import com.gust.render_engine.core.GPCamera2D;
import com.gust.scene2D.GPScene2D;
import com.gust.scene3D.GPNode;
import com.gust.system.GPInput.GPTouch;

/**
 * 用于OpenGLES的屏幕类，用于游戏的流程控制
 * 
 * @author gustplus
 * 
 */
public abstract class GPGLScreen {
	protected final GPGLGame glGame;
	protected GPNode root;
	protected GPScene2D stage;
	protected String tag;
	protected GPCamera2D camera2d;

	public GPGLScreen(GPGLGame glGame) {
		this.glGame = glGame;
		this.root = new GPNode();
		this.stage = new GPScene2D(0, 0);
		this.camera2d = glGame.getCamera();
		// GPSourceManager.getInstance().loadSources(depend());
	}

	public GPCamera2D getCamera2d() {
		return camera2d;
	}

	public abstract String[] depend();

	public void pause() {
		// isInited = false;
		// GPShaderManager.reset();
	}

	public void resume() {

	}
	
	public void reloadVBO(){
		stage.reload();
	}

	public void dispose() {

	}
	
	public void finish(){
		
	}

	public void mainLoop(float deltaTime) {
		glGame.getInput().getKeyEvents();
		ArrayList<GPTouch> touches = (ArrayList<GPTouch>) glGame.getInput()
				.getTouches();
		glGame.camera2d.transformTouchs(touches);
		
		update(deltaTime, touches);
		
		this.stage.update(touches, deltaTime);
		
		int size = touches.size();
//		this.stage.sortAllChildren();

		for (int j = 0; j < size; ++j) {
			GPTouch touch = touches.get(j);
			if (stage.dispatchTouch(touch, deltaTime)) {
				touches.remove(j);
				--size;
				--j;
			}
		}
		stage.updateGeometryState();
		GPActionManager.getInstance().update(deltaTime);
		present(deltaTime);
	}

	/**
	 * 屏幕内容的更新方法
	 * 
	 * @param deltaTime当前帧的时间长度
	 */
	public abstract void update(float deltaTime, ArrayList<GPTouch> touches);

	/**
	 * 呈现当前帧内容的方法
	 * 
	 * @param deltaTime当前帧的时间长度
	 */
	public abstract void present(float deltaTime);

	public GPNode getRoot() {
		return root;
	}

	public String toString() {
		return tag;
	}

	public String getTag() {
		return tag;
	}
}
