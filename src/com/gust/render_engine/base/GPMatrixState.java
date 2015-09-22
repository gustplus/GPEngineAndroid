package com.gust.render_engine.base;


import com.gust.engine.core.GPShader;

import android.opengl.GLES20;
import android.opengl.Matrix;
/**
 * �������任
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
	 * ������Ⱦ��
	 * @param aProgramNum��Ⱦ�����
	 */
//	public static void setShaderProgram(int aProgram){
//		program = aProgram;
//	}
	
	public static void setShader(GPShader shader){
		program = shader.getProgram();
	}
	
	/**
	 * ������ͶӰ�������ز���
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
	 * ����͸ͶӰ����Ĳ���
	 * @param fieldOfView�ӽǣ�0~180��
	 * @param aspectRatio�����
	 * @param near��ƽ�棨>0��
	 * @param farԶƽ��(>near)
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
	* ����͸��ͶӰ����Ĳ���
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
	 * ����ؾ�����Ϊ��λ����
	 * @param Mode��������
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
	 * �ƶ����
	 * @param fromX�۲��ߵ�X����
	 * @param fromY�۲��ߵ�Y����
	 * @param fromZ�۲��ߵ�Z����
	 * @param toX�۲���X����
	 * @param toY�۲���Y����
	 * @param toZ�۲���Z����
	 * @param upXͷ������
	 * @param upYͷ������
	 * @param upZͷ������
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
	 * ��������任���󲢴�����ɫ��
	 */
	public static void setCameraMatrix(){
		cameraPositionHandler = GLES20.glGetUniformLocation(program, "cameraMatrix");
		GLES20.glUniformMatrix4fv(cameraPositionHandler, 1, false, getCameraMatrix(), 0);
	}
	
	/**
	 * ���ܾ��󴫸���ɫ��
	 */
	public static void setTotalMatrix(){
		totalMatrixHandler = GLES20.glGetUniformLocation(program, "uMVPMatrix");
		GLES20.glUniformMatrix4fv(totalMatrixHandler, 1, false, getTotalMatrix(), 0);
	}
	
	/**
	 * ���ý�ģ�;��󴫸���ɫ��
	 */
	public static void setModelMatrix(){
		modelMatrixHandler = GLES20.glGetUniformLocation(program, "modelMatrix");
		GLES20.glUniformMatrix4fv(modelMatrixHandler, 1, false, modelMatrix, 0);
	}
	
	/**
	 * ����ͶӰ����������任����Ļ���������ɫ��
	 */
	public static void setProjViewMatrix(){
		projViewMatrixHandler = GLES20.glGetUniformLocation(program, "projViewMatrix");
		GLES20.glUniformMatrix4fv(projViewMatrixHandler, 1, false, getProjViewMatrix(), 0);
	}
}
