package com.gust.scene2D;

import android.opengl.GLES20;

import com.gust.common.game_util.GPLogger;
import com.gust.engine.core.GPDebugDraw;
import com.gust.render_engine.base.MatrixHelper;
import com.gust.render_engine.core.Camera;
import com.gust.render_engine.core.GLBlendCommond;
import com.gust.render_engine.core.GPOpenGLStateManager;
import com.gust.scene3D.GPDebugVertex3D;

public class GPRender {
	private Camera camera;
	private GPOpenGLStateManager manager;

	public GPRender() {
		GLES20.glClearColor(0, 0, 0, 1);
		manager = GPOpenGLStateManager.getInstance();
		manager.setClearColor(0, 0, 0);
	}
	
	public void setClearColor(float R, float G, float B){
		manager.setClearColor(R, G, B);
	}

	// public void setShader(GPShader shader) {
	// curShader = shader;
	// curShader.bind();
	// cameraPositionHandler = GLES20.glGetUniformLocation(
	// curShader.getProgram(), GPShaderManager.VIEW_MATRIX_NAME);
	// totalMatrixHandler = GLES20.glGetUniformLocation(
	// curShader.getProgram(), GPShaderManager.MVP_MATRIX_NAME);
	// projViewMatrixHandler = GLES20.glGetUniformLocation(
	// curShader.getProgram(), GPShaderManager.PROJECTION_VIEW_MATRIX_NAME);
	// modelMatrixHandler = GLES20.glGetUniformLocation(
	// curShader.getProgram(), GPShaderManager.MODEL_MATRIX_NAME);
	// }

	public void bindCamera(Camera camera) {
		this.camera = camera;
	}

	public void clearScreen() {
		if (camera == null) {
			GPLogger.log("GPRender", "camera hasn't been set", GPLogger.Warning);
		}
		manager.clearScreen();
		manager.enableTexture2D(true);
		manager.enableBlend(true);
	}

	public void setupCamera() {
		camera.setup();
	}

	public void enableBlend(int sfactor, int dfactor) {
		manager.enableBlend(true);
		manager.pushCommond(new GLBlendCommond(sfactor, dfactor));
	}

	public void disableBlend() {
		manager.enableBlend(false);
	}

//	public void setBlendFunc(int sfactor, int dfactor) {
//		manager.useBlendFunc(sfactor, dfactor);
//	}

	public void useDefaultBlendFunc() {
		manager.pushCommond(new GLBlendCommond(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA));
//		manager.useBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void setDepthBufferReadOnly() {
		GLES20.glDepthMask(false);
	}

	public void setDepthBufferReadWrite() {
		GLES20.glDepthMask(true);
	}

	public void drawVisibleSet(GPNode2D node) {
		// curShader.bind();
		// setProjViewMatrix();
		// setViewMatrix();
		// GPMatrix4 matrix = node.getWorldTransform();
		// GPShaderManager.getInstance().setModelMatrix(matrix);
		MatrixHelper.getInstance().setTotalMatrix(
				camera.getProjectionAndViewMatrix());
		node.draw();
	}

	public void drawDebug(GPDebugDraw debugs) {
		// GPShaderManager.getInstance().useShader(debugs.getShader());
		if (debugs.debugDraw == null) {
			return;
		}
		int len = debugs.debugDraw.size();
		MatrixHelper.getInstance().setTotalMatrix(
				camera.getProjectionAndViewMatrix());
		for (int i = 0; i < len; ++i) {
			GPDebugVertex3D debug = debugs.debugDraw.get(i);
			debug.bind();
			debug.draw(GLES20.GL_LINE_LOOP, 0, debug.getNum());
		}
		debugs.clear();
	}
	// public void setViewMatrix() {
	// // cameraPositionHandler =
	// // GLES20.glGetUniformLocation(curShader.getProgram(), "cameraMatrix");
	// GLES20.glUniformMatrix4fv(cameraPositionHandler, 1, false, camera
	// .getViewMatrix().getData(), 0);
	// }
	//
	// /**
	// * 将总矩阵传给着色器
	// */
	// public void setTotalMatrix() {
	// // totalMatrixHandler =
	// // GLES20.glGetUniformLocation(curShader.getProgram(), "uMVPMatrix");
	// GLES20.glUniformMatrix4fv(totalMatrixHandler, 1, false, camera
	// .getProjectionAndViewMatrix().getData(), 0);
	// }
	//
	// /**
	// * 设置将模型矩阵传给着色器
	// */
	// public void setModelMatrix(GPMatrix4 modelMat) {
	// // modelMatrixHandler =
	// // GLES20.glGetUniformLocation(curShader.getProgram(), "modelMatrix");
	// GLES20.glUniformMatrix4fv(modelMatrixHandler, 1, false,
	// modelMat.getData(), 0);
	// }
	//
	// /**
	// * 设置投影矩阵与相机变换矩阵的积并传给着色器
	// */
	// public void setProjViewMatrix() {
	// // projViewMatrixHandler =
	// // GLES20.glGetUniformLocation(curShader.getProgram(),
	// // "projViewMatrix");
	// GLES20.glUniformMatrix4fv(projViewMatrixHandler, 1, false, camera
	// .getProjectionAndViewMatrix().getData(), 0);
	// }
}
