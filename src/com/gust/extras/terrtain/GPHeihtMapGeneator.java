package com.gust.extras.terrtain;

import java.util.Random;

import com.gust.common.math.GPVector2f;

import android.graphics.Bitmap;
import android.util.Log;

public class GPHeihtMapGeneator {
	private static byte[] heights;
	private static byte maxDelta = 50;
	private static byte minDelta = 5;
	private static int rowNum;
	private static int colNum;
	static int n = 0;

	public GPHeihtMapGeneator()
	{
		// TODO Auto-generated constructor stub
	}

	public static void createHightMapWithFaultFormation(int rows, int cols,
			int iteration)
	{
		rowNum = rows;
		colNum = cols;
		heights = new byte[rows * cols];
		int[] bufX = new int[2];
		int[] bufY = new int[2];
		Random rand = new Random();
		byte randomHeight;
		for (int i = 0; i < iteration; i++) {
			randomHeight = (byte) (maxDelta - ((maxDelta - minDelta) * i)
					/ iteration);
			bufX[0] = (int) (rand.nextInt(cols));
			bufY[0] = (int) (rand.nextInt(rows));
			do {
				bufX[1] = (int) (rand.nextInt(cols));
				bufY[1] = (int) (rand.nextInt(rows));
			} while ((bufX[1] == bufX[0]) && (bufY[1] == bufY[0]));
			int dx1 = bufX[1] - bufX[0];
			int dy1 = bufY[1] - bufY[0];
			int dx2,dy2;
			
			if (rand.nextBoolean()) {
				for (int row = 0; row < rows; row++)
					for (int col = 0; col < cols; col++) {
						dx2 = row - bufX[0];
						dy2 = col - bufY[0];
						if ((dx2*dy1-dx1*dy2) >= 0) {
							heights[cols * row + col] += randomHeight;
							if (heights[cols * row + col] > 255)
								heights[cols * row + col] = (byte) 255;
						}
					}
			} else {
				for (int row = 0; row < rows; row++)
					for (int col = 0; col < cols; col++) {
						dx2 = row - bufX[0];
						dy2 = col - bufY[0];
						if ((dx2*dy1-dx1*dy2) <= 0) {
							heights[cols * row + col] += randomHeight;
							if (heights[cols * row + col] > 255)
								heights[cols * row + col] = (byte) 255;
						}
					}
			}
		}

	}

	public static Bitmap getHeightMap()
	{
		Log.e("", "num = " + n);
		Bitmap heightmap = Bitmap.createBitmap(rowNum, colNum,
				Bitmap.Config.ARGB_8888);
		for (int x = 0; x < rowNum; x++)
			for (int y = 0; y < colNum; y++) {
				int height = heights[x * rowNum + y];
				// Log.e("", "height = " + height);
				int color = height + (height << 8) + (height << 16)
						+ (255 << 24);
				heightmap.setPixel(x, y, color);
			}
		return heightmap;
	}

	public static void setDelta(byte bMaxDelta, byte bMinDelta)
	{
		maxDelta = bMaxDelta;
		minDelta = bMinDelta;
	}

	public static void createHightMapWithMidpointDisplacement(int rows,
			int cols, float roughness)
	{

		if (rowNum != colNum/* ||rowNum%2 != 1 */) {
			Log.e("terrain datas error!",
					"the number of rows and cols must be the same and be 2^n + 1!");
			return;
		}
		rowNum = rows;
		colNum = cols;
		heights = new byte[rows * cols];
		float rough = (float) Math.pow(2, -1 * roughness);
		Random random = new Random();
		heights[0] = (byte) Math.abs((random.nextFloat() * 128));
		heights[colNum * rowNum - 1] = (byte) Math
				.abs((random.nextFloat() * 128));
		heights[colNum - 1] = (byte) Math.abs((random.nextFloat() * 255));
		heights[colNum * (rowNum - 1)] = (byte) Math
				.abs((random.nextFloat() * 128));
		// Log.e("", " 0   ="+ heights[0]);
		// Log.e("", " 1   ="+ heights[colNum*rowNum-1]);
		// Log.e("", " 2   ="+ heights[colNum-1]);
		// Log.e("", " 3   ="+ heights[colNum*(rowNum-1)]);
		getMidpoint(new GPVector2f(0, 0), new GPVector2f(0, colNum - 1),
				new GPVector2f(rowNum - 1, 0), new GPVector2f(rowNum - 1,
						colNum - 1), 255, rough);
	}

