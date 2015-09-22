package com.gust.engine.core;

import com.gust.common.math.GPMatrix4;

import android.opengl.GLES20;

public class GPShader extends GPSource{
	private int totalMatrixHandler;
	private int modelMatrixHandler;
	private int projViewMatrixHandler;
	private int cameraPositionHandler;

	private int program;

	public int getProgram() {
		return program;
	}
	
	public void setProgram(int program) {
		this.program = program;
		totalMatrixHandler = -1;
		modelMatrixHandler = -1;
		projViewMatrixHandler = -1;
		cameraPositionHandler = -1;
	}

	public GPShader(int program, String name) {
		super(name);
		this.program = program;
		totalMatrixHandler = -1;
		modelMatrixHandler = -1;
		projViewMatrixHandler = -1;
		cameraPositionHandler = -1;
	}

	public void bind() {
		super.bind();
		GPShaderManager.getInstance().useShader(this);
	}
	
	public void setCameraMatrix(GPMatrix4 matrix) {
		if (cameraPositionHandler == -1)
		cameraPositionHandler = GLES20.glGetUniformLocation(program,
				GPShaderManager.CAMERA_MATRIX_NAME);
		GLES20.glUniformMatrix4fv(cameraPositionHandler, 1, false,
				matrix.getData(), 0);
	}

	/**
	 * 将总矩阵传给着色器
	 */
	public void setTotalMatrix(GPMatrix4 matrix) {
		if (totalMatrixHandler == -1)
			totalMatrixHandler = GLES20.glGetUniformLocation(program,
					GPShaderManager.MVP_MATRIX_NAME);
		GLES20.glUniformMatrix4fv(totalMatrixHandler, 1, false,
				matrix.getData(), 0);
	}

	/**
	 * 设置将模型矩阵传给着色器
	 */
	public void setModelMatrix(GPMatrix4 matrix) {
		if (modelMatrixHandler == -1)
			modelMatrixHandler = GLES20.glGetUniformLocation(program,
					GPShaderManager.MODEL_MATRIX_NAME);
		GLES20.glUniformMatrix4fv(modelMatrixHandler, 1, false,
				matrix.getData(), 0);
	}

	/**
	 * 设置投影矩阵与相机变换矩阵的积并传给着色器
	 */
	public void setProjViewMatrix(GPMatrix4 matrix) {
		if (projViewMatrixHandler == -1)
			projViewMatrixHandler = GLES20.glGetUniformLocation(program,
					GPShaderManager.PROJECTION_VIEW_MATRIX_NAME);
		GLES20.glUniformMatrix4fv(projViewMatrixHandler, 1, false,
				matrix.getData(), 0);
	}

	public int getUniformLocation(String name) {
		return GLES20.glGetUniformLocation(program, name);
	}

	public int getAttribLocation(String name) {
		return GLES20.glGetAttribLocation(program, name);
	}

	@Override
	public void dispose() {
		GLES20.glDeleteProgram(program);
	}
	
	public void reload(){
		
	}
}
