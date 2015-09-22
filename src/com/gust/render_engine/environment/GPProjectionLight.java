package com.gust.render_engine.environment;

import com.gust.common.math.GPConstants;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.base.GPMatrixState;
import com.gust.render_engine.core.GPTexture_20;

import android.opengl.GLES20;
/**
 * 投射光源
 * @author gustplus
 *
 */
public class GPProjectionLight {
	float[] ambient = { 0.2f, 0.2f, 0.2f, 1 };
	float[] diffuse = { 0.2f, 0.2f, 0.2f, 1 };
	float[] specular = { 1f, 1f, 1f, 1 };
	// 点光源的position数组的第4位必须是1；线光源的direction数组的第4位必须是0
	float[] position = { 0, 0, 0, 1 };
	float[] direction = { 0, 0, -1 };

	float angle;
	float pictureDest;

	private int positionHandler;
	private int ambientColorHandler;
	private int diffuseColorHandler;
	private int specularColorHandler;
	private int dirctionHandler;
	private int angleHandler;
	private int texHandler;
	
	private GPTexture_20 picture;

	/**
	 * 
	 * @param programNum 渲染器编号
	 * @param picture 投影的图像
	 * @param pictureDest 投影图像与光源的距离
	 */
	public GPProjectionLight(GPShader shader, GPTexture_20 picture, float pictureDest)
	{
		this.picture = picture;
		this.pictureDest = pictureDest;
		ambientColorHandler = shader.getUniformLocation("ambientColor");
		diffuseColorHandler = shader.getUniformLocation("diffuseColor");
		specularColorHandler = shader.getUniformLocation("specularColor");
		positionHandler = shader.getUniformLocation("lightPosition");
		dirctionHandler = shader.getUniformLocation("lightDirction");
		angleHandler = shader.getUniformLocation("lightAngle");
		texHandler = shader.getUniformLocation("trueTexture");
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

	public void setPosition(float x, float y, float z)
	{
		position[0] = x;
		position[1] = y;
		position[2] = z;
	}

	/**
	 * 设置投影方向
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setDirection(float x, float y, float z)
	{
		direction[0] = x;
		direction[1] = y;
		direction[2] = z;
	}

	/**
	 * 启用投影光源
	 */
	public void enable()
	{
		GPMatrixState.pushMatrix();
			GPMatrixState.loadIdentity(GPMatrixState.PROJESTION_MODE);
			GPMatrixState.Frustum(-0.5f, 0.5f, -0.5f, 0.5f, pictureDest, 500);
			GPMatrixState.loadIdentity(GPMatrixState.VIEW_MODE);
			GPMatrixState.lookatf(position[0], position[1], position[2], position[0]
				+ direction[0], position[1] + direction[1], position[2]
				+ direction[2], direction[0], 1 + direction[1], direction[2]);
			GPMatrixState.setProjViewMatrix();
		GPMatrixState.popMatrix();
		
		angle = (float)Math.atan(0.5/pictureDest)*GPConstants.TO_DEGREES;
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		picture.bind();
		GLES20.glUniform1i(texHandler, 1);
		GLES20.glUniform4fv(ambientColorHandler, 1, ambient, 0);
		GLES20.glUniform4fv(diffuseColorHandler, 1, diffuse, 0);
		GLES20.glUniform4fv(specularColorHandler, 1, specular, 0);
		GLES20.glUniform4fv(positionHandler, 1, position, 0);
		GLES20.glUniform3fv(dirctionHandler, 1, direction, 0);
		GLES20.glUniform1f(angleHandler, angle);
	}
}
