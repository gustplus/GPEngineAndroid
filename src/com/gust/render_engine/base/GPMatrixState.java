package com.gust.render_engine.base;


import com.gust.engine.core.GPShader;

import android.opengl.GLES20;
import android.opengl.Matrix;
/**
 * 管理矩阵变换
 * @author gustplus
 *
 */
public class GPMatrixState {
	private static float[] totalMatrix = new float[16];
	private static float[] modelMatrix = new float[16];
	private static float[] cameraMatrix = new float[16];
	private static float[] projestionMatrix = new float[16];
	private static float[][] matrixStack = new float[16][16];
	private static int program = 0;
	private static int top = -1;
	
	private static int totalMatrixHandler;
	private static int modelMatrixHandler;
	private static int projViewMatrixHandler;
	private static int cameraPositionHandler;

	public static int PROJESTION_MODE = 0;
	public static int MODEL_MODE = 1;
	public static int VIEW_MODE = 2;
	
	/**
	 * 设置渲染器
	 * @param aProgramNum渲染器编号
	 */
//	public static void setShaderProgram(int aProgram){
//		program = aProgram;
//	}
	
	public static void setShader(GPShader shader){
		program = shader.getProgram();
	}
	
	/**
	 * 设置正投影矩阵的相关参数
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @param zNear
	 * @param zFar
	 */
	public static void orthof(float left, float right, float bottom, float top, float zNear, float zFar){
		Matrix.orthoM(projestionMatrix, 0, left, right, bottom, top, zNear, zFar);
	}
	
	/**
	 * 设置透投影矩阵的参数
	 * @param fieldOfView视角（0~180）
	 * @param aspectRatio长宽比
	 * @param near近平面（>0）
	 * @param far远平面(>near)
	 */
	public static void Perspective(float fieldOfView, float aspectRatio, float near,
			float far)
	{
		float top = (float) (near * Math.tan(Math.toRadians(fieldOfView/2)));
		float bottom = -top;
		float right = top * aspectRatio;
		float left = -right;
//		float right = (float) (near * Math.tan(Math.toRadians(fieldOfView/2)));
//		float top = right / aspectRatio;
//		float bottom = -top;
//		float left = -right;
		Matrix.frustumM(projestionMatrix, 0, left, right, bottom, top, near,
				far);
	}
	
	/**
	* 设置透视投影矩阵的参数
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @param near
	 * @param far
	 */
	public static void Frustum(float left, float right, float bottom, float top, float near, float far){
		Matrix.frustumM(projestionMatrix, 0, left, right, bottom, top, near, far);
	}

	/**
	 * 将相关矩阵置为单位矩阵
	 * @param Mode矩阵类型
	 */
	public static void loadIdentity(int Mode)
	{
		if (Mode == PROJESTION_MODE)
			Matrix.setIdentityM(projestionMatrix, 0);
		if (Mode == VIEW_MODE)
			Matrix.setIdentityM(cameraMatrix, 0);
		if(Mode == MODEL_MODE)
			Matrix.setIdentityM(modelMatrix, 0);
	}
	
	/**
	 * 移动相机
	 * @param fromX观察者的X坐标
	 * @param fromY观察者的Y坐标
	 * @param fromZ观察者的Z坐标
	 * @param toX观察点的X坐标
	 * @param toY观察点的Y坐标
	 * @param toZ观察点的Z坐标
	 * @param upX头顶方向
	 * @param upY头顶方向
	 * @param upZ头顶方向
	 */
	public static void lookatf( float fromX, float fromY, float fromZ, float toX, float toY, float toZ, float upX, float upY, float upZ){
		Matrix.setLookAtM(cameraMatrix, 0, fromX, fromY, fromZ, toX, toY, toZ, upX, upY, upZ);
	}
	
	
	public static void cRotatef(float angle, float x, float y, float z)
	{
		Matrix.rotateM(cameraMatrix, 0, angle, x, y, z);
	}

	public static void cTranslatef(float x, float y, float z)
	{
		Matrix.translateM(cameraMatrix, 0, x, y, z);
	}
	
	public static void rotatef(float angle, float x, float y, float z)
	{
		Matrix.rotateM(modelMatrix, 0, angle, x, y, z);
	}

	public static void translatef(float x, float y, float z)
	{
		Matrix.translateM(modelMatrix, 0, x, y, z);
	}

	public static void scalef(float x, float y, float z)
	{
		Matrix.scaleM(modelMatrix, 0, x, y, z);
	}

	public static void pushMatrix()
	{
		if (top < 15)
			matrixStack[++top] = modelMatrix.clone();
	}

	public static void popMatrix()
	{
		if (top > -1)
			modelMatrix = matrixStack[top--].clone();
	}
	
	public static float[] getTotalMatrix(){
		Matrix.multiplyMM(totalMatrix, 0, cameraMatrix, 0, modelMatrix, 0);
		Matrix.multiplyMM(totalMatrix, 0, projestionMatrix, 0, totalMatrix, 0);
		return totalMatrix;
	}
	
	public static float[] getProjViewMatrix(){
		float[] projViewMatrix = new float[16];
		Matrix.multiplyMM(projViewMatrix, 0, projestionMatrix, 0, cameraMatrix, 0);
		return projViewMatrix;
	}
	
	public static float[] getCameraMatrix(){
		return cameraMatrix;
	}
	
	/**
	 * 设置相机变换矩阵并传给着色器
	 */
	public static void setCameraMatrix(){
		cameraPositionHandler = GLES20.glGetUniformLocation(program, "cameraMatrix");
		GLES20.glUniformMatrix4fv(cameraPositionHandler, 1, false, getCameraMatrix(), 0);
	}
	
	/**
	 * 将总矩阵传给着色器
	 */
	public static void setTotalMatrix(){
		totalMatrixHandler = GLES20.glGetUniformLocation(program, "uMVPMatrix");
		GLES20.glUniformMatrix4fv(totalMatrixHandler, 1, false, getTotalMatrix(), 0);
	}
	
	/**
	 * 设置将模型矩阵传给着色器
	 */
	public static void setModelMatrix(){
		modelMatrixHandler = GLES20.glGetUniformLocation(program, "modelMatrix");
		GLES20.glUniformMatrix4fv(modelMatrixHandler, 1, false, modelMatrix, 0);
	}
	
	/**
	 * 设置投影矩阵与相机变换矩阵的积并传给着色器
	 */
	public static void setProjViewMatrix(){
		projViewMatrixHandler = GLES20.glGetUniformLocation(program, "projViewMatrix");
		GLES20.glUniformMatrix4fv(projViewMatrixHandler, 1, false, getProjViewMatrix(), 0);
	}
}
