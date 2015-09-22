package com.gust.render_engine.environment;

import com.gust.engine.core.GPShader;

import android.opengl.GLES20;
/**
 * ����ʵ����Ϸ�����еĻ����⣬Ĭ��Ϊ ��0.2f, 0.2f, 0.2f, 1��
 * @author gustplus
 *
 */
public class GPAmbientLight {
	float[] color = { 0.2f, 0.2f, 0.2f, 1 };
	private int colorHandler;
	/**
	 * 
	 * @param programNum ������ص�shader�ű����Ƽ�ΪShaders.SHADER_XXXLIGHT
	 */
	public GPAmbientLight(GPShader shader){
		colorHandler = shader.getUniformLocation("ambientColor");
	}

	/**
	 * ���û�����
	 */
	public void enable()
	{
		GLES20.glUniform4fv(colorHandler, 1, color, 0);
	}
	/**
	 * ���û��������
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
