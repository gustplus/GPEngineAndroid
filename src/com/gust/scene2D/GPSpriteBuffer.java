package com.gust.scene2D;

import android.opengl.GLES20;
import java.lang.Math;

import com.gust.common.game_util.GPLogger;
import com.gust.common.math.GPConstants;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.GPTexture2D;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.render_engine.core.GPVertexBuffer3;

/**
 * 批处理渲染类，一般用于渲染系统UI或2D图形
 * 
 * @author gustplus
 * 
 */
public class GPSpriteBuffer {
	float[] verticeBuffer;
	float[] texCoordBuffer;
	int vBufferIndex;
	int tBufferIndex;
	GPVertexBuffer3 vertices;
	int numSprites;

	private int maxSprite;

	private GPTexture2D texture;

	/**
	 * 构造器
	 * 
	 * @param maxSprite支持渲染的最大矩形数
	 * @param propramNum渲染器
	 *            编号
	 */
	public GPSpriteBuffer(int maxSprite, GPShader shader) {
		this.maxSprite = maxSprite;
		verticeBuffer = new float[maxSprite * 3 * 4];
		texCoordBuffer = new float[maxSprite * 2 * 4];
		vBufferIndex = 0;
		tBufferIndex = 0;
		this.numSprites = 0;
		this.vertices = new GPVertexBuffer3(shader);

		short[] indices = new short[maxSprite * 6];
		int len = indices.length;
		short j = 0;
		for (int i = 0; i < len; i += 6, j += 4) {
			indices[i + 0] = (short) (j + 0);
			indices[i + 1] = (short) (j + 1);
			indices[i + 2] = (short) (j + 2);
			indices[i + 3] = (short) (j + 2);
			indices[i + 4] = (short) (j + 3);
			indices[i + 5] = (short) (j + 0);
		}
		vertices.setIndices(indices, 0, len);
	}

	public GPShader getShader() {
		return this.vertices.getShader();
	}

	public int getMaxSpriteNum() {
		return maxSprite;
	}

	public void setTexture(GPTexture2D tex) {
		if (tex == null) {
			GPLogger.log("GPSpriteBuffer", "texture is null");
			return;
		}
		this.texture = tex;
	}

	public void clear() {
		numSprites = 0;
		vBufferIndex = 0;
		tBufferIndex = 0;
	}
	
	public void addSprite(GPVertexBuffer3 vertices){
		float[] vertice_float = vertices.getVertices();
		if(vertice_float == null){
			return;
		}
		float[] texCoords_float = vertices.getTexCoords();
		if(texCoords_float == null){
			return;
		}
		int index = 0;
		int index_tex = 0;
		verticeBuffer[vBufferIndex++] = vertice_float[index++];
		verticeBuffer[vBufferIndex++] = vertice_float[index++];
		verticeBuffer[vBufferIndex++] = vertice_float[index++];
		texCoordBuffer[tBufferIndex++] = texCoords_float[index_tex++];
		texCoordBuffer[tBufferIndex++] = texCoords_float[index_tex++];

		verticeBuffer[vBufferIndex++] = vertice_float[index++];
		verticeBuffer[vBufferIndex++] = vertice_float[index++];
		verticeBuffer[vBufferIndex++] = vertice_float[index++];
		texCoordBuffer[tBufferIndex++] = texCoords_float[index_tex++];
		texCoordBuffer[tBufferIndex++] = texCoords_float[index_tex++];

		verticeBuffer[vBufferIndex++] = vertice_float[index++];
		verticeBuffer[vBufferIndex++] = vertice_float[index++];
		verticeBuffer[vBufferIndex++] = vertice_float[index++];
		texCoordBuffer[tBufferIndex++] = texCoords_float[index_tex++];
		texCoordBuffer[tBufferIndex++] = texCoords_float[index_tex++];

		verticeBuffer[vBufferIndex++] = vertice_float[index++];
		verticeBuffer[vBufferIndex++] = vertice_float[index++];
		verticeBuffer[vBufferIndex++] = vertice_float[index++];
		texCoordBuffer[tBufferIndex++] = texCoords_float[index_tex++];
		texCoordBuffer[tBufferIndex++] = texCoords_float[index_tex++];
		
		numSprites++;
	}

