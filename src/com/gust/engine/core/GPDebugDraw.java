package com.gust.engine.core;

import java.util.ArrayList;

import android.opengl.GLES20;

import com.gust.common.math.GPConstants;
import com.gust.common.math.GPMatrix4;
import com.gust.common.math.GPVector2f;
import com.gust.common.math.GPVector3f;
import com.gust.physics2D.Bound_Circle;
import com.gust.physics2D.Bound_Polygon;
import com.gust.physics2D.Bound_Rectangle;
import com.gust.physics2D.GPLine;
import com.gust.physics3D.GPAABB;
import com.gust.render_engine.core.Camera;
import com.gust.render_engine.core.GPOpenGLStateManager;
import com.gust.scene3D.GPDebugVertex3D;

public class GPDebugDraw {
	public ArrayList<GPDebugVertex3D> debugDraw;
	private GPShader shader;
	private Camera camera;

	private int totalMatrixHandler;

	public GPDebugDraw(Camera camera) {
		shader = GPShaderManager.getInstance().getShader("debug");
		this.camera = camera;
		totalMatrixHandler = shader.getUniformLocation(GPShaderManager.MVP_MATRIX_NAME);
		GPOpenGLStateManager.getInstance().setLineWidth(2);
	}

	public GPShader getShader() {
		return shader;
	}

	/**
	 * 将总矩阵传给着色器
	 */
	public void setTotalMatrix() {
		GLES20.glUniformMatrix4fv(totalMatrixHandler, 1, false, camera
				.getProjectionAndViewMatrix().getData(), 0);
	}

	public void drawLine(GPVector2f startpoint, GPVector2f endPoint,
			GPVector3f color) {
		GPDebugVertex3D line = new GPDebugVertex3D(null);
		float[] vertices = new float[6];
		vertices[0] = startpoint.x;
		vertices[1] = startpoint.y;
		vertices[2] = 0;
		vertices[3] = endPoint.x;
		vertices[4] = endPoint.y;
		vertices[5] = 0;
		line.setVertices(vertices, 0, 6);

		float[] colors = new float[8];
		colors[0] = color.x;
		colors[1] = color.y;
		colors[2] = color.z;
		colors[3] = 1;
		colors[4] = color.x;
		colors[5] = color.y;
		colors[6] = color.z;
		colors[7] = 1;
		line.setColors(colors, 0, 8);

		if (debugDraw == null) {
			debugDraw = new ArrayList<GPDebugVertex3D>(10);
		}
		debugDraw.add(line);
	}

	public void drawLine(GPLine line, GPVector3f color) {
		GPDebugVertex3D lineVertice = new GPDebugVertex3D(null);
		float[] vertices = new float[6];
		vertices[0] = line.getStartPoint().x;
		vertices[1] = line.getStartPoint().y;
		vertices[2] = 0;
		vertices[3] = line.getEndPoint().x;
		vertices[4] = line.getEndPoint().y;
		vertices[5] = 0;
		lineVertice.setVertices(vertices, 0, 6);

		float[] colors = new float[8];
		colors[0] = color.x;
		colors[1] = color.y;
		colors[2] = color.z;
		colors[3] = 1;
		colors[4] = color.x;
		colors[5] = color.y;
		colors[6] = color.z;
		colors[7] = 1;
		lineVertice.setColors(colors, 0, 8);

		if (debugDraw == null) {
			debugDraw = new ArrayList<GPDebugVertex3D>(10);
		}
		debugDraw.add(lineVertice);
	}
	
	public void drawPolygon(Bound_Polygon polygon, GPVector3f color){
		GPLine[] lines = polygon.getLines();
		int len = lines.length;
		for (int i = 0; i < len; ++i) {
			drawLine(lines[i], color);
		}
	}

	public void drawRect(Bound_Rectangle rect, GPVector3f color) {
		GPDebugVertex3D drawRect = new GPDebugVertex3D(null);
		float[] vertices = new float[12];
		vertices[0] = rect.lowerLeft.x;
		vertices[1] = rect.lowerLeft.y;
		vertices[2] = 0;
		vertices[3] = rect.lowerLeft.x + rect.width;
		vertices[4] = rect.lowerLeft.y;
		vertices[5] = 0;
		vertices[6] = rect.lowerLeft.x + rect.width;
		vertices[7] = rect.lowerLeft.y + rect.height;
		vertices[8] = 0;
		vertices[9] = rect.lowerLeft.x;
		vertices[10] = rect.lowerLeft.y + rect.height;
		vertices[11] = 0;
		drawRect.setVertices(vertices, 0, 12);

		float[] colors = new float[16];
		colors[0] = color.x;
		colors[1] = color.y;
		colors[2] = color.z;
		colors[3] = 1;
		colors[4] = color.x;
		colors[5] = color.y;
		colors[6] = color.z;
		colors[7] = 1;
		colors[8] = color.x;
		colors[9] = color.y;
		colors[10] = color.z;
		colors[11] = 1;
		colors[12] = color.x;
		colors[13] = color.y;
		colors[14] = color.z;
		colors[15] = 1;
		drawRect.setColors(colors, 0, 16);

		if (debugDraw == null) {
			debugDraw = new ArrayList<GPDebugVertex3D>(10);
		}
		debugDraw.add(drawRect);
	}

