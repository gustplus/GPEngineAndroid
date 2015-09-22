package com.gust.render_engine.environment;

import com.gust.common.math.GPVector3f;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.GPTexture_20;
import com.gust.render_engine.core.GPVertexBuffer3;
import com.gust.scene3D.GPVertexBuffer3D;

import android.opengl.GLES20;
/**
 * 天空盒，用于模拟天空
 * @author gustplus
 *
 */
public class GPSkyBox {
	private float width;
	private GPVertexBuffer3D vertices;
	private GPVector3f position;
	private GPTexture_20 skyMap[];
	private float[][] verticesBuffer;
	short[] indices;
	
	float[] texCoords;
	
	public GPSkyBox(GPShader shader, float width){
		this.width = width;
		position = new GPVector3f(0, 0, 0);
		vertices = new GPVertexBuffer3D(shader);
		skyMap = new GPTexture_20[5];
	}
	
	public GPVector3f getPosition(){
		return position;
	}
	
	/**
	 * 
	 * @param fiveFileNames 分别是上，左，右，前，后的纹理文件名；
	 * @param mipmap
	 */
	public void loadSky(String [] fiveFileNames,boolean mipmap){
		for(int i = 0;i<5;i++){
			skyMap[i] = new GPTexture_20(fiveFileNames[i], mipmap);
		}
		verticesBuffer = new float[][]{
				{
					width/2, width/2,-width/2,
					-width/2, width/2,-width/2,
					-width/2, width/2 ,width/2,
					width/2, width/2, width/2,	
				},{
					-width/2,-width/2,-width/2,
					-width/2,-width/2, width/2,
					-width/2, width/2, width/2,
					-width/2, width/2,-width/2,	
				},{
					width/2,-width/2, width/2,
					width/2,-width/2,-width/2,
					width/2, width/2,-width/2,
					width/2, width/2, width/2,	
				},{
					-width/2,-width/2,width/2,
					width/2,-width/2,width/2,
					width/2, width/2,width/2,
					-width/2, width/2,width/2,
				},{
					width/2,-width/2,-width/2,
					-width/2,-width/2,-width/2,
					-width/2, width/2,-width/2,
					width/2, width/2,-width/2}
				};
		
		indices = new short[]{0,1,2,2,3,0};
		
		texCoords = new float[]{0,1, 1,1, 1,0, 0,0};
		
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
	}
	
	public void drawSky(){
		this.vertices.setIndices(indices, 0, indices.length);
//		this.vertices.setTexCoords(texCoords, 0, texCoords.length);
		for(int i = 0;i<5;i++){
			GLES20.glEnable(GLES20.GL_TEXTURE_2D);
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			skyMap[i].bind();

			this.vertices.setVertices(verticesBuffer[i], 0, 12);
			vertices.bind();
			this.vertices.draw(GLES20.GL_TRIANGLES, 0, 6);
		}
	}
	
}

