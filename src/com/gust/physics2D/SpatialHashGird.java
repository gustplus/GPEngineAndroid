package com.gust.physics2D;

import java.util.ArrayList;
import java.util.List;

import android.util.FloatMath;

import com.gust.game_entry2D.GPGameObject_Rect;

public class SpatialHashGird {
	List<GPGameObject_Rect>[] dynamicObjects;
	List<GPGameObject_Rect>[] staticObjects;
	int cellsPerRow;
	int cellsPerCol;
	float cellSize;
	int[] cellIds;
	List<GPGameObject_Rect> foundObject;

	public SpatialHashGird(float worldWidth, float worldHeight, float cellSize)
	{
		this.cellSize = cellSize;
		cellsPerRow = (int) FloatMath.ceil(worldWidth / cellSize);
		cellsPerCol = (int) FloatMath.ceil(worldHeight / cellSize);
		int numCells = cellsPerCol * cellsPerRow;
		dynamicObjects = new List[numCells];
		staticObjects = new List[numCells];
		for (int i = 0; i < numCells; i++) {
			dynamicObjects[i] = new ArrayList<GPGameObject_Rect>(10);
			staticObjects[i] = new ArrayList<GPGameObject_Rect>(10);
		}
		foundObject = new ArrayList<GPGameObject_Rect>(10);
	}

	public int[] getGridIds(GPGameObject_Rect obj)
	{
		int[] ids = new int[4];
		int x1 = (int) FloatMath.floor(obj.bound.lowerLeft.x / cellSize);
		int x2 = (int) FloatMath
				.floor((obj.bound.lowerLeft.x + obj.bound.width) / cellSize);
		int y1 = (int) FloatMath.floor(obj.bound.lowerLeft.y);
		int y2 = (int) FloatMath
				.floor((obj.bound.lowerLeft.y + obj.bound.height) / cellSize);

		if (x1 == x2 && y1 == y2) {
			if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y2 < cellsPerCol)
				cellIds[0] = y1 * cellsPerRow + x1;
			else
				cellIds[0] = -1;
			cellIds[1] = -1;
			cellIds[2] = -1;
			cellIds[3] = -1;
		} else if (x1 == x2) {
			int i = 0;
			if (x1 >= 0 && x1 < cellsPerRow) {
				if (y1 > 0 && y1 < cellsPerCol)
					cellIds[i++] = y1 * cellsPerRow + x1;
				if (y2 > 0 && y2 < cellsPerCol)
					cellIds[i++] = y2 * cellsPerRow + x1;
				while (i < 4)
					cellIds[i++] = -1;
			}
		} else if (y1 == y2) {
			int i = 0;
			if (y1 >= 0 && y1 < cellsPerCol) {
				if (x1 > 0 && x1 < cellsPerRow)
					cellIds[i++] = y1 * cellsPerRow + x1;
				if (x2 > 0 && x2 < cellsPerRow)
					cellIds[i++] = y1 * cellsPerRow + x2;
				while (i < 4)
					cellIds[i++] = -1;
			}
		} else {
			int i = 0;
			if (x1 >= 0 && x1 < cellsPerRow) {
				if (y1 > 0 && y1 < cellsPerCol)
					cellIds[i++] = y1 * cellsPerRow + x1;
				if (y2 > 0 && y2 < cellsPerCol)
					cellIds[i++] = y2 * cellsPerRow + x1;
			}
			if (x2 >= 0 && x2 < cellsPerRow) {
				if (y1 > 0 && y1 < cellsPerCol)
					cellIds[i++] = y1 * cellsPerRow + x2;
				if (y2 > 0 && y2 < cellsPerCol)
					cellIds[i++] = y2 * cellsPerRow + x2;
			}
			while (i < 4)
				cellIds[i++] = -1;
		}
		return ids;
	}

	public void insertDynamicObject(GPGameObject_Rect obj)
	{
		int[] cellIds = getGridIds(obj);
		int i = 0;
		while (i < 4 && cellIds[i++] != -1)
			dynamicObjects[cellIds[i]].add(obj);
	}

	public void insertStaticObject(GPGameObject_Rect obj)
	{
		int[] cellIds = getGridIds(obj);
		int i = 0;
		while (i < 4 && cellIds[i++] != -1)
			staticObjects[cellIds[i]].add(obj);
	}

	public void removeObject(GPGameObject_Rect obj)
	{
		int[] cellIds = getGridIds(obj);
		int i = 0;
		while (i < 4 && cellIds[i++] != -1) {
			dynamicObjects[cellIds[i]].remove(obj);
			staticObjects[cellIds[i]].remove(obj);
		}
	}

	public void clearDynamicObject()
	{
		int len = dynamicObjects.length;
		for (int i = 0; i < len; i++)
			dynamicObjects[i].clear();
	}

	public List<GPGameObject_Rect> getPotentialCollider(GPGameObject_Rect obj)
	{
		int[] cellIds = getGridIds(obj);
		int i = 0;
		int cellId = -1;
		while (i < 4 && (cellIds[i++] = cellId) != -1) {
			int len = dynamicObjects[cellId].size();
			for (int num = 0; num < len; num++) {
				GPGameObject_Rect collider = dynamicObjects[cellId].get(num);
				if (!foundObject.contains(collider))
					foundObject.add(collider);
			}
			len = staticObjects[cellId].size();
			for (int num = 0; num < len; num++) {
				GPGameObject_Rect collider = staticObjects[cellId].get(num);
				if (!foundObject.contains(collider))
					foundObject.add(collider);
			}
		}
		return foundObject;
	}
	
	
}
