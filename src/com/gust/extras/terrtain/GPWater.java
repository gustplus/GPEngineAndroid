package com.gust.extras.terrtain;

import com.gust.common.math.GPVector2f;
import com.gust.common.math.GPVector3f;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.base.GPGeometry;
import com.gust.render_engine.core.GPTexture2D;
import com.gust.scene3D.GPVertexBuffer3D;

public class GPWater extends GPGeometry {
	private boolean isCollusion;
	protected GPTerrainHeightData data;
//	private int programNum;
	private GPShader shader;
	private float maxHeight;
	private float[][] heights;
	private int colsNum;
	private int rowsNum;
	private float[][] velocitys;
	private float[][] forces;

	public GPWater(GPShader shader, GPVector3f position, GPVector3f bound,
			int rowNum, int colNum, boolean isCollusion) {
		super(position, null);
		// TODO Auto-generated constructor stub
		float maxX = bound.x;
		float maxY = bound.y;
		maxHeight = bound.z;
		this.isCollusion = isCollusion;
//		this.programNum = programNum;
		this.shader = shader;

		colsNum = colNum;
		rowsNum = rowNum;
		heights = new float[rowNum][colNum];
		velocitys = new float[rowNum][colNum];
		forces = new float[rowNum][colNum];
		create(maxX * 2, maxY * 2, rowNum, colNum);
	}

	private void create(float width, float height, int rowNum, int colNum) {
		createVertices(width, height, colNum, rowNum);
	}

	private void createVertices(float width, float height, int cols, int rows) {
		int maxVertices = cols * rows;
		int maxIndices = (cols - 1) * (rows - 1) * 2 * 3;
		float perLength = height / (rows - 1);
		float perWidth = width / (cols - 1);
		
		heights = new float[rows][cols];
		for (int z = 0; z < rowsNum; ++z) {
			for (int x = 0; x < colsNum; ++x) {
				heights[z][x] = 0;
				forces[z][x] = 0;
			}
		}

		GPVertexBuffer3D result = new GPVertexBuffer3D(shader);

		float[] vertices = new float[maxVertices * 3];
		float[] texcoords = new float[maxVertices * 2];
		float tempX, tempZ;
		float left = -width / 2;
		float top = -height / 2;
		int index = 0;
		int textureIndex = 0;
		for (int i = 0; i < rows; i++) {
			tempZ = top + perLength * i;
			for (int j = 0; j < cols; j++) {
				tempX = left + perWidth * j;

				vertices[index++] = tempX;
				vertices[index++] = heights[i][j];;
				vertices[index++] = tempZ;
			}
		}
		result.setVertices(vertices, 0, index);
//		result.setTexCoords(texcoords, 0, textureIndex);

		index = 0;
		short[] indices = new short[maxIndices];
		for (int k = 0; k < (rows - 1); k++)
			for (int l = 0; l < (cols - 1); l++) {
				indices[index++] = (short) (k * cols + l);
				indices[index++] = (short) ((k + 1) * cols + l);
				indices[index++] = (short) (k * cols + l + 1);
				indices[index++] = (short) ((k + 1) * cols + l + 1);
				indices[index++] = (short) (k * cols + l + 1);
				indices[index++] = (short) ((k + 1) * cols + l);
			}
		result.setIndices(indices, 0, index);
//		object = result;

		if (isCollusion) {
			data = new GPTerrainHeightData(heights, rows, cols, new GPVector2f(
					width / 2, height / 2),
					new GPVector2f(perWidth, perLength), position);
		}
	}

	public void setReflactMap(GPTexture2D reflactMap) {
		this.setTexture(reflactMap);
	}

	
	public void setRipplePoint(int row, int col, float power) {
		float h = power * -maxHeight;
		this.heights[row][col] = h;
//		this.object.setValueAtVertices(row * colsNum + col, h);
	}
	

	public void update(float deltaTime) {
		float d;
		float fTempF;
		float fVert;
		int x, z;

		// calculate the current forces acting upon the water
		for (z = 1; z < rowsNum - 1; z++) {
			for (x = 1; x < colsNum - 1; x++) {
				fTempF = forces[z][x];
				fVert = heights[z][x];

				// bottom
				d = fVert - heights[z - 1][x];
				forces[z - 1][x] += d;
				fTempF -= d;

				// left
				d = fVert - heights[z][x - 1];
				forces[z][x - 1] += d;
				fTempF -= d;

				// top
				d = fVert - heights[z + 1][x];
				forces[z + 1][x] += d;
				fTempF -= d;

				// right
				d = fVert - heights[z][x + 1];
				forces[z][x + 1] += d;
				fTempF -= d;

				// upper right
				d = (fVert - heights[z + 1][x + 1]) * 4.94974747f;
				forces[z + 1][x + 1] += d;
				fTempF -= d;

				// lower left
				d = (fVert - heights[z - 1][x - 1]) * 4.94974747f;
				forces[z - 1][x - 1] += d;
				fTempF -= d;

				// lower right
				d = (fVert - heights[z - 1][x + 1]) * 4.94974747f;
				forces[z - 1][x + 1] += d;
				fTempF -= d;

				// upper left
				d = (fVert - heights[z + 1][x - 1]) * 4.94974747f;
				forces[z + 1][x - 1] += d;
				fTempF -= d;

				forces[z][x] = fTempF;
			}
		}
		
		for (z = 0; z < rowsNum; ++z)
			for (x = 0; x < colsNum; ++x) {
				velocitys[z][x] += (forces[z][x] * deltaTime);
				heights[z][x] += velocitys[z][x] * deltaTime;
//				this.object.setValueAtVertices(z * colsNum + x, heights[z][x]);
				forces[z][x] = 0.0f;
			}
		
//		object.updateNormals();
	}
}
