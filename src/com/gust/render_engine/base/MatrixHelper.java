package com.gust.render_engine.base;

import com.gust.common.math.GPMatrix4;
import com.gust.engine.core.GPShader;

public class MatrixHelper {
	private GPMatrix4 totalMatrix;
	private GPMatrix4 viewMatrix;
	private GPMatrix4 modelMatrix;
	private GPMatrix4 projViewMatrix;
	private GPMatrix4 cameraPosition;
	
	private GPMatrix4 storedMatrix;
	
	public void storeMatrix(GPMatrix4 matrix){
		storedMatrix = matrix;
	}
	
	public GPMatrix4 loadMatrix(){
		return storedMatrix;
	}

	public GPMatrix4 getTotalMatrix() {
		return totalMatrix;
	}

	public GPMatrix4 getViewMatrix() {
		return viewMatrix;
	}

	public GPMatrix4 getModelMatrix() {
		return modelMatrix;
	}

	public GPMatrix4 getProjViewMatrix() {
		return projViewMatrix;
	}

	private static MatrixHelper matrixHelper;

	public static MatrixHelper getInstance() {
		if (matrixHelper == null) {
			matrixHelper = new MatrixHelper();
		}
		return matrixHelper;
	}
	
	public void setViewMatrix(GPMatrix4 matrix) {
		viewMatrix = matrix;
	}

	/**
	 * 将总矩阵传给着色器
	 */
	public void setTotalMatrix(GPMatrix4 matrix) {
		totalMatrix = matrix;
	}

	/**
	 * 设置将模型矩阵传给着色器
	 */
	public void setModelMatrix(GPMatrix4 matrix) {
		modelMatrix = matrix;
	}

	/**
	 * 设置投影矩阵与相机变换矩阵的积并传给着色器
	 */
	public void setProjViewMatrix(GPMatrix4 matrix) {
		projViewMatrix = matrix;
	}
	

	public void postViewMatrix(GPShader shader) {
		if(viewMatrix == null){
			return;
		}
		shader.setCameraMatrix(viewMatrix);
	}

	/**
	 * 将总矩阵传给着色器
	 */
	public void postTotalMatrix(GPShader shader) {
		if(totalMatrix == null){
			return;
		}
		shader.setTotalMatrix(totalMatrix);
	}

	/**
	 * 设置将模型矩阵传给着色器
	 */
	public void postModelMatrix(GPShader shader) {
		if(modelMatrix == null){
			return;
		}
		shader.setModelMatrix(modelMatrix);
	}

	/**
	 * 设置投影矩阵与相机变换矩阵的积并传给着色器
	 */
	public void postProjViewMatrix(GPShader shader) {
		if(projViewMatrix == null){
			return;
		}
		shader.setProjViewMatrix(projViewMatrix);
	}
}
