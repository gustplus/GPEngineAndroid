package com.gust.render_engine.core;

import com.gust.common.game_util.GPLogger;
import com.gust.common.game_util.GPPool;
import com.gust.common.game_util.GPStack;
import com.gust.physics2D.Bound_Rectangle;
import com.gust.render_engine.base.GPRuntimeVaribles;

import android.opengl.GLES20;

public class GPOpenGLStateManager {
	private int lineWidth;
	private int blendSrcFactor;
	private int blendDstFactor;

	private boolean useBlend;

	private float[] color;

	private boolean useDepthBuffer;
	private boolean useStencilBuffer;

	private boolean useScissor;

	private static GPOpenGLStateManager stateManager;

	private boolean depthReadOnly;

	private Bound_Rectangle scissorRect;
	
	private boolean useTexture2D;
	private boolean useTextureCube;

	private GPStack<GLCommond> commondsStack;

	public static GPOpenGLStateManager getInstance() {
		if (stateManager == null) {
			stateManager = new GPOpenGLStateManager();
		}
		return stateManager;
	}

	private GPOpenGLStateManager() {
		int[] info = new int[1];
		GLES20.glGetIntegerv(GLES20.GL_LINE_WIDTH, info, 0);
		lineWidth = info[0];

		useScissor = false;

		color = new float[3];

		useBlend = false;
		blendSrcFactor = 0;
		blendDstFactor = 0;

		depthReadOnly = false;
		useScissor = false;
		scissorRect = new Bound_Rectangle(0, 0, (int) GPRuntimeVaribles.SCREENWIDTH, (int) GPRuntimeVaribles.SCREENHEIGHT);
	}

	public void pushCommond(GLCommond commond) {
		if (commondsStack == null) {
			commondsStack = new GPStack<GLCommond>(16);
		}
		commondsStack.push(commond);
		commond.excute();
	}

	public void popCommond() {
		GLCommond commond = commondsStack.pop();
		if (commond != null) {
			commond.revoke();
		}
		
		commond = commondsStack.getTop();
		if (commond != null) {
			commond.excute();
		}
	}

	public void enableBlend(boolean useBlend) {
		this.useBlend = useBlend;
		if (useBlend) {
			GLES20.glEnable(GLES20.GL_BLEND);
		} else {
			GLES20.glDisable(GLES20.GL_BLEND);
		}
	}

	public void useBlendFunc(int sFactor, int dFactor) {
		if (blendDstFactor == dFactor && blendSrcFactor == sFactor) {
			return;
		}
		blendSrcFactor = sFactor;
		blendDstFactor = dFactor;
		GLES20.glBlendFunc(sFactor, dFactor);
	}

	public void setLineWidth(int width) {
		if (width == lineWidth) {
			return;
		}
		lineWidth = width;
		GLES20.glLineWidth(lineWidth);
	}

	public void enableDepthBuffer(boolean enable) {
		if (enable == useDepthBuffer) {
			return;
		}
		useDepthBuffer = enable;
		if (enable) {
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		} else {
			GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		}
	}

	public void enableStencilBuffer(boolean enable) {
		if (enable == useStencilBuffer) {
			return;
		}
		useDepthBuffer = enable;
		if (enable) {
			GLES20.glEnable(GLES20.GL_STENCIL_TEST);
		} else {
			GLES20.glDisable(GLES20.GL_STENCIL_TEST);
		}
	}

	public void setClearColor(float r, float g, float b) {
		if (color[0] == r && color[1] == g && color[2] == b) {
			return;
		}
		color[0] = r;
		color[1] = g;
		color[2] = b;
		GLES20.glClearColor(r, g, b, 0);
	}

	public void clearScreen() {
		int mask = GLES20.GL_COLOR_BUFFER_BIT;
		if (useDepthBuffer) {
			mask |= GLES20.GL_DEPTH_BUFFER_BIT;
		}
		if (useStencilBuffer) {
			mask |= GLES20.GL_STENCIL_BUFFER_BIT;
		}
		GLES20.glClear(mask);
	}

	public void enableScissor(boolean enable) {
		if (enable == useScissor) {
			return;
		}
		if (enable) {
			GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
			useScissor = true;
		} else {
			GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
			
			scissorRect.lowerLeft.x = 0;
			scissorRect.lowerLeft.y = 0;
			scissorRect.width = (int) GPRuntimeVaribles.SCREENWIDTH;
			scissorRect.height = (int) GPRuntimeVaribles.SCREENHEIGHT;
			GLES20.glScissor((int)scissorRect.lowerLeft.x, (int)scissorRect.lowerLeft.y, (int)scissorRect.width, (int)scissorRect.height);
			useScissor = false;
		}
	}

	public void scissor(Bound_Rectangle rect) {
		// if (!useScissor) {
		// return;
		// }

		this.scissorRect = rect.clone();

		GLES20.glScissor((int)scissorRect.lowerLeft.x, (int)scissorRect.lowerLeft.y, (int)scissorRect.width, (int)scissorRect.height);
	}
	
	public Bound_Rectangle getScissorRect(){
		return scissorRect.clone();
	}

	public void enableTexture2D(boolean enable) {
		if (useTexture2D == enable) {
			return;
		}
		useTexture2D = enable;
		if (enable) {
			GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		} else {
			GLES20.glDisable(GLES20.GL_TEXTURE_2D);
		}
	}

	public void enableTextureCube(boolean enable) {
		if (useTextureCube == enable) {
			return;
		}
		useTextureCube = enable;
		if (enable) {
			GLES20.glEnable(GLES20.GL_TEXTURE_CUBE_MAP);
		} else {
			GLES20.glDisable(GLES20.GL_TEXTURE_CUBE_MAP);
		}
	}

	public void setDepthBufferReadOnly() {
		if (depthReadOnly) {
			return;
		}
		depthReadOnly = true;
		GLES20.glDepthMask(false);
	}

	public void setDepthBufferReadWrite() {
		if (!depthReadOnly) {
			return;
		}
		depthReadOnly = false;
		GLES20.glDepthMask(true);
	}

	public void resume() {
		GLES20.glClearColor(color[0], color[1], color[2], 1);
		if (useBlend) {
			GLES20.glEnable(GLES20.GL_BLEND);
			GLES20.glBlendFunc(blendSrcFactor, blendDstFactor);
		}
		if (useDepthBuffer) {
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		}
		if (useStencilBuffer) {
			GLES20.glEnable(GLES20.GL_STENCIL_TEST);
		}
		if (depthReadOnly) {
			GLES20.glDepthMask(false);
		}
		GLES20.glLineWidth(lineWidth);
		if (useScissor) {
			GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
			GLES20.glScissor((int)scissorRect.lowerLeft.x, (int)scissorRect.lowerLeft.y, (int)scissorRect.width, (int)scissorRect.height);
		}

		if (useTexture2D) {
			GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		}

		if (useTextureCube) {
			GLES20.glEnable(GLES20.GL_TEXTURE_CUBE_MAP);
		}
	}

	public void shutdown() {
		stateManager = null;
	}
}