	/**
	 * @param centerX
	 * @param centerY
	 * @param width
	 * @param height
	 * @param region
	 */
	public void addSprite(float centerX, float centerY, float width,
			float height, GPTextureRegion region) {
		float halfWidth = width / 2;
		float halfHeight = height / 2;

		float x1 = centerX - halfWidth;
		float y1 = centerY - halfHeight;
		float x2 = centerX + halfWidth;
		float y2 = centerY + halfHeight;

		verticeBuffer[vBufferIndex++] = x1;
		verticeBuffer[vBufferIndex++] = y1;
		verticeBuffer[vBufferIndex++] = 0;
		texCoordBuffer[tBufferIndex++] = region.u1;
		texCoordBuffer[tBufferIndex++] = region.v2;

		verticeBuffer[vBufferIndex++] = x2;
		verticeBuffer[vBufferIndex++] = y1;
		verticeBuffer[vBufferIndex++] = 0;
		texCoordBuffer[tBufferIndex++] = region.u2;
		texCoordBuffer[tBufferIndex++] = region.v2;

		verticeBuffer[vBufferIndex++] = x2;
		verticeBuffer[vBufferIndex++] = y2;
		verticeBuffer[vBufferIndex++] = 0;
		texCoordBuffer[tBufferIndex++] = region.u2;
		texCoordBuffer[tBufferIndex++] = region.v1;

		verticeBuffer[vBufferIndex++] = x1;
		verticeBuffer[vBufferIndex++] = y2;
		verticeBuffer[vBufferIndex++] = 0;
		texCoordBuffer[tBufferIndex++] = region.u1;
		texCoordBuffer[tBufferIndex++] = region.v1;

		numSprites++;
	}

	public void addSprite(float centerX, float centerY, float width,
			float height) {
		float halfWidth = width / 2;
		float halfHeight = height / 2;

		float x1 = centerX - halfWidth;
		float y1 = centerY - halfHeight;
		float x2 = centerX + halfWidth;
		float y2 = centerY + halfHeight;

		verticeBuffer[vBufferIndex++] = x1;
		verticeBuffer[vBufferIndex++] = y1;
		verticeBuffer[vBufferIndex++] = 0;
		texCoordBuffer[tBufferIndex++] = 0;
		texCoordBuffer[tBufferIndex++] = 1;

		verticeBuffer[vBufferIndex++] = x2;
		verticeBuffer[vBufferIndex++] = y1;
		verticeBuffer[vBufferIndex++] = 0;
		texCoordBuffer[tBufferIndex++] = 1;
		texCoordBuffer[tBufferIndex++] = 1;

		verticeBuffer[vBufferIndex++] = x2;
		verticeBuffer[vBufferIndex++] = y2;
		verticeBuffer[vBufferIndex++] = 0;
		texCoordBuffer[tBufferIndex++] = 1;
		texCoordBuffer[tBufferIndex++] = 0;

		verticeBuffer[vBufferIndex++] = x1;
		verticeBuffer[vBufferIndex++] = y2;
		verticeBuffer[vBufferIndex++] = 0;
		texCoordBuffer[tBufferIndex++] = 0;
		texCoordBuffer[tBufferIndex++] = 0;

		numSprites++;
	}

	/**
	 * 渲染（必须在此之前调用beginCatch（））
	 * 
	 * @param centerX
	 * @param centerY
	 * @param centerZ
	 * @param width
	 * @param height
	 * @param scale缩放值
	 * @param region
	 */
	public void addSprite3D(float centerX, float centerY, float centerZ,
			float width, float height, float scaleX, float scaleY,
			float rotate, GPTextureRegion region) {
		float halfWidth = width * scaleX / 2;
		float halfHeight = height * scaleY / 2;

		float rad = rotate * GPConstants.TO_RADIANS;
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);

		float x1 = -halfWidth * cos - (-halfHeight) * sin;
		float y1 = -halfWidth * sin + (-halfHeight) * cos;
		float x2 = halfWidth * cos - (-halfHeight) * sin;
		float y2 = halfWidth * sin + (-halfHeight) * cos;
		float x3 = halfWidth * cos - halfHeight * sin;
		float y3 = halfWidth * sin + halfHeight * cos;
		float x4 = -halfWidth * cos - halfHeight * sin;
		float y4 = -halfWidth * sin + halfHeight * cos;

		x1 += centerX;
		x2 += centerX;
		x3 += centerX;
		x4 += centerX;
		y1 += centerY;
		y2 += centerY;
		y3 += centerY;
		y4 += centerY;

