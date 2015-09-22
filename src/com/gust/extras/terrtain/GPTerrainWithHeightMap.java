package com.gust.extras.terrtain;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLES20;

import com.gust.common.math.GPVector2f;
import com.gust.common.math.GPVector3f;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.GPTexture2D;
import com.gust.scene3D.GPVertexBuffer3D;

/**
 * 地形构造器
 * 
 * @author gustplus
 * 
 */
public class GPTerrainWithHeightMap extends GPTerrain {
	GPTexture2D vegetation;
	int vega1Handler;
	int vega2Handler;
	int changeHeightHandler;
	int crossLengthHandler;
	private float totalHeight;
	private GPShader shader;
	private int nums;
	float changeHeight;
	float crossLength;

	int rowNum;
	int colNum;

	/**
	 * 
	 * @param programNum
	 *            着色器编号
	 * @param vegetation1低海拔植被纹理
	 * @param vegetation2高海拔植被纹理
	 * @param heightmap用于生成地形的灰度图
	 * @param totalHeight总高度
	 * @param changeHeight从低海拔纹理过渡到高海拔纹理的起始海拔
	 * @param crossLength从低海拔纹理过渡到高海拔纹理的总长度
	 */
	public GPTerrainWithHeightMap(GPShader shader, GPTexture2D vegetation1,
			GPTexture2D vegetation2, float totalHeight, float changeHeight,
			float crossLength, GPVector3f position) {
		super(position);
		this.shader = shader;
		this.texture = vegetation1;
		this.vegetation = vegetation2;
		this.totalHeight = totalHeight;
		this.changeHeight = changeHeight;
		this.crossLength = crossLength;

		this.changeHeightHandler = shader.getUniformLocation("height");
		this.crossLengthHandler = shader.getUniformLocation("across");
		this.vega1Handler = shader.getUniformLocation("fTexture");
		this.vega2Handler = shader.getUniformLocation("sTexture");
	}

	/**
	 * 生成地形
	 * 
	 * @param colLength生成的地形每一列的宽度
	 * @param rowWidth生成的地形每一行的高度
	 * @param wrapS纹理的重复次数
	 *            （行）
	 * @param wrapT纹理的重复次数
	 *            （列）
	 */
	public void createLandform(Bitmap heightmap, float colLength,
			float rowWidth, float wrapS, float wrapT, boolean releaseAfter) {
		rowNum = heightmap.getHeight();
		colNum = heightmap.getWidth();
		float[][] heights = new float[rowNum][colNum];
		for (int i = 0; i < rowNum; i++)
			for (int j = 0; j < colNum; j++) {
				int color = heightmap.getPixel(i, j);
				int red = Color.red(color);
				int green = Color.green(color);
				int blue = Color.blue(color);
				heights[j][i] = (float) (red + green + blue) / 255 / 3
						* totalHeight;
			}
		if (releaseAfter)
			heightmap.recycle();
		createVertices(heights, colNum, rowNum, colLength, rowWidth, wrapS,
				wrapT);
	}
	
	public void setPosition(GPVector3f position) {
		// TODO Auto-generated method stub
		this.position = position;
		this.data.offset = position;
	}

	private void createVertices(float[][] heights, int cols, int rows,
			float colLength, float rowWidth, float wrapS, float wrapT) {
		int maxVertices = cols * rows;
		int maxIndices = (cols - 1) * (rows - 1) * 2 * 3;
		float perLength = colLength / (cols - 1);
		float perWidth = rowWidth / (rows - 1);

		data = new GPTerrainHeightData(heights, rows, cols, new GPVector2f(
				rowWidth / 2, colLength / 2), new GPVector2f(perWidth,
				perLength), position);

		GPVertexBuffer3D result = new GPVertexBuffer3D(shader);

		float[] vertices = new float[maxVertices * 3];
		float[] texcoords = new float[maxVertices * 2];
		float tempX, tempY, tempZ;
		float tempU, tempV;
		float left = -rowWidth / 2;
		float top = -colLength / 2;
		int index = 0;
		int textureIndex = 0;
		for (int i = 0; i < rows; i++) {
			tempZ = top + perLength * i;
			tempV = wrapT * (1 - i / (float) rows);
			for (int j = 0; j < cols; j++) {
				tempX = left + perWidth * j;
				tempY = heights[i][j];
				tempU = wrapS * (1 - j / (float) cols);

				vertices[index++] = tempX;
				vertices[index++] = tempY;
				vertices[index++] = tempZ;

				texcoords[textureIndex++] = tempU;
				texcoords[textureIndex++] = tempV;
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
		nums = index;
		result.setIndices(indices, 0, index);
//		object = result;
	}

	/**
	 * 渲染地形
	 */
	public void drawSelf(int type) {
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		vegetation.bind();
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		texture.bind();
		object.bind();
		shader.bind();
		GLES20.glUniform1i(vega1Handler, 0);
		GLES20.glUniform1i(vega2Handler, 1);
		GLES20.glUniform1f(changeHeightHandler, changeHeight + position.y);
		GLES20.glUniform1f(crossLengthHandler, crossLength);
		object.draw(type, 0, nums);
		GLES20.glBindTexture(type, 0);
//		 GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	}

	public void draw() {
		draw(GLES20.GL_TRIANGLES);
	}

	public void setTexture(GPTexture2D texture, GPTexture2D texture2) {
		// TODO Auto-generated method stub
		super.setTexture(texture);
		this.vegetation = texture2;
	}

	@Override
	public int getRowNum() {
		// TODO Auto-generated method stub
		return rowNum;
	}

	@Override
	public int getColNum() {
		// TODO Auto-generated method stub
		return colNum;
	}

}