	/**
	 * 0┏━━┓1 
	 *  ┃       ┃
	 * 2┗━━┛3
	 * 
	 * @param points
	 * @param curSideLen
	 */

	private static void getMidpoint(GPVector2f point0, GPVector2f point1,
			GPVector2f point2, GPVector2f point3, float curSideLength,
			float roughness)
	{
		n++;
		Random rand = new Random();
		if (curSideLength < 2)
			return;
		GPVector2f left = point0.add(point2).mul(0.5f);
		GPVector2f right = point1.add(point3).mul(0.5f);
		GPVector2f up = point0.add(point1).mul(0.5f);
		GPVector2f bottom = point2.add(point3).mul(0.5f);
		GPVector2f mid = left.add(right).mul(0.5f);
		byte changeHeight = (byte) (curSideLength / 2);

		int a = (int) (point0.x * rowNum + point0.y);
		int b = (int) (point1.x * rowNum + point1.y);
		int c = (int) (point2.x * rowNum + point2.y);
		int d = (int) (point3.x * rowNum + point3.y);
		int midIndex = (a + b + c + d) / 4;

		byte baseHeight = (byte) ((heights[a] + heights[b] + heights[c] + heights[d]) / 4);
		int index = midIndex;
		heights[index] = (byte) (baseHeight + (rand.nextInt(changeHeight))
				* (rand.nextBoolean() == true ? 1 : -1));

		baseHeight = (byte) ((heights[a] + heights[b] + heights[midIndex]) / 3);
		index = (a + b) / 2;
		heights[index] = (byte) (baseHeight + (rand.nextInt(changeHeight))
				* (rand.nextBoolean() == true ? 1 : -1));

		baseHeight = (byte) ((heights[c] + heights[d] + heights[midIndex]) / 3);
		index = (c + d) / 2;
		heights[index] = (byte) (baseHeight + (rand.nextInt(changeHeight))
				* (rand.nextBoolean() == true ? 1 : -1));

		baseHeight = (byte) ((heights[a] + heights[c] + heights[midIndex]) / 3);
		index = (a + c) / 2;
		heights[index] = (byte) (baseHeight + (rand.nextInt(changeHeight))
				* (rand.nextBoolean() == true ? 1 : -1));

		baseHeight = (byte) ((heights[b] + heights[d] + heights[midIndex]) / 3);
		index = (b + d) / 2;
		heights[index] = (byte) (baseHeight + (rand.nextInt(changeHeight))
				* (rand.nextBoolean() == true ? 1 : -1));

		getMidpoint(point0, up, left, mid, changeHeight, roughness);
		getMidpoint(up, point1, mid, right, changeHeight, roughness);
		getMidpoint(left, mid, point2, bottom, changeHeight, roughness);
		getMidpoint(mid, right, bottom, point3, changeHeight, roughness);
	}

	/**
	 * 柔化地形
	 * 
	 * @param filter柔化程度
	 *            （0.0f为不柔化）
	 */
	public static void softTerrain(float filter)
	{
		// 从上到下、从左到右逐行柔化
		for (int i = 0; i < rowNum; i++)
			soft(heights, i * colNum, 1, colNum, filter);
		// 从上到下、从右到左逐行柔化
		for (int i = 0; i < rowNum; i++)
			soft(heights, (i + 1) * colNum - 1, -1, colNum, filter);
		// 从左到右、从上到下逐列柔化
		for (int j = 0; j < colNum; j++)
			soft(heights, j, colNum, rowNum, filter);
		// 从右到左、从上到下逐行柔化
		for (int j = 0; j < colNum; j++)
			soft(heights, colNum * ( rowNum - 1) + j, -colNum, rowNum, filter);
		
//		for( i=0; i<m_iSize; i++ )
//			FilterHeightBand( &fpHeightData[m_iSize*(m_iSize-1)+i], -m_iSize, m_iSize, fFilter );
	}

	/**
	 * 柔化
	 * 
	 * @param heights高度参数数组
	 * @param offset起始数据偏移量
	 * @param stride两数据间的间隔
	 * @param length数据长度
	 * @param filter柔化程度
	 *            （0.0f为不柔化）
	 */
	private static void soft(byte[] heights, int offset, int stride,
			int length, float filter)
	{
		byte height = heights[offset];
		int j = offset + stride;
		for (int i = 0; i < length - 1; i++) {
			heights[j] = (byte) (filter * height + (1 - filter) * heights[j]);
			height = heights[j];
			j += stride;
		}
	}
	
}
