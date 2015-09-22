package com.gust.render_engine.environment;

import com.gust.common.math.GPVector3f;
import com.gust.engine.core.GPShader;
import com.gust.physics3D.GPBoundingSphere;
import com.gust.render_engine.core.GPTexture2D;
import com.gust.render_engine.core.GPVertexBuffer3;
import com.gust.scene3D.GPDisplayObject3D;
import com.gust.scene3D.GPGeometry;

public class GPSkyDome extends GPGeometry {
	public float radius;
	public GPBoundingSphere sphere;

	private GPSkyDome(GPTexture2D skyTexture, GPDisplayObject3D object,
			GPVector3f position, float radius) {
		super(position, object);
//		this.setTexture(skyTexture);
		this.radius = radius;
		this.sphere = new GPBoundingSphere(position, this.radius*2.7f);
	}

	/**
	 * 
	 * @param divisionXoZ
	 *            纬度上分割的段数
	 * @param divisionXoY
	 *            经度上分割的段数
	 * @param radius
	 *            dome半径
	 */

//	public static GPSkyDome create(int divisionXoZ, int divisionXoY,
//			float radius, GPTexture2D skyTexture, GPShader shader,
//			GPVector3f position) {
//		int num = (divisionXoY + 1) * (divisionXoZ + 1);
//		float vertices[] = new float[num * 3];
//		short indices[] = new short[divisionXoY * divisionXoZ * 6];
//		float texCoords[] = new float[num * 2];
//
//		float ANGLE_SPAN_XOZ = 360 / divisionXoZ;
//		float ANGLE_SPAN_XOY = 90 / divisionXoY;
//		int vIndex = -1;
//		int cIndex = -1;
//		for (float vAngle = 90; vAngle >= 0; vAngle -= ANGLE_SPAN_XOY)// 垂直方向
//		{
//			float v = 1.0f - vAngle / 90.0f;
//			for (float hAngle = 360; hAngle >= 0; hAngle -= ANGLE_SPAN_XOZ)// 水平方向
//			{
//				double xozLength = radius * Math.cos(Math.toRadians(vAngle));
//				vertices[++vIndex] = (float) (xozLength * Math.cos(Math
//						.toRadians(hAngle)));
//				vertices[++vIndex] = (float) (radius * Math.sin(Math
//						.toRadians(vAngle)));
//				vertices[++vIndex] = (float) (xozLength * Math.sin(Math
//						.toRadians(hAngle)));
//
//				texCoords[++cIndex] = 1.0f - hAngle / 360;
//				texCoords[++cIndex] = v;
//			}
//		}
//
//		vIndex = 0;
//		for (int i = 0; i < divisionXoY; i++) {
//			for (int j = 0; j < divisionXoZ; j++) {
//				indices[vIndex] = (short) ((divisionXoZ + 1) * i + j);
//				indices[vIndex + 1] = (short) ((divisionXoZ + 1) * i + j + 1);
//				indices[vIndex + 2] = (short) ((divisionXoZ + 1) * (i + 1) + j);
//				indices[vIndex + 3] = (short) ((divisionXoZ + 1) * (i + 1) + j);
//				indices[vIndex + 4] = (short) ((divisionXoZ + 1) * i + j + 1);
//				indices[vIndex + 5] = (short) ((divisionXoZ + 1) * (i + 1) + j + 1);
//				vIndex += 6;
//			}
//		}
//
//		GPVertexBuffer3 sky = new GPVertexBuffer3(shader);
//		sky.setVertices(vertices, 0, vertices.length);
//		sky.setIndices(indices, 0, indices.length);
//		sky.setTexCoords(texCoords, 0, texCoords.length);
//
//		GPSkyDome dome = new GPSkyDome(skyTexture, sky, position, radius);
//
//		return dome;
//	}
//
//	public void setTexture(GPTexture2D tex) {
//		this.texture = tex;
//	}

}
