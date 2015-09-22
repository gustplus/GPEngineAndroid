package com.gust.render_engine.core;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.gust.common.game_util.GPLogger;
import com.gust.engine.core.GPSource;
import com.gust.engine.core.GPSourceManager;
import com.gust.render_engine.base.GPGLGame;
import com.gust.system_implement.GPFileManager;

public class GPTexture2D extends GPSource{
	private int textureId;
	public int width;
	public int height;
	private boolean mipmap;

	/**
	 * @param fileName纹理图像文件名
	 * @param game
	 * @param mipmap是否自动生成mipmap
	 */
	public GPTexture2D(String fileName, InputStream input, boolean mipmap)
	{
		super(fileName);
		this.mipmap = mipmap;
		load(input);
	}

	public GPTexture2D(String texName, Bitmap bitmap, boolean mipmap)
	{
		super(texName);
		load(bitmap);
	}
	
	public GPTexture2D(int sourceId, GPGLGame glgame){
		super("sourceId:" + sourceId);
		InputStream input = glgame.getResources().openRawResource(sourceId);
		load(input);
	}
	
	public boolean isMipmap(){
		return mipmap;
	}
	
	void setTextureID(int textureID){
		this.textureId = textureID;
	}

	private void load(Bitmap bitmap){
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
	
	private void load(InputStream input)
	{
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
	public void setFilter(float minFilter, float magFilter)
	{

		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, magFilter);
	}

	public void reload()
	{
		InputStream input = null;
		try {
			input = GPFileManager.getinstance().readAsset(GPSourceManager.getInstance().getSourcePath() + name);
			GPLogger.log("TextureManager", "reload " + name);
			load(input);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (input != null) {
				try {
					input.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		bind();
		setFilter(GLES20.GL_NEAREST, GLES20.GL_LINEAR);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}
	
	public void reload(Bitmap bitmap){
		load(bitmap);
		bind();
		setFilter(GLES20.GL_NEAREST, GLES20.GL_LINEAR);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}

	/**
	 * 绑定现有贴图作为有效贴图
	 */
	public void bind()
	{
		super.bind();
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
	}

	/**
	 * 删除贴图
	 */
	public void dispose()
	{
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		int[] textureIds = { textureId };
		GLES20.glDeleteTextures(1, textureIds, 0);
	}

}