	/**
	 * 
	 * @param circle
	 * @param part
	 *            圆的段数，越高越精细
	 */
	public void drawCircle(Bound_Circle circle, int part, GPVector3f color) {
		GPDebugVertex3D drawCircle = new GPDebugVertex3D(null);
		int num = part;
		float rad = circle.radius;
		float perRotate = 360f / part;
		float rotate = 0;
		float[] vertices = new float[num * 3];
		float[] colors = new float[num * 4];
		for (int i = 0; i < part; ++i) {
			vertices[i * 3] = rad
					* (float) Math.cos(rotate * GPConstants.TO_RADIANS)
					+ circle.center.x;
			vertices[i * 3 + 1] = rad
					* (float) Math.sin(rotate * GPConstants.TO_RADIANS)
					+ circle.center.y;
			vertices[i * 3 + 2] = 0;

			colors[i * 4] = color.x;
			colors[i * 4 + 1] = color.y;
			colors[i * 4 + 2] = color.z;
			colors[i * 4 + 3] = 1;
			rotate += perRotate;
		}
		drawCircle.setVertices(vertices, 0, num * 3);
		drawCircle.setColors(colors, 0, num * 4);

		if (debugDraw == null) {
			debugDraw = new ArrayList<GPDebugVertex3D>(10);
		}
		debugDraw.add(drawCircle);
	}

	public void drawCuboid(GPMatrix4 matrix, GPAABB aabb, GPVector3f colors) {
		GPDebugVertex3D box = new GPDebugVertex3D(matrix);
		GPVector3f dist = aabb.getRight_Bottom_Behind().sub(
				aabb.getLeft_Up_Front());
		float halfX = Math.abs(dist.x);
		float halfY = Math.abs(dist.y);
		float halfZ = Math.abs(dist.z);
		box.setVertices(new float[] { -halfX, halfY, halfZ, -halfX, -halfY,
				halfZ, halfX, halfY, halfZ, halfX, -halfY, halfZ, -halfX,
				halfY, -halfZ, -halfX, -halfY, -halfZ, halfX, halfY, -halfZ,
				halfX, -halfY, -halfZ }, 0, 24);
		box.setIndices(new short[] { 0, 1, 2, 1, 2, 3, 4, 5, 0, 0, 5, 1, 2, 3,
				6, 3, 6, 7, 6, 7, 5, 6, 5, 4, 0, 6, 4, 0, 2, 6, 1, 5, 3, 3, 5,
				7 }, 0, 36);
		box.setColors(new float[] { colors.x, colors.y, colors.z, 1, colors.x,
				colors.y, colors.z, 1, colors.x, colors.y, colors.z, 1,
				colors.x, colors.y, colors.z, 1, colors.x, colors.y, colors.z,
				1, colors.x, colors.y, colors.z, 1, colors.x, colors.y,
				colors.z, 1, colors.x, colors.y, colors.z, 1 }, 0, 32);
		if (debugDraw == null) {
			debugDraw = new ArrayList<GPDebugVertex3D>(10);
		}
		debugDraw.add(box);
	}

	public void drawBall(GPVector3f position, float radius, GPVector3f color,
			int part) {
		int num = (part + 1) * (part / 2 + 1);
		float vertices[] = new float[num * 3];
		short indices[] = new short[part * part * 6];
		float colors[] = new float[num * 4];

		float ANGLE_SPAN_XOZ = 360 / part;
		float ANGLE_SPAN_XOY = 180 / part;
		int vIndex = -1;
		int cIndex = -1;
		for (float vAngle = 180; vAngle >= 0; vAngle -= ANGLE_SPAN_XOY)// 垂直方向
		{
			for (float hAngle = 360; hAngle >= 0; hAngle -= ANGLE_SPAN_XOZ)// 水平方向
			{
				double xozLength = radius * Math.cos(Math.toRadians(vAngle));
				vertices[++vIndex] = (float) (xozLength * Math.cos(Math
						.toRadians(hAngle)));
				vertices[++vIndex] = (float) (radius * Math.sin(Math
						.toRadians(vAngle)));
				vertices[++vIndex] = (float) (xozLength * Math.sin(Math
						.toRadians(hAngle)));

				colors[++cIndex] = color.x;
				colors[++cIndex] = color.y;
				colors[++cIndex] = color.z;
				colors[++cIndex] = 1;
			}
		}

		vIndex = 0;
		for (int i = 0; i < part; i++) {
			for (int j = 0; j < part / 2; j++) {
				indices[vIndex] = (short) ((part + 1) * i + j);
				indices[vIndex + 1] = (short) ((part + 1) * i + j + 1);
				indices[vIndex + 2] = (short) ((part + 1) * (i + 1) + j);
				indices[vIndex + 3] = (short) ((part + 1) * (i + 1) + j);
				indices[vIndex + 4] = (short) ((part + 1) * i + j + 1);
				indices[vIndex + 5] = (short) ((part + 1) * (i + 1) + j + 1);
				vIndex += 6;
			}
		}

		GPDebugVertex3D box = new GPDebugVertex3D();
		box.setVertices(vertices, 0, vertices.length);
		box.setIndices(indices, 0, indices.length);
		box.setColors(colors, 0, colors.length);
		if (debugDraw == null) {
			debugDraw = new ArrayList<GPDebugVertex3D>(10);
		}
		debugDraw.add(box);
	}

	public void setLineWidth(int size) {
		GLES20.glLineWidth(size);
	}

	public void clear() {
		debugDraw.clear();
	}

//	public void draw() {
//		int len = debugDraw.size();
//		for (int i = 0; i < len; ++i) {
//			shader.bind();
//			setTotalMatrix();
//			GPDebugVertex3D vertices = debugDraw.get(i);
//			vertices.bind();
//			vertices.draw(GLES20.GL_LINE_LOOP, 0, vertices.getNum());
//		}
//		debugDraw.clear();
//	}
}
