package com.gust.scene3D;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.gust.common.math.GPMatrix4;
import com.gust.engine.core.GPShader;
import com.gust.engine.core.GPShaderManager;
import com.gust.render_engine.base.MatrixHelper;

import android.opengl.GLES20;

/**
 * 存放顶点相关信息的类
 * 
 * @author gustplus
 * 
 */
public class GPDebugVertex3D {
	private IntBuffer vertices;
	private IntBuffer colors;
	private ShortBuffer indices;

	private int verticesLength;

	private int vetricesHandler;
	private int colorHandler;

	private int indicesLength;
	private GPShader shader;
	
	private GPMatrix4 matrix;

	/**
	 * 
	 * @param programNum渲染器编号
	 * @param willChange顶点数据是否可能改变
	 */
	public GPDebugVertex3D(GPMatrix4 matrix) {
		this.matrix = matrix;
		this.shader = GPShaderManager.getInstance().getShader("debug");
		indices = null;
	}
	
	public GPDebugVertex3D() {
		this.matrix = null;
		this.shader = GPShaderManager.getInstance().getShader("debug");
		indices = null;
	}

	public GPShader getShader() {
		return shader;
	}

	public GPMatrix4 getMatrix() {
		return matrix;
	}

	public void setMatrix(GPMatrix4 matrix) {
		this.matrix = matrix;
	}

	/**
	 * 设置顶点信息
	 * 
	 * @param vertices顶点数组
	 * @param offset偏移量
	 * @param length使用的长度
	 */
	public void setVertices(float[] vertices, int offset, int length) {
		if (verticesLength < length) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(4 * length);
			buffer.order(ByteOrder.nativeOrder());
			this.vertices = buffer.asIntBuffer();
		}
		int[] vertices_int = new int[vertices.length];
		this.verticesLength = length;
		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++) {
			vertices_int[j] = Float.floatToRawIntBits(vertices[i]);
		}
		this.vertices.clear();
		this.vertices.position(0);
		this.vertices.put(vertices_int, 0, length);
		this.vertices.flip();
		this.vertices.position(0);
		vetricesHandler = shader.getAttribLocation("aPosition");
	}

	/**
	 * 设置索引信息
	 * 
	 * @param indices索引数组
	 * @param offset偏移量
	 * @param length使用的长度
	 */
	public void setIndices(short[] indices, int offset, int length) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(length * Short.SIZE / 8);
		buffer.order(ByteOrder.nativeOrder());
		this.indices = buffer.asShortBuffer();
		this.indicesLength = length;
		this.indices.clear();
		this.indices.position(0);
		this.indices.put(indices, offset, length);
		this.indices.flip();
		this.indices.position(0);
	}

	/**
	 * 设置顶点颜色数组
	 * 
	 * @param colors顶点颜色数组
	 * @param offset偏移量
	 * @param length使用的长度
	 */
	public void setColors(float[] colors, int offset, int length) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(colors.length * 4);
		buffer.order(ByteOrder.nativeOrder());
		this.colors = buffer.asIntBuffer();
		int[] colorbuffer = new int[colors.length];
		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++) {
			colorbuffer[j] = Float.floatToRawIntBits(colors[i]);
		}
		this.colors.clear();
		this.colors.position(0);
		this.colors.put(colorbuffer, 0, length);
		this.colors.flip();
		colors = null;
		colorbuffer = null;
		colorHandler = shader.getAttribLocation("aColor");
		this.colors.position(0);
	}

	public float[] getVertices() {
		float[] vertices = new float[verticesLength];
		int[] verticesInt = new int[verticesLength];
		this.vertices.position(0);
		this.vertices.get(verticesInt);
		for (int i = 0; i < verticesLength; ++i) {
			vertices[i] = Float.intBitsToFloat(verticesInt[i]);
		}
		return vertices;
	}

	public short[] getIndices() {
		this.indices.capacity();
		float[] indices = new float[verticesLength / 2];
		short[] indicesShort = new short[verticesLength / 2];
		this.indices.position(0);
		this.indices.get(indicesShort);
		for (int i = 0; i < verticesLength / 2; ++i) {
			indices[i] = Float.intBitsToFloat(indicesShort[i]);
		}
		return indicesShort;
	}

	/**
	 * 绑定顶点信息供渲染
	 */
	public void bind() {
		shader.bind();
		if (verticesLength > 0) {
			GLES20.glVertexAttribPointer(vetricesHandler, 3, GLES20.GL_FLOAT,
					false, 3 * 4, vertices);
			GLES20.glEnableVertexAttribArray(vetricesHandler);
			GLES20.glVertexAttribPointer(colorHandler, 4, GLES20.GL_FLOAT,
					false, 4 * 4, colors);
			GLES20.glEnableVertexAttribArray(colorHandler);
		}
	}

	/**
	 * 必须先调用bind（），渲染顶点组
	 * 
	 * @param primitiveMode顶点的组织形状
	 *            （点、线、三角形）
	 * @param offset偏移量
	 * @param num渲染的顶点数目
	 */
	public void draw(int primitiveMode, int offset, int num) {
		MatrixHelper.getInstance().postTotalMatrix(shader);
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
	 * 
	 * @return int顶点数目
	 */
	public int getNumVertices() {
		return verticesLength / 3;
	}

	/**
	 * 取得索引数目
	 * 
	 * @return int索引数目
	 */
	public int getNumIndices() {
		return indicesLength;
	}

	public int getNum() {
		if (indicesLength > 0)
			return indicesLength;
		else
			return verticesLength / 3;
	}
}
