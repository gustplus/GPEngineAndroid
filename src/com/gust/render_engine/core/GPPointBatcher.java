package com.gust.render_engine.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import com.gust.engine.core.GPShader;
import com.gust.render_engine.base.GPMatrixState;

import android.opengl.GLES20;

public class GPPointBatcher {

	private IntBuffer vertices;
	private IntBuffer colors;
	private float points[];
	private float color[];
	private int vIndex;
	private int cIndex;
	private int zOrder;
	private int vetricesHandler;
	private int colorHandler;
	private int sizeHandler;
//	private int program;
	private float alpha;
	private float pointSize;
	
	private int maxNum;
	
	private GPShader shader;

	public GPPointBatcher(int maxPoints, GPShader shader) {
		// TODO Auto-generated constructor stub
//		this.program = program;
		this.shader = shader;
		this.maxNum = maxPoints;
		ByteBuffer buffer = ByteBuffer.allocateDirect(maxPoints * 3 * 4);
		buffer.order(ByteOrder.nativeOrder());
		vertices = buffer.asIntBuffer();
		vIndex = 0;
		cIndex = 0;
		points = new float[maxPoints * 3];
		this.alpha = 1;

		this.zOrder = 0;
		this.pointSize = 4;

		buffer = ByteBuffer.allocateDirect(maxPoints * 4 * 4);
		buffer.order(ByteOrder.nativeOrder());
		this.colors = buffer.asIntBuffer();
		color = new float[maxPoints * 4];

		vetricesHandler =shader.getUniformLocation("aPosition");
		colorHandler = shader.getUniformLocation("aColor");
		sizeHandler = shader.getUniformLocation("pointSize");
	}

	public void setZOrder(int zOrder) {
		this.zOrder = zOrder;
	}

	public void setPointSize(float pointSize) {
		this.pointSize = pointSize;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public void addPoint(float x, float y, float r, float g, float b) {
		if (maxNum - vIndex/3 > 0) {
			points[vIndex++] = x;
			color[cIndex++] = r;
			points[vIndex++] = y;
			color[cIndex++] = g;
			points[vIndex++] = zOrder;
			color[cIndex++] = b;
			color[cIndex++] = alpha;
		}
	}

	public void beginBatch() {
		if (vIndex > 0) {
			GLES20.glUseProgram(shader.getProgram());
			int pointsInt[] = new int[vIndex];
			for (int i = 0; i < vIndex; ++i) {
				pointsInt[i] = Float.floatToRawIntBits(points[i]);
			}
			this.vertices.clear();
			this.vertices.position(0);
			this.vertices.put(pointsInt, 0, vIndex);
			this.vertices.flip();
			GLES20.glVertexAttribPointer(vetricesHandler, 3, GLES20.GL_FLOAT,
					false, 3 * 4, vertices);
			GLES20.glEnableVertexAttribArray(vetricesHandler);
			
			int colorInt[] = new int[cIndex];
			for (int i = 0; i < cIndex; ++i) {
				colorInt[i] = Float.floatToRawIntBits(color[i]);
			}
			this.colors.clear();
			this.colors.position(0);
			this.colors.put(colorInt, 0, cIndex);
			this.colors.flip();
			GLES20.glVertexAttribPointer(colorHandler, 4, GLES20.GL_FLOAT,
					false, 4 * 4, colors);
			GLES20.glEnableVertexAttribArray(colorHandler);

			GLES20.glUniform1f(sizeHandler, pointSize);
		}
	}

	public void draw() {
//		GPMatrixState.setShaderProgram(program);
		GPMatrixState.setShader(shader);
		GPMatrixState.setTotalMatrix();
		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, vIndex / 3);
	}

	public void endBatch(boolean refreshAfter) {
		if (refreshAfter) {
			vIndex = 0;
			cIndex = 0;
		}
	}

}
