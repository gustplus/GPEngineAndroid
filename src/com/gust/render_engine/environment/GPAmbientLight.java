package com.gust.render_engine.environment;

import com.gust.engine.core.GPShader;

import android.opengl.GLES20;
/**
 * 用于实现游戏场景中的环境光，默认为 （0.2f, 0.2f, 0.2f, 1）
 * @author gustplus
 *
 */
public class GPAmbientLight {
	float[] color = { 0.2f, 0.2f, 0.2f, 1 };
	private int colorHandler;
	/**
	 * 
	 * @param programNum 传入加载的shader脚本，推荐为Shaders.SHADER_XXXLIGHT
	 */
	public GPAmbientLight(GPShader shader){
		colorHandler = shader.getUniformLocation("ambientColor");
	}

	/**
	 * 启用环境光
	 */
	public void enable()
	{
		GLES20.glUniform4fv(colorHandler, 1, color, 0);
	}
	/**
	 * 设置环境光参数
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void setLight(float r, float g, float b, float a)
	{
		color[0] = r;
		color[1] = g;
		color[2] = b;
		color[3] = a;
	}
}
