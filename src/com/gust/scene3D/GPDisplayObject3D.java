package com.gust.scene3D;

import java.util.ArrayList;

import com.gust.engine.core.GPShader;
import com.gust.physics3D.GPAABB;

public class GPDisplayObject3D {
	protected GPVertexBuffer3D vertices;
	protected ArrayList<GPCoordBuffer2D> coords;
	
	public GPDisplayObject3D(GPVertexBuffer3D vertices){
		this.vertices = vertices;
		coords = new ArrayList<GPCoordBuffer2D>(1);
	}

	public GPVertexBuffer3D getVertexBuffer() {
		return vertices;
	}
	
	public GPShader getShader(){
		return vertices.getShader();
	}

	public void setVertexBuffer(GPVertexBuffer3D vertices) {
		this.vertices = vertices;
	}

	public GPCoordBuffer2D getCoordBuffer(int index) {
		return coords.get(index);
	}

	public void addCoordBuffer(GPCoordBuffer2D coords) {
		coords.setShader(vertices.getShader());
		this.coords.add(coords);
	}
	
	public void bind(){
		int size = coords.size();
		for(int i = 0; i<size; ++i){
			coords.get(i).bind();
		}
		this.vertices.bind();
	}
	
	public GPAABB getAABB(){
		return this.vertices.AABB;
	}
	
	public void draw(int primitiveMode) {
		vertices.draw(primitiveMode, 0, vertices.getNum());
	}
	
//	public void draw(int primitiveMode, int offset, int num) {
//		shader.setTotalMatrix(this);
//		GPMatrixState.setModelMatrix();
//		GPMatrixState.setCameraMatrix();
//		if (indices != null) {
//			indices.position(offset);
//			GLES20.glDrawElements(primitiveMode, num, GLES20.GL_UNSIGNED_SHORT,
//					indices);
//		} else {
//			GLES20.glDrawArrays(primitiveMode, offset, num);
//		}
//	}
}
