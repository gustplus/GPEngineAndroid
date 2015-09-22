package com.gust.render_engine.environment;

import android.opengl.GLES20;

import com.gust.common.math.GPConstants;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.GPVertexBuffer3;
import com.gust.scene3D.GPVertexBuffer3D;
/**
 * 光晕类
 * @author gustplus
 *
 */
public class GPLightHalo {
	private GPVertexBuffer3D light;
	private float angle;
	private float[] rootColor = new float[] { 1, 1, 0, 0.6f };
	private float[] endColor = new float[] { 0, 0, 0, 0 };
	private int rootColorHandler;
	private int endColorHandler;
	private int nearHandler;
	private int farHandler;
	private float jitter = 1;
	private int jitterHandler;
	private GPShader shader;
	private float near;
	private float far;

	/**
	 * 光晕的总角度
	 * @param angle
	 * @param program
	 */
	public GPLightHalo(float angle, GPShader shader)
	{
		this.shader = shader;
		this.angle = angle / 2;
		jitterHandler = shader.getUniformLocation("jitter");
		nearHandler = shader.getUniformLocation("near");
		farHandler = shader.getUniformLocation("far");
		rootColorHandler = shader.getUniformLocation("rootColor");
		endColorHandler = shader.getUniformLocation("endColor");
	}

	public void setJitter(float jitter)
	{
		this.jitter = jitter;
	}
/**
 * 设置发射端颜色
 * @param red
 * @param green
 * @param blue
 * @param alpha
 */
	public void setRootColor(float red, float green, float blue,float alpha)
	{
		rootColor[0] = red;
		rootColor[1] = green;
		rootColor[2] = blue;
		rootColor[3] = alpha;
	}
/**
 * 设置消失点颜色
 * @param red
 * @param green
 * @param blue
 * @param alpha
 */
	public void setEndColor(float red, float green, float blue,float alpha)
	{
		endColor[0] = red;
		endColor[1] = green;
		endColor[2] = blue;
		endColor[3] = alpha;
	}
/**
 * 
 * @param divide将光晕细分的程度，越高越真实，不得小于3
 * @param near光晕的起始点（y轴向）
 * @param far光晕的消失点（y轴向）
 */
	public void createHalo(int divide, float near, float far)
	{
		this.near = near;
		this.far = far;
		float tan = (float) Math.tan(this.angle * GPConstants.TO_RADIANS);
		float nearR = near * tan;
		float farR = far * tan;
		float[] vertices = new float[(divide + 1) * 2 * 3];
		int index = 0;
		float divideAngle = (float) Math.PI * 2 / divide;
		float tempR = nearR;
		float temp = near;
		for (int row = 0; row < 2; row++) {
			for (int col = 0; col <= divide; col++) {
				vertices[index++] = tempR * (float) Math.cos(divideAngle * col);
				vertices[index++] = temp;
				vertices[index++] = tempR * (float) Math.sin(divideAngle * col);
			}
			tempR = farR;
			temp = far;
		}

		short[] indices = new short[(1 + divide) * 2 * 3];
		index = 0;
		for (int row = 0; row < 1; row++) {
			for (int col = 0; col < divide; col++) {
				indices[index++] = (short) (row * (divide + 1) + col);
				indices[index++] = (short) ((row + 1) * (divide + 1) + col + 1);
				indices[index++] = (short) (row * (divide + 1) + col + 1);
				indices[index++] = (short) ((row + 1) * (divide + 1) + col + 1);
				indices[index++] = (short) (row * (divide + 1) + col);
				indices[index++] = (short) ((row + 1) * (divide + 1) + col);
			}
		}

		light = new GPVertexBuffer3D(shader);
		light.setVertices(vertices, 0, vertices.length);
		light.setIndices(indices, 0, indices.length);
	}

	public void draw()
	{
		shader.bind();
		GLES20.glUniform4fv(rootColorHandler, 1, rootColor, 0);
		GLES20.glUniform4fv(endColorHandler, 1, endColor, 0);
		GLES20.glUniform1f(jitterHandler, jitter);
		GLES20.glUniform1f(nearHandler, near);
		GLES20.glUniform1f(farHandler, far);

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		light.bind();
		light.draw(GLES20.GL_TRIANGLES, 0, light.getNumIndices());
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}
