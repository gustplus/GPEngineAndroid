package com.gust.scene2D;

import android.opengl.GLES20;

import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.render_engine.core.GPVertexBuffer3;

public class SpriteRect {
	private GPVertexBuffer3 vertices;

	public SpriteRect(GPTextureRegion region, GPShader shader) {
		super();
		this.vertices = new GPVertexBuffer3(shader);
		short[] indices = new short[6];
		indices[0] = (short) (0);
		indices[1] = (short) (1);
		indices[2] = (short) (2);
		indices[3] = (short) (2);
		indices[4] = (short) (3);
		indices[5] = (short) (0);
		vertices.setIndices(indices, 0, 6);
		
		float[] texCoordBuffer = new float[8];
		if (region.isRotated()) {
			texCoordBuffer[0] = region.u1;
			texCoordBuffer[1] = region.v1;
			texCoordBuffer[2] = region.u1;
			texCoordBuffer[3] = region.v2;
			texCoordBuffer[4] = region.u2;
			texCoordBuffer[5] = region.v2;
			texCoordBuffer[6] = region.u2;
			texCoordBuffer[7] = region.v1;
		} else {
			texCoordBuffer[0] = region.u1;
			texCoordBuffer[1] = region.v2;
			texCoordBuffer[2] = region.u2;
			texCoordBuffer[3] = region.v2;
			texCoordBuffer[4] = region.u2;
			texCoordBuffer[5] = region.v1;
			texCoordBuffer[6] = region.u1;
			texCoordBuffer[7] = region.v1;
		}
		vertices.setTexCoords(texCoordBuffer, 0, 8);
	}
	
	public SpriteRect(GPShader shader) {
		super();
		this.vertices = new GPVertexBuffer3(shader);
		short[] indices = new short[6];
		indices[0] = (short) (0);
		indices[1] = (short) (1);
		indices[2] = (short) (2);
		indices[3] = (short) (2);
		indices[4] = (short) (3);
		indices[5] = (short) (0);
		vertices.setIndices(indices, 0, 6);
	}

	public GPShader getShader() {
		return vertices.getShader();
	}

	public void setVertices(float[] vertices) {
		this.vertices.setVertices(vertices, 0, 12);
	}

	public void setGPTextureRegion(GPTextureRegion region) {
		float[] texCoordBuffer = new float[8];
		if (region.isRotated()) {
			texCoordBuffer[0] = region.u1;
			texCoordBuffer[1] = region.v1;
			texCoordBuffer[2] = region.u1;
			texCoordBuffer[3] = region.v2;
			texCoordBuffer[4] = region.u2;
			texCoordBuffer[5] = region.v2;
			texCoordBuffer[6] = region.u2;
			texCoordBuffer[7] = region.v1;
		} else {
			texCoordBuffer[0] = region.u1;
			texCoordBuffer[1] = region.v2;
			texCoordBuffer[2] = region.u2;
			texCoordBuffer[3] = region.v2;
			texCoordBuffer[4] = region.u2;
			texCoordBuffer[5] = region.v1;
			texCoordBuffer[6] = region.u1;
			texCoordBuffer[7] = region.v1;
		}
		vertices.setTexCoords(texCoordBuffer, 0, 8);
	}
	
	public void reloadVBO(){
		this.vertices.reloadVBO();
	}

	public void bind() {
		this.vertices.bind();
	}

	public void draw() {
		this.vertices.draw(GLES20.GL_TRIANGLES, 0, 6);
	}

	public GPVertexBuffer3 getVerticeBuffer() {
		return vertices;
	}
	
	public void setAlpha(float alpha){
		vertices.setAlpha(alpha);
	}
	
public void changeToColor(float[] color, float time){
		vertices.changeToColor(color, time);
	}
}
