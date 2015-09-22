package com.gust.render_engine.base;

import android.opengl.GLES20;

import com.gust.common.math.GPVector3f;
import com.gust.render_engine.core.GPTexture2D;
import com.gust.render_engine.core.GPVertexBuffer3;

public class GPGeometry extends GPNode {

	public GPVertexBuffer3 object;

	public GPTexture2D texture;

	public GPGeometry(GPVector3f position, GPVertexBuffer3 object) {
		// TODO Auto-generated constructor stub
		super(position);
		this.object = object;
		if (object != null)
			this.AABB = this.object.AABB;
		parent = null;
		children = null;
	}

	public void setTexture(GPTexture2D texture) {
		this.texture = texture;
	}

	@Override
	public void drawSelf(int type) {
		// TODO Auto-generated method stub
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		if (texture != null) {
			texture.bind();
		}
		object.bind();
		object.draw(type, 0, object.getNum());
	}
	
	public void setPosition(GPVector3f position){
		this.position = position;
	}

}
