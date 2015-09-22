package com.gust.render_engine.environment;

import com.gust.engine.core.GPShader;

import android.opengl.GLES20;

public class GPPointLight {
	float[] ambient = { 0.2f, 0.2f, 0.2f, 1 };
	float[] diffuse = { 0.2f, 0.2f, 0.2f, 1 };
	float[] specular = { 1f, 1f, 1f, 1 };
	//点光源的postion数组的第4位必须是1；线光源的direction数组的第4位必须是0
	float[] postion = { 0, 0, 0, 1 };
	float[] direction = {0, 0, -1};
	
	float angle = 180;
	float exponent = 1;
	
	private int positionHandler;
	private int ambientColorHandler;
	private int diffuseColorHandler;
	private int specularColorHandler;
	private int dirctionHandler;
	private int angleHandler;
	private int exponentHandler;
	private GPShader shader;

	public GPPointLight(GPShader shader){
		this.shader = shader;
		ambientColorHandler = shader.getUniformLocation("ambientColor");
		diffuseColorHandler = shader.getUniformLocation("diffuseColor");
		specularColorHandler = shader.getUniformLocation("specularColor");
		positionHandler = shader.getUniformLocation("lightPosition");
		dirctionHandler = shader.getUniformLocation("lightDirction");
		angleHandler = shader.getUniformLocation("lightAngle");
		exponentHandler = shader.getUniformLocation("exponent");
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

	public void setPostion(float x, float y, float z)
	{
		postion[0] = x;
		postion[1] = y;
		postion[2] = z;
	}
	
	public void setDirection(float x, float y, float z)
	{
		direction[0] = x;
		direction[1] = y;
		direction[2] = z;
	}
	/**
	 * 用于模拟手电等类型的光源
	 * @param angle 光的总角度/2
	 */
	public void setAngle(float angle){
		this.angle = angle;
	}
	/**
	 * 设置光的衰减系数
	 * @param exponent 衰减系数
	 */
	
	public void setExponent(float exponent){
		this.exponent = exponent;
	}
	
	/**
	 * 启用点光源
	 */
	public void enable()
	{
		shader.bind();
		GLES20.glUniform4fv(ambientColorHandler, 1, ambient, 0);
		GLES20.glUniform4fv(diffuseColorHandler, 1, diffuse, 0);
		GLES20.glUniform4fv(specularColorHandler, 1, specular, 0);
		GLES20.glUniform4fv(positionHandler, 1, postion, 0);
		GLES20.glUniform3fv(dirctionHandler, 1, direction, 0);
		GLES20.glUniform1f(angleHandler, angle);
		GLES20.glUniform1f(exponentHandler, exponent);
	}
}