		verticeBuffer[vBufferIndex++] = x1;
		verticeBuffer[vBufferIndex++] = y1;
		verticeBuffer[vBufferIndex++] = centerZ;
		texCoordBuffer[tBufferIndex++] = region.u1;
		texCoordBuffer[tBufferIndex++] = region.v2;

		verticeBuffer[vBufferIndex++] = x2;
		verticeBuffer[vBufferIndex++] = y2;
		verticeBuffer[vBufferIndex++] = centerZ;
		texCoordBuffer[tBufferIndex++] = region.u2;
		texCoordBuffer[tBufferIndex++] = region.v2;

		verticeBuffer[vBufferIndex++] = x3;
		verticeBuffer[vBufferIndex++] = y3;
		verticeBuffer[vBufferIndex++] = centerZ;
		texCoordBuffer[tBufferIndex++] = region.u2;
		texCoordBuffer[tBufferIndex++] = region.v1;

		verticeBuffer[vBufferIndex++] = x4;
		verticeBuffer[vBufferIndex++] = y4;
		verticeBuffer[vBufferIndex++] = centerZ;
		texCoordBuffer[tBufferIndex++] = region.u1;
		texCoordBuffer[tBufferIndex++] = region.v1;

		numSprites++;
	}

	public void addSprite3D(float centerX, float centerY, float centerZ,
			float width, float height, float scaleX, float scaleY,
			GPTextureRegion region) {
		float halfWidth = width * scaleX / 2;
		float halfHeight = height * scaleY / 2;

		float x1 = centerX - halfWidth;
		float y1 = centerY - halfHeight;
		float x2 = centerX + halfWidth;
		float y2 = centerY + halfHeight;
		verticeBuffer[vBufferIndex++] = x1;
		verticeBuffer[vBufferIndex++] = y1;
		verticeBuffer[vBufferIndex++] = centerZ;
		texCoordBuffer[tBufferIndex++] = region.u1;
		texCoordBuffer[tBufferIndex++] = region.v2;

		verticeBuffer[vBufferIndex++] = x2;
		verticeBuffer[vBufferIndex++] = y1;
		verticeBuffer[vBufferIndex++] = centerZ;
		texCoordBuffer[tBufferIndex++] = region.u2;
		texCoordBuffer[tBufferIndex++] = region.v2;

		verticeBuffer[vBufferIndex++] = x2;
		verticeBuffer[vBufferIndex++] = y2;
		verticeBuffer[vBufferIndex++] = centerZ;
		texCoordBuffer[tBufferIndex++] = region.u2;
		texCoordBuffer[tBufferIndex++] = region.v1;

		verticeBuffer[vBufferIndex++] = x1;
		verticeBuffer[vBufferIndex++] = y2;
		verticeBuffer[vBufferIndex++] = centerZ;
		texCoordBuffer[tBufferIndex++] = region.u1;
		texCoordBuffer[tBufferIndex++] = region.v1;

		numSprites++;
	}

	/**
	 * 渲染（必须在此之前调用beginCatch（））
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param angle
	 * @param region
	 */
	public void addSprite(float centerX, float centerY, float width,
			float height, float scaleX, float scaleY, float rotate,
			GPTextureRegion region) {
		addSprite3D(centerX, centerY, 0, width, height, scaleX, scaleY, rotate,
				region);
	}

	public void addSprite(float centerX, float centerY, float width,
			float height, float scaleX, float scaleY, GPTextureRegion region) {
		addSprite3D(centerX, centerY, 0, width, height, scaleX, scaleY, region);
	}
	
	public void flipToSize(){
		vertices.setVertices(verticeBuffer, 0, vBufferIndex);
		vertices.setTexCoords(texCoordBuffer, 0, tBufferIndex);
	}

	public void draw() {
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		if (texture == null) {
//			GPLogger.log("SpriteBuffer", "[error] : no texture");
			return;
		}
		texture.bind();
		vertices.bind();
		vertices.draw(GLES20.GL_TRIANGLES, 0, numSprites * 6);
	}
	
	public void setAlpha(float alpha){
		if(vertices != null){
			vertices.setAlpha(alpha);
		}
	}
	
	public void changeToColor(float[] color, float time){
		if(vertices != null){
			vertices.changeToColor(color, time);
		}
	}
	
	public void setFunctionID(int functionID){
		if(vertices != null){
			vertices.setFunctionID(functionID);
		}
	}
	
	public void reloadVBO(){
		vertices.reloadVBO();
	}
}
