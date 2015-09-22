package com.gust.extras.terrtain;

import com.gust.common.game_util.GPLogger;
import com.gust.common.math.GPVector2f;
import com.gust.common.math.GPVector3f;
import com.gust.game_entry3D.GPGameObject3D;
import com.gust.render_engine.base.GPNode;

public class GPTerrainGroup {
	public GPTerrain terrainGroup[];
	public GPCollidedTerrain collusionData[];
	public GPVector3f center;
	public int num;
	public int rows;
	public int cols;
	public float perWidth;
	public float perHeight;
	public float width;
	public float height;

	public GPVector3f left_top;

	public GPTerrainGroup(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		terrainGroup = new GPTerrain[rows * cols];
		collusionData = new GPCollidedTerrain[rows * cols];
	}

	public void addTerrain(int index, GPTerrain terrain) {
		if (index < terrainGroup.length) {
			if (terrainGroup[index] == null) {
				num += 1;
			}
			terrainGroup[index] = terrain;
		}
	}

	public void create(float totalWidth, float totalHeight, GPVector3f center,
			GPNode parent) {
		if (num < terrainGroup.length) {
			GPLogger.log("terrainGroup", "terrains are not enough");
		} else {
			this.width = totalWidth;
			this.height = totalHeight;
			this.center = center;
			perWidth = totalWidth / cols;
			perHeight = totalHeight / rows;
			left_top = center.sub(totalWidth / 2, 0, totalHeight / 2);
			GPVector3f start = left_top.add(perWidth / 2, 0, perHeight / 2);
			for (int row = 0; row < rows; ++row) {
				for (int col = 0; col < cols; ++col) {
					int i = row * cols + col;
					terrainGroup[i].position = start.add(col * perWidth, 0, row
							* perHeight);
					GPTerrainHeightData data1;
					GPTerrainHeightData data2;
					if (col > 0 && col < cols - 1) {
						data1 = terrainGroup[i - 1]
								.getDatas();
						data2 = terrainGroup[i + 1]
								.getDatas();
						for (int allRow = 0; allRow < data1.rows; ++allRow) {
							float changeHeight = data1.heights[allRow][data1.cols - 1];
							terrainGroup[i].changeData(allRow, 0, changeHeight);
							changeHeight = data2.heights[allRow][0];
							terrainGroup[i].changeData(allRow, data2.cols - 1,
									changeHeight);
						}
					}
					if (row > 0 && row < rows - 1) {
						data1 = terrainGroup[i - cols].getDatas();
						data2 = terrainGroup[i + cols].getDatas();
						for (int allCol = 0; allCol < data1.cols; ++allCol) {
							float changeHeight = data1.heights[data1.rows - 1][allCol];
							terrainGroup[i].changeData(0, allCol, changeHeight);
							changeHeight = data2.heights[0][allCol];
							terrainGroup[i].changeData(data2.rows - 1, allCol,
									changeHeight);
						}
					}

					GPCollidedTerrain collusion = new GPCollidedTerrain();
					collusion.getDatas(terrainGroup[i]);
					collusionData[i] = collusion;
					parent.addChild(terrainGroup[i], "group " + i);
				}
			}
		}
	}

	public GPTerrain getTerrainByPosition(GPVector3f position) {
		int col = (int) ((position.x - left_top.x) / perWidth);
		int row = (int) ((position.z - left_top.z) / perHeight);

		return terrainGroup[row * cols + col];
	}

	public GPCollidedTerrain getCollusionByPosition(GPVector3f position) {
		int col = (int) ((position.x - left_top.x) / perWidth);
		int row = (int) ((position.z - left_top.z) / perHeight);
		return collusionData[row * cols + col];
	}

	public int[] getTerrainNumByPosition(GPVector3f position) {
		int col = (int) ((position.x - left_top.x) / perWidth);
		int row = (int) ((position.z - left_top.z) / perHeight);
		return new int[] { row, col };
	}

	public void update(GPVector3f viewPoint, GPVector3f viewDirection,
			float visibleRadius) {
		if (num < terrainGroup.length) {
			GPLogger.log("terrainGroup", "terrains are not enough");
		} else {
			float perBoundingCircle = (float) (Math
					.sqrt((perWidth * perWidth + perHeight * perHeight)) / 2);
			for (int i = 0; i < num; i++) {
				float dist = terrainGroup[i].position.dist(viewPoint);
				if (dist > visibleRadius + perBoundingCircle) {
					terrainGroup[i].setVisible(false);
				} else {
					terrainGroup[i].setVisible(true);
				}
			}
		}
	}

	public GPVector2f update(GPVector3f movement, GPVector3f direction,
			GPGameObject3D object) {
		GPVector3f position = object.position.clone();
		GPCollidedTerrain terrain = getCollusionByPosition(position
				.add(movement));
		terrain.getSituation(object, movement, direction);
		return terrain.getRotatation();
	}

	public void reset() {
		this.num = 0;
	}

	public GPTerrain getTerrain(int index) {
		return terrainGroup[index];
	}

	public boolean isIn(GPGameObject3D object) {
		// TODO Auto-generated method stub
		float x = object.position.x;
		float y = object.position.z;

		if (x > -width / 2 + center.x && x < width / 2 + center.x
				&& y > -height / 2 + center.z && y < height / 2 + center.z)
			return true;
		else
			return false;
	}
}
