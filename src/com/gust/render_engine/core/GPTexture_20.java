package com.gust.render_engine.core;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.gust.engine.core.GPSource;
import com.gust.render_engine.base.GPGLGame;
import com.gust.system_implement.GPFileManager;

public class GPTexture_20 extends GPSource {
	private int textureId;
	public int width;
	public int height;
	private boolean mipmap;
	GPFileManager fileIO;

	private static int texId;

	/**
	 * @param fileName纹理图像文件名
	 * @param game
	 * @param mipmap是否自动生成mipmap
	 */
	public GPTexture_20(String fileName, boolean mipmap) {
		super(fileName);
		fileIO = GPFileManager.getinstance();
		InputStream input = null;
		try {
			input = fileIO.readAsset(fileName);
			load(input, mipmap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param bitmap纹理位图
	 * @param game
	 * @param mipmap是否自动生成mipmap
	 */
	public GPTexture_20(Bitmap bitmap, boolean mipmap) {
		super("null");
		load(bitmap, mipmap);
	}

	public GPTexture_20(int sourceId, GPGLGame glgame, boolean mipmap) {
		super("sourceId:" + sourceId);
		InputStream input = glgame.getResources().openRawResource(sourceId);
		load(input, mipmap);
	}

	/**
	 * 
	 * @param in
	 *            纹理贴图的输入流
	 * @param game
	 * @param mipmap是否自动生成mipmap
	 */
	public GPTexture_20(InputStream input, boolean mipmap) {
		super("stream");
		load(input, mipmap);
	}

	private void load(Bitmap bitmap, boolean mipmap) {
		int[] textureIds = new int[1];
		GLES20.glGenTextures(1, textureIds, 0);
		textureId = textureIds[0];
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		if (mipmap) {
			GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
			setFilter(GLES20.GL_LINEAR_MIPMAP_NEAREST,
					GLES20.GL_LINEAR_MIPMAP_LINEAR);
		} else
			setFilter(GLES20.GL_NEAREST, GLES20.GL_LINEAR);
		bitmap.recycle();
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}

	private void load(InputStream input, boolean mipmap) {
		int[] textureIds = new int[1];
		GLES20.glGenTextures(1, textureIds, 0);
		textureId = textureIds[0];
		Bitmap bitmap = BitmapFactory.decodeStream(input);
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		if (mipmap) {
			GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
			setFilter(GLES20.GL_LINEAR_MIPMAP_NEAREST,
					GLES20.GL_LINEAR_MIPMAP_LINEAR);
		} else
			setFilter(GLES20.GL_NEAREST, GLES20.GL_LINEAR);
		bitmap.recycle();
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}

	/**
	 * 设置适配器
	 * 
	 * @param minFilter当纹理大小大于图元大小时使用的适配器
	 * @param magFilter当纹理大小小于图元大小时使用的适配器
	 */
	public void setFilter(float minFilter, float magFilter) {

		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, magFilter);
	}

	public void reload() {
		InputStream input = null;
		try {
			input = fileIO.readAsset(name);
			load(input, mipmap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		bind();
		setFilter(GLES20.GL_NEAREST, GLES20.GL_LINEAR);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}

	public void reload(Bitmap bitmap) {
		load(bitmap, mipmap);
		bind();
		setFilter(GLES20.GL_NEAREST, GLES20.GL_LINEAR);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}

	/**
	 * 绑定现有贴图作为有效贴图
	 */
	public void bind() {
//		if (texId != textureId) {
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
			texId = textureId;
//		}
	}

	/**
	 * 删除贴图
	 */
	public void dispose() {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		int[] textureIds = { textureId };
		GLES20.glDeleteTextures(1, textureIds, 0);
	}

}
