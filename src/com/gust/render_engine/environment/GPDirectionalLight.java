package com.gust.render_engine.environment;

import com.gust.engine.core.GPShader;

import android.opengl.GLES20;
/**
 * 用于实现游戏场景中的线光源，默认方向为（0，0，-1）
 * @author gustplus
 *
 */
public class GPDirectionalLight {
	float[] ambient = { 0.2f, 0.2f, 0.2f, 1 };
	float[] diffuse = { 1f, 1f, 1f, 1 };
	float[] specular = { 0f, 0f, 0f, 1 };
	//线光源的direction数组的第4位必须是0；点光源的postion数组的第4位必须是1
	float[] direction = { 0, 0, -1, 0 };
	
	//private float exponent = 0;
	
	//private int exponentHandler;
	private int directionHandler;
	private int ambientColorHandler;
	private int diffuseColorHandler;
	private int specularColorHandler;

	/**
	 * 
	 * @param programNum 渲染器编号，推荐为Shaders.SHADER_XXXLIGHT
	 */
	public GPDirectionalLight(GPShader shader){
		ambientColorHandler = shader.getUniformLocation("ambientColor");
		diffuseColorHandler = shader.getUniformLocation("diffuseColor");
		specularColorHandler = shader.getUniformLocation("specularColor");
		directionHandler = shader.getUniformLocation("lightDirction");
		//exponentHandler = GLES20.glGetUniformLocation(programNum, "exponent");
	}
	
	public void setAmbient(float r, float g, float b, float a)
	{
		ambient[0] = r;
		ambient[1] = g;
		ambient[2] = b;
		ambient[3] = a;
	}

	public void setDiffuse(float r, float g, float b, float a)
	{
		diffuse[0] = r;
		diffuse[1] = g;
		diffuse[2] = b;
		diffuse[3] = a;
	}

	public void setSpecular(float r, float g, float b, float a)
	{
		specular[0] = r;
		specular[1] = g;
		specular[2] = b;
		specular[3] = a;
	}

	public void setDirection(float x, float y, float z)
	{
		direction[0] = x;
		direction[1] = y;
		direction[2] = z;
	}
	
	//public void setExponent(float exponent){
	//	this.exponent = exponent;
	//}

	/**
	 * 启用线光源
	 */
	public void enable()
	{
		GLES20.glUniform4fv(ambientColorHandler, 1, ambient, 0);
		GLES20.glUniform4fv(diffuseColorHandler, 1, diffuse, 0);
		GLES20.glUniform4fv(specularColorHandler, 1, specular, 0);
		GLES20.glUniform4fv(directionHandler, 1, direction, 0);
		//GLES20.glUniform1f(exponentHandler, exponent);
	}
}
