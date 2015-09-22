package com.gust.render_engine.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.gust.engine.core.GPShader;
import com.gust.render_engine.base.GPMatrixState;

import android.opengl.GLES20;

public class GPVertexBuffer2 {
	private boolean hasColor;
	private boolean hasTexCoords;
	private IntBuffer vertices;
	private IntBuffer colors;
	private IntBuffer texCoords;
	private ShortBuffer indices;

	private int[] vertices_int;
	private int verticesLength;

	private int vetricesHandler;
	private int colorHandler;
	private int texCoorHandler;
	private int alphaHandler;

//	private int programNum;
	private GPShader shader;
	private int indicesLength;
	private float alpha;

	/**
	 * 设置允许的最大顶点数量及索引数量
	 * @param programNum渲染器编号
	 * @param maxVertices允许的最大顶点数量
	 * @param maxIndices允许的最大索引数量
	 */
	public GPVertexBuffer2(GPShader shader, int maxVertices, int maxIndices)
	{
//		this.programNum = programNum;
		this.shader = shader;
		this.vertices_int = new int[2 * maxVertices];
		this.alpha = 1;

		ByteBuffer buffer = ByteBuffer.allocateDirect(2 * 4 * maxVertices);
		buffer.order(ByteOrder.nativeOrder());
		vertices = buffer.asIntBuffer();

		if (maxIndices > 0) {
			buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
			buffer.order(ByteOrder.nativeOrder());
			indices = buffer.asShortBuffer();
		} else {
			indices = null;
		}
		alphaHandler = shader.getUniformLocation("alpha");
//		alphaHandler = GLES20.glGetUniformLocation(programNum, "alpha");
	}

	/**
	 * 设置顶点信息
	 * @param vertices顶点数组
	 * @param offset偏移量
	 * @param length使用的长度
	 */
	public void setVertices(float[] vertices, int offset, int length)
	{
		this.verticesLength = length;
		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++) {
			vertices_int[j] = Float.floatToRawIntBits(vertices[i]);
		}
		this.vertices.clear();
		this.vertices.put(vertices_int, 0, length);
		this.vertices.flip();
//		vetricesHandler = GLES20.glGetAttribLocation(programNum, "aPosition");
		vetricesHandler = shader.getAttribLocation("aPosition");
	}

	/**
	 * 设置索引信息
	 * @param indices索引数组
	 * @param offset偏移量
	 * @param length使用的长度
	 */
	public void setIndices(short[] indices, int offset, int length)
	{
		this.indicesLength = length;
		this.indices.clear();
		this.indices.position(0);
		this.indices.put(indices, offset, length);
		this.indices.flip();
	}

	/**
	 * 设置顶点颜色数组
	 * @param colors顶点颜色数组
	 * @param offset偏移量
	 * @param length使用的长度
	 */
	public void setColors(float[] colors, int offset, int length)
	{
		hasColor = true;
		ByteBuffer buffer = ByteBuffer.allocateDirect(colors.length * 4);
		buffer.order(ByteOrder.nativeOrder());
		this.colors = buffer.asIntBuffer();
		int[] colorbuffer = new int[colors.length];
		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++) {
			colorbuffer[j] = Float.floatToRawIntBits(colors[i]);
		}
		this.colors.clear();
		this.colors.put(colorbuffer, 0, length);
		this.colors.flip();
		colorHandler = shader.getAttribLocation("aColor");
	}

	/**
	 * 设置顶点纹理坐标信息
	 * @param texCoords纹理坐标数组
	 * @param offset偏移量
	 * @param length使用的长度
	 */
	public void setTexCoords(float[] texCoords, int offset, int length)
	{
		hasTexCoords = true;
		ByteBuffer buffer = ByteBuffer.allocateDirect(texCoords.length * 4);
		buffer.order(ByteOrder.nativeOrder());
		this.texCoords = buffer.asIntBuffer();
		int[] texCoordBuffer = new int[texCoords.length];
		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++) {
			texCoordBuffer[j] = Float.floatToRawIntBits(texCoords[i]);
		}
		this.texCoords.clear();
		this.texCoords.put(texCoordBuffer, 0, length);
		this.texCoords.flip();
		texCoorHandler = shader.getAttribLocation("aTexCoor");
	}
	
	public void setAlpha(float alpha){
		this.alpha = alpha;
	}
	
	public GPShader getShader(){
		return shader;
	}

	/**
	 * 绑定顶点信息供渲染
	 */
	public void bind()
	{
		shader.bind();
		if (verticesLength > 0) {
			GLES20.glVertexAttribPointer(vetricesHandler, 2, GLES20.GL_FLOAT,
					false, 2 * 4, vertices);
			GLES20.glEnableVertexAttribArray(vetricesHandler);

			if (hasColor) {
				GLES20.glVertexAttribPointer(colorHandler, 4, GLES20.GL_FLOAT,
						false, 4 * 4, colors);
				GLES20.glEnableVertexAttribArray(colorHandler);
			}

			if (hasTexCoords) {
				GLES20.glVertexAttribPointer(texCoorHandler, 2,
						GLES20.GL_FLOAT, false, 2 * 4, texCoords);
				GLES20.glEnableVertexAttribArray(texCoorHandler);
			}
			GLES20.glUniform1f(alphaHandler, alpha);
		}
	}

	/**
	 * 必须先调用bind（），渲染顶点组
	 * @param primitiveMode顶点的组织形状（点、线、三角形）
	 * @param offset偏移量
	 * @param num渲染的顶点数目
	 */
	public void draw(int primitiveMode, int offset, int num)
	{
//		GLES20.glUseProgram(this.programNum);
		shader.bind();
		GPMatrixState.setShader(shader);
		GPMatrixState.setTotalMatrix();
		GPMatrixState.setModelMatrix();
		GPMatrixState.setCameraMatrix();

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

		if (indices != null) {
			indices.position(offset);
			GLES20.glDrawElements(primitiveMode, num, GLES20.GL_UNSIGNED_SHORT,
					indices);
		} else {
			GLES20.glDrawArrays(primitiveMode, offset, num);
		}
	}

	/**
	 * 取得顶点数目
	 * @return int顶点数目
	 */
	public int getNumVertices()
	{
		return verticesLength / 2;
	}

	/**
	 * 取得索引数目
	 * @return int索引数目
	 */
	public int getNumIndices()
	{
		return indicesLength;
	}
}
