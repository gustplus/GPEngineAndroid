package com.gust.render_engine.environment;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class GPEnvironmentTexure {
	private int texId;
	private Context context;
	
	public GPEnvironmentTexure(Context context){
		this.context = context;
	}
	
	public void loadEnv(String [] sixFileNames){
		int[] texIds = new int[1];
		GLES20.glGenTextures(1, texIds, 0);
		texId = texIds[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, texId);
		InputStream in = null;
		Bitmap skyMap = null;
		for(int faces = 0;faces<6;faces++){
			try {
				in = context.getAssets().open(sixFileNames[faces]);
				skyMap = BitmapFactory.decodeStream(in);
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				try {
					in.close();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X+faces, 0, skyMap, 0);
			skyMap.recycle();
		}
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, 0);
	}
	
	public void loadEnv(Bitmap[] sixBitmaps){
		int[] texIds = new int[1];
		GLES20.glGenTextures(1, texIds, 0);
		texId = texIds[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, texId);
		Bitmap skyMap = null;
		for(int faces = 0;faces<6;faces++){
				skyMap = sixBitmaps[faces];
			GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X+faces, 0, skyMap, 0);
			skyMap.recycle();
		}
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, 0);
	}
	
	public void loadEnv(int[] sixFileIds){
		int[] texIds = new int[1];
		GLES20.glGenTextures(1, texIds, 0);
		texId = texIds[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, texId);
		InputStream in = null;
		Bitmap skyMap = null;
		for(int faces = 0;faces<6;faces++){
			try {
				in = context.getResources().openRawResource(sixFileIds[faces]);
				skyMap = BitmapFactory.decodeStream(in);
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				try {
					in.close();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X+faces, 0, skyMap, 0);
			skyMap.recycle();
		}
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, 0);
	}
	
	public void bind(){
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, texId);
	}
}
