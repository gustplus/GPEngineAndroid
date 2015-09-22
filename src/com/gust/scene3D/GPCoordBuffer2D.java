package com.gust.scene3D;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import android.opengl.GLES20;

import com.gust.engine.core.GPShader;
import com.gust.engine.core.GPShaderManager;
import com.gust.render_engine.core.GPTexture2D;

public class GPCoordBuffer2D {
	private IntBuffer texCoords;
	private int texCoorHandler;
	private GPTexture2D[] textures;
	private int[] texturehandlers;
	private GPShader shader;

	private int texCoordsLen;
	
	private int textureOffset;

	public GPCoordBuffer2D(boolean willChange, int numOfTexs, int textureOffset) {
		texCoordsLen = 0;
		this.textureOffset = textureOffset;
		textures = new GPTexture2D[numOfTexs];
		texturehandlers = new int[numOfTexs];
		int len = numOfTexs;
		for (int i = 0; i < len; ++i) {
			texturehandlers[i] = textureOffset;
		}
	}

	public GPShader getShader() {
		return shader;
	}

	public void setShader(GPShader shader) {
		this.shader = shader;
		texCoorHandler = shader.getAttribLocation("aTexCoor");
	}

	public void addTexture(GPTexture2D texture, int index) {
		this.textures[index] = texture;
		this.texturehandlers[index] = shader.getUniformLocation(GPShaderManager.NORMAL_NAME + textureOffset + index);
	}
	
	public void setTextureOffset(int offset){
		int len = texturehandlers.length;
		this.textureOffset = offset;
		for (int i = 0; i < len; ++i) {
			this.texturehandlers[i] = shader.getUniformLocation(GPShaderManager.NORMAL_NAME + textureOffset + i);
		}
	}

	/**
	 * 设置顶点纹理坐标信息
	 * 
	 * @param texCoords纹理坐标数组
	 * @param offset偏移量
	 * @param length使用的长度
	 */
	public void setTexCoords(float[] texCoords, int offset, int length) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(texCoords.length * 4);
		buffer.order(ByteOrder.nativeOrder());
		this.texCoords = buffer.asIntBuffer();
		int[] coords_int = new int[texCoords.length];
		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++) {
			coords_int[j] = Float.floatToRawIntBits(texCoords[i]);
		}
		this.texCoords.clear();
		this.texCoords.position(0);
		this.texCoords.put(coords_int, 0, length);
		this.texCoords.flip();
		this.texCoordsLen = length;
	}

	public float[] getTexCoords() {
		int len = texCoordsLen;
		float[] texCoords = new float[len];
		int[] texCoordsInt = new int[len];
		this.texCoords.position(0);
		this.texCoords.get(texCoordsInt);
		for (int i = 0; i < len; ++i) {
			texCoords[i] = Float.intBitsToFloat(texCoordsInt[i]);
		}
		return texCoords;
	}

	public void bind() {
		shader.bind();
		GLES20.glVertexAttribPointer(texCoorHandler, 2, GLES20.GL_FLOAT, false,
				2 * 4, texCoords);
		GLES20.glEnableVertexAttribArray(texCoorHandler);

		int len = textures.length;
		for (int i = 0; i < len; ++i) {
			if (texturehandlers[i] <= textureOffset) {
				GLES20.glUniform1i(texturehandlers[i], textureOffset + i);
				GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureOffset + i);
				textures[i].bind();
			}
		}
	}
}