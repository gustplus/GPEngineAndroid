package com.gust.scene3D;

import android.opengl.GLES20;

import com.gust.common.game_util.GPLogger;
import com.gust.common.math.GPMatrix4;
import com.gust.engine.core.GPDebugDraw;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.Camera;

public class GPRender3D {
	private GPShader curShader;
	private Camera camera;

	private int totalMatrixHandler;
	private int modelMatrixHandler;
	private int projViewMatrixHandler;
	private int cameraPositionHandler;

	public GPRender3D() {
		GLES20.glClearColor(0, 0, 0, 1);
	}

	public void setShader(GPShader shader) {
		curShader = shader;
		curShader.bind();
		cameraPositionHandler = GLES20.glGetUniformLocation(
				curShader.getProgram(), "cameraMatrix");
		totalMatrixHandler = GLES20.glGetUniformLocation(
				curShader.getProgram(), "uMVPMatrix");
		projViewMatrixHandler = GLES20.glGetUniformLocation(
				curShader.getProgram(), "projViewMatrix");
		modelMatrixHandler = GLES20.glGetUniformLocation(
				curShader.getProgram(), "modelMatrix");
	}

	public void bindCamera(Camera camera) {
		this.camera = camera;
	}

	public void setClearColor(float r, float g, float b, float a) {
		GLES20.glClearColor(r, g, b, a);
	}

	public void clearScreen() {
		if (camera == null) {
			GPLogger.log("GPRender", "camera hasn't been set", GPLogger.Warning);
		}
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BITS);
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		EnableBlend();
	}

	public void setupCamera() {
		camera.setup();
	}

	public void EnableBlend() {
		GLES20.glEnable(GLES20.GL_BLEND);
	}

	public void DisableBlend() {
		GLES20.glDisable(GLES20.GL_BLEND);
	}

	public void setBlendFunc(int dfactor, int sfactor) {
		GLES20.glBlendFunc(sfactor, dfactor);
	}

	public void useDefaultBlendFunc() {
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void setDepthBufferReadOnly() {
		GLES20.glDepthMask(false);
	}

	public void setDepthBufferReadWrite() {
		GLES20.glDepthMask(true);
	}

	public void drawVisibleSet(GPSpatial node) {
		curShader.bind();
		setProjViewMatrix();
		setViewMatrix();
		GPMatrix4 matrix = node.getWorldTransform();
		setModelMatrix(matrix);
		setTotalMatrix(matrix);
		// node.draw();
	}

	public void drawDebug(GPDebugDraw debugs) {
		setShader(debugs.getShader());
		int len = debugs.debugDraw.size();
		for (int i = 0; i < len; ++i) {
			GPDebugVertex3D debug = debugs.debugDraw.get(i);
			setTotalMatrix(debug.getMatrix());
			debug.bind();
			debug.draw(GLES20.GL_LINE_LOOP, 0, debug.getNum());
		}
		debugs.clear();
	}

	public void setViewMatrix() {
		// cameraPositionHandler =
		// GLES20.glGetUniformLocation(curShader.getProgram(), "cameraMatrix");
		GLES20.glUniformMatrix4fv(cameraPositionHandler, 1, false, camera
				.getViewMatrix().getData(), 0);
	}

	/**
	 * 将总矩阵传给着色器
	 */
	public void setTotalMatrix(GPMatrix4 modelMat) {
		// totalMatrixHandler =
		// GLES20.glGetUniformLocation(curShader.getProgram(), "uMVPMatrix");
		if (modelMat == null) {
			GLES20.glUniformMatrix4fv(totalMatrixHandler, 1, false, camera
					.getProjectionAndViewMatrix().getData(), 0);
		} else {
			GLES20.glUniformMatrix4fv(totalMatrixHandler, 1, false, camera
					.getProjectionAndViewMatrix().mul(modelMat).getData(), 0);
		}
	}

	/**
	 * 设置将模型矩阵传给着色器
	 */
	public void setModelMatrix(GPMatrix4 modelMat) {
		// modelMatrixHandler =
		// GLES20.glGetUniformLocation(curShader.getProgram(), "modelMatrix");
		if(modelMat == null){
			return;
		}
		GLES20.glUniformMatrix4fv(modelMatrixHandler, 1, false,
				modelMat.getData(), 0);
	}

	/**
	 * 设置投影矩阵与相机变换矩阵的积并传给着色器
	 */
	public void setProjViewMatrix() {
		// projViewMatrixHandler =
		// GLES20.glGetUniformLocation(curShader.getProgram(),
		// "projViewMatrix");
		GLES20.glUniformMatrix4fv(projViewMatrixHandler, 1, false, camera
				.getProjectionAndViewMatrix().getData(), 0);
	}
}
