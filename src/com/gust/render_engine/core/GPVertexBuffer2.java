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
	 * �����������󶥵���������������
	 * @param programNum��Ⱦ�����
	 * @param maxVertices�������󶥵�����
	 * @param maxIndices����������������
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
	 * ���ö�����Ϣ
	 * @param vertices��������
	 * @param offsetƫ����
	 * @param lengthʹ�õĳ���
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
	 * ����������Ϣ
	 * @param indices��������
	 * @param offsetƫ����
	 * @param lengthʹ�õĳ���
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
	 * ���ö�����ɫ����
	 * @param colors������ɫ����
	 * @param offsetƫ����
	 * @param lengthʹ�õĳ���
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
	 * ���ö�������������Ϣ
	 * @param texCoords������������
	 * @param offsetƫ����
	 * @param lengthʹ�õĳ���
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
	 * �󶨶�����Ϣ����Ⱦ
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
	 * �����ȵ���bind��������Ⱦ������
	 * @param primitiveMode�������֯��״���㡢�ߡ������Σ�
	 * @param offsetƫ����
	 * @param num��Ⱦ�Ķ�����Ŀ
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
	 * ȡ�ö�����Ŀ
	 * @return int������Ŀ
	 */
	public int getNumVertices()
	{
		return verticesLength / 2;
	}

	/**
	 * ȡ��������Ŀ
	 * @return int������Ŀ
	 */
	public int getNumIndices()
	{
		return indicesLength;
	}
}
