package com.gust.common.animation2D;

import com.gust.action.GPSpriteAnimation;
import com.gust.common.math.GPVector3f;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.base.GPGeometry;
import com.gust.render_engine.base.GPSpatial;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.render_engine.core.GPVertexBuffer3;

public class GPLabelBoard extends GPGeometry implements
		Comparable<GPLabelBoard> {
	private GPTextureRegion region;
	public GPSpriteAnimation animation;
	float coords[];

	private GPLabelBoard(GPVector3f position, GPVertexBuffer3 object,
			GPVector3f angles, GPSpriteAnimation animation) {
		super(position, object);
		// TODO Auto-generated constructor stub
		this.angles = angles.clone();
		this.texture = animation.texture;
		this.animation = animation;
		this.region = animation.getCurRegion();
		animation.play();
	}

	public static GPLabelBoard create(GPVector3f position, GPVector3f angles,
			float width, float height, GPShader shader, GPSpriteAnimation animation) {
		float[] vertices = new float[] { -width / 2, height, 0, width / 2,
				height, 0, -width / 2, 0, 0, width / 2, 0, 0, };
		short[] indices = new short[] { 0, 2, 1, 1, 2, 3 };
		float[] texCoord = animation.getCurRegion().toArray();

		GPVertexBuffer3 object = new GPVertexBuffer3(shader);
		object.setVertices(vertices, 0, 12);
		object.setIndices(indices, 0, 6);
		object.setTexCoords(texCoord, 0, 8);
		object.setTexture(animation.texture);

		return new GPLabelBoard(position, object, angles, animation);
	}

	public void faceTo(GPSpatial object) {
		float yaw = object.angles.y;
		// float pitch = 90 - object.angles.x;
		// float roll = 90 - object.angles.z;
		this.angles.y = yaw;
	}

	public void update(float deltaTime) {
		animation.update(deltaTime);
		region = animation.getCurRegion();
		coords = region.toArray();
		object.setTexCoords(coords, 0, 8);
		// object.bind();
	}

	public boolean isDone() {
		return animation.isDone();
	}

	public void reset() {
		this.animation.reset();
	}

	public int compareTo(GPLabelBoard arg0) {
		// TODO Auto-generated method stub
		if (arg0.position.z > this.position.z)
			return -1;
		else
			return 1;

	}
}
