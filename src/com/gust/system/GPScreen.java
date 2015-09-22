package com.gust.system;

/**
 * ������Ϸ�����̿��Ƶ���Ļ��
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
	 * ��Ļ���ݵĸ��·���
	 * 
	 * @param deltaTime��ǰ֡��ʱ�䳤��
	 */
	public abstract void update(float deltaTime);
	
	/**
	 * ���ֵ�ǰ֡���ݵķ���
	 * 
	 * @param deltaTime��ǰ֡��ʱ�䳤��
	 */
	public abstract void present(float deltaTime);

	public abstract void pause();

	public abstract void resume();

	public abstract void dispose();
	
	
	
}
