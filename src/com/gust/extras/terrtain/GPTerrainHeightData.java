package com.gust.extras.terrtain;

import com.gust.common.math.GPVector2f;
import com.gust.common.math.GPVector3f;

public class GPTerrainHeightData {
	public float heights[][];
	public int rows;
	public int cols;
	public GPVector2f halfBound; // ���εĳ����һ��
	public GPVector2f perBound; // ÿ����ĳ���
	public GPVector3f offset;

	public GPTerrainHeightData(float[][] heights, int rows, int cols,
			GPVector2f halfBound, GPVector2f perBound, GPVector3f position) {
		this.heights = heights;
		this.rows = rows;
		this.cols = cols;
		this.halfBound = halfBound;
		this.perBound = perBound;
		this.offset = position;
	}

	public void setData(int row, int col, float height) {
		if (row < rows && col < cols && row > 0 && col > 0) {
			heights[row][col] = height;
		}
	}
}
