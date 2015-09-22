package com.gust.system;

/**
 * 用于游戏的流程控制的屏幕类
 * 
 * @author gustplus
 * 
 */
public abstract class GPScreen {
//	protected final GPGame game;
	protected String tag;

	public GPScreen(GPGame game)
	{
//		this.game = game;
	}
	
//	public abstract void initShadersAndTexture();

	/**
	 * 屏幕内容的更新方法
	 * 
	 * @param deltaTime当前帧的时间长度
	 */
	public abstract void update(float deltaTime);
	
	/**
	 * 呈现当前帧内容的方法
	 * 
	 * @param deltaTime当前帧的时间长度
	 */
	public abstract void present(float deltaTime);

	public abstract void pause();

	public abstract void resume();

	public abstract void dispose();
	
	
	
}
