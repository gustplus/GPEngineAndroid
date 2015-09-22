package com.gust.common.load_util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.gust.common.math.GPVector3f;
import com.gust.engine.core.GPShader;
import com.gust.scene3D.GPCoordBuffer2D;
import com.gust.scene3D.GPDisplayObject3D;
import com.gust.scene3D.GPVertexBuffer3D;
import com.gust.system_implement.GPFileManager;

/**
 * 用于读取.OBJ格式的模型文件的帮助类
 * 
 * @author gustplus
 * 
 */
public class GPObjLoader extends GPModelLoader {
	/**
	 * 读取OBJ格式的静态模型
	 * 
	 * @param programNum渲染器编号
	 * @param fileNameOBJ文件名
	 * @return Vertices3_20 静态模型
	 */
	public static GPDisplayObject3D load(GPShader shader, String fileName) {
		GPDisplayObject3D model = null;
		InputStream in = null;
		try {
			in = GPFileManager.getinstance().readAsset(fileName);
			List<String> lines = readLines(in);

			float[] vertices = new float[lines.size() * 3];
			float[] normals = new float[lines.size() * 3];
			float[] uv = new float[lines.size() * 2];

			int[] faceVertices = new int[lines.size() * 3];
			int[] faceNormals = new int[lines.size() * 3];
			int[] faceUVs = new int[lines.size() * 3];

			int numVertices = 0;
			int numNormals = 0;
			int numUV = 0;
			int numFaces = 0;

			int verticeIndex = 0;
			int normalIndex = 0;
			int uvIndex = 0;
			int faceIndex = 0;

			int size = lines.size();
			for (int i = 0; i < size; i++) {
				String line = lines.get(i);
				String[] temp;
				if (line.startsWith("v ")) {
					temp = line.split("[ ]+");
					vertices[verticeIndex] = Float.parseFloat(temp[1]);
					vertices[verticeIndex + 1] = Float.parseFloat(temp[2]);
					vertices[verticeIndex + 2] = Float.parseFloat(temp[3]);
					verticeIndex += 3;
					numVertices++;
					continue;
				}

				if (line.startsWith("vn ")) {
					temp = line.split("[ ]+");
					normals[normalIndex] = Float.parseFloat(temp[1]);
					normals[normalIndex + 1] = Float.parseFloat(temp[2]);
					normals[normalIndex + 2] = Float.parseFloat(temp[3]);
					normalIndex += 3;
					numNormals++;
					continue;
				}

				if (line.startsWith("vt ")) {
					temp = line.split("[ ]+");
					uv[uvIndex] = Float.parseFloat(temp[1]);
					uv[uvIndex + 1] = Float.parseFloat(temp[2]);
					uvIndex += 2;
					numUV++;
					continue;
				}

				if (line.startsWith("f ")) {
					temp = line.split("[ ]+");

					String[] verticeDatas = temp[1].split("/");
					faceVertices[faceIndex] = getRealIndex(verticeDatas[0],
							numVertices);
					if (verticeDatas.length > 2)
						faceNormals[faceIndex] = getRealIndex(verticeDatas[2],
								numNormals);
					if (verticeDatas.length > 1)
						faceUVs[faceIndex] = getRealIndex(verticeDatas[1],
								numUV);
					faceIndex++;

					verticeDatas = temp[2].split("/");
					faceVertices[faceIndex] = getRealIndex(verticeDatas[0],
							numVertices);
					if (verticeDatas.length > 2)
						faceNormals[faceIndex] = getRealIndex(verticeDatas[2],
								numNormals);
					if (verticeDatas.length > 1)
						faceUVs[faceIndex] = getRealIndex(verticeDatas[1],
								numUV);
					faceIndex++;

					verticeDatas = temp[3].split("/");
					faceVertices[faceIndex] = getRealIndex(verticeDatas[0],
							numVertices);
					if (verticeDatas.length > 2)
						faceNormals[faceIndex] = getRealIndex(verticeDatas[2],
								numNormals);
					if (verticeDatas.length > 1)
						faceUVs[faceIndex] = getRealIndex(verticeDatas[1],
								numUV);
					faceIndex++;

					numFaces++;
					continue;
				}
			}

			float[] allVertices = new float[numFaces * 3 * 3];
			float[] allNormals = new float[numFaces * 3 * 3];
			float[] allUVs = new float[numFaces * 2 * 3];

			int allVerIndex = 0;
			int allNorIndex = 0;
			int allUVIndex = 0;

			for (int j = 0; j < numFaces * 3; j++) {
				int tervIndex = faceVertices[j] * 3;
				allVertices[allVerIndex++] = vertices[tervIndex];
				allVertices[allVerIndex++] = vertices[tervIndex + 1];
				allVertices[allVerIndex++] = vertices[tervIndex + 2];

				if (numUV > 0) {
					int texIndex = faceUVs[j] * 2;
					allUVs[allUVIndex++] = uv[texIndex];
					allUVs[allUVIndex++] = 1 - uv[texIndex + 1];
				}

				if (numNormals > 0) {
					int norIndex = faceNormals[j] * 3;
					allNormals[allNorIndex++] = normals[norIndex];
					allNormals[allNorIndex++] = normals[norIndex + 1];
					allNormals[allNorIndex++] = normals[norIndex + 2];
				}
			}

			GPVertexBuffer3D verticeBuffer = new GPVertexBuffer3D(shader);
			verticeBuffer.setVertices(allVertices, 0, allVertices.length);
			model = new GPDisplayObject3D(verticeBuffer);
			if (allNorIndex > 0)
				verticeBuffer.setNormals(allNormals, 0, allNormals.length);
			if (allUVIndex > 0) {
				GPCoordBuffer2D texcoorBuffer = new GPCoordBuffer2D(false, 1, 0);
				texcoorBuffer.setTexCoords(allUVs, 0, allUVs.length);
				model.addCoordBuffer(texcoorBuffer);
				// model.setTexCoords(allUVs, 0, allUVs.length);
			}
			return model;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return model;
	}

	/**
	 * 读取OBJ格式的文件的静态模型，并细化（未改变模型，仅对光照有效）
	 * 
	 * @param programNum渲染器编号
	 * @param fileNameOBJ文件名
	 * @return Vertices3_20 静态模型
	 */
	public static GPDisplayObject3D loadWithSmooth(GPShader shader,
			String fileName) {
		GPDisplayObject3D model = null;

		InputStream in = null;

		HashMap<Integer, HashSet<GPVector3f>> points = new HashMap<Integer, HashSet<GPVector3f>>();
		try {
			in = GPFileManager.getinstance().readAsset(fileName);
			List<String> lines = readLines(in);

			float[] vertices = new float[lines.size() * 3];
			float[] uv = new float[lines.size() * 2];

			int[] faceVertices = new int[lines.size() * 3];// 顶点索引
			int[] faceUVs = new int[lines.size() * 3];

			int numVertices = 0;
			int numUV = 0;
			int numFaces = 0;

			int verticeIndex = 0;
			int uvIndex = 0;
			int faceIndex = 0;

			int size = lines.size();
			for (int i = 0; i < size; i++) {
				String line = lines.get(i);

				if (line.startsWith("v ")) {
					String[] temp = line.split("[ ]+");
					vertices[verticeIndex] = Float.parseFloat(temp[1]);
					vertices[verticeIndex + 1] = Float.parseFloat(temp[2]);
					vertices[verticeIndex + 2] = Float.parseFloat(temp[3]);
					verticeIndex += 3;
					numVertices++;
					continue;
				}

				if (line.startsWith("vt ")) {
					String[] temp = line.split("[ ]+");
					uv[uvIndex] = Float.parseFloat(temp[1]);
					uv[uvIndex + 1] = Float.parseFloat(temp[2]);
					uvIndex += 2;
					numUV++;
					continue;
				}

				if (line.startsWith("f ")) {
					String[] temp = line.split("[ ]+");

					int[] tempIndices = new int[3];

					String[] verticeDatas = temp[1].split("/");
					tempIndices[0] = faceVertices[faceIndex] = getRealIndex(
							verticeDatas[0], numVertices);
					if (verticeDatas.length > 1)
						faceUVs[faceIndex] = getRealIndex(verticeDatas[1],
								numUV);

					float x0 = vertices[3 * tempIndices[0]];
					float y0 = vertices[3 * tempIndices[0] + 1];
					float z0 = vertices[3 * tempIndices[0] + 2];

					faceIndex++;

					verticeDatas = temp[2].split("/");
					tempIndices[1] = faceVertices[faceIndex] = getRealIndex(
							verticeDatas[0], numVertices);
					if (verticeDatas.length > 1)
						faceUVs[faceIndex] = getRealIndex(verticeDatas[1],
								numUV);

					float x1 = vertices[3 * tempIndices[1]];
					float y1 = vertices[3 * tempIndices[1] + 1];
					float z1 = vertices[3 * tempIndices[1] + 2];

					faceIndex++;

					verticeDatas = temp[3].split("/");
					tempIndices[2] = faceVertices[faceIndex] = getRealIndex(
							verticeDatas[0], numVertices);
					if (verticeDatas.length > 1)
						faceUVs[faceIndex] = getRealIndex(verticeDatas[1],
								numUV);

					float x2 = vertices[3 * tempIndices[2]];
					float y2 = vertices[3 * tempIndices[2] + 1];
					float z2 = vertices[3 * tempIndices[2] + 2];

					faceIndex++;

					GPVector3f v1 = new GPVector3f(x1 - x0, y1 - y0, z1 - z0);
					GPVector3f v2 = new GPVector3f(x2 - x0, y2 - y0, z2 - z0);
					v1 = v1.crossMul(v2);

					for (int tempIndex : tempIndices) {
						HashSet<GPVector3f> normal = points.get(tempIndex);
						if (normal == null)
							normal = new HashSet<GPVector3f>();
						normal.add(v1);
						points.put(tempIndex, normal);
					}

					numFaces++;
					continue;
				}
			}

			for (int k = 0; k < points.size(); k++) {
				HashSet<GPVector3f> temp = points.get(k);
				if (temp != null) {
					GPVector3f v = GPVector3f.getAverageVector(temp);
					temp.clear();
					temp.add(v);
					points.put(k, temp);
				}
			}

			float[] allVertices = new float[numFaces * 3 * 3];
			float[] allNormals = new float[numFaces * 3 * 3];
			float[] allUVs = new float[numFaces * 2 * 3];

			int allVerIndex = 0;
			int allNorIndex = 0;
			int allUVIndex = 0;

			for (int j = 0; j < numFaces * 3; j++) {
				int tervIndex = faceVertices[j] * 3;
				allVertices[allVerIndex++] = vertices[tervIndex];
				allVertices[allVerIndex++] = vertices[tervIndex + 1];
				allVertices[allVerIndex++] = vertices[tervIndex + 2];

				if (numUV > 0) {
					int texIndex = faceUVs[j] * 2;
					allUVs[allUVIndex++] = uv[texIndex];
					allUVs[allUVIndex++] = 1 - uv[texIndex + 1];
				}

				int norIndex = faceVertices[j];
				HashSet<GPVector3f> temp = points.get(norIndex);
				GPVector3f v = new GPVector3f();
				for (GPVector3f v0 : temp)
					v = v0;
				allNormals[allNorIndex++] = -v.x;
				allNormals[allNorIndex++] = -v.y;
				allNormals[allNorIndex++] = -v.z;
			}

			GPVertexBuffer3D verticeBuffer = new GPVertexBuffer3D(shader);
			verticeBuffer.setVertices(allVertices, 0, allVertices.length);
			verticeBuffer.setNormals(allNormals, 0, allNormals.length);
			model = new GPDisplayObject3D(verticeBuffer);
			if (allUVIndex > 0) {
				GPCoordBuffer2D texcoorBuffer = new GPCoordBuffer2D(false, 1, 0);
				texcoorBuffer.setTexCoords(allUVs, 0, allUVs.length);
				model.addCoordBuffer(texcoorBuffer);
				// model.setTexCoords(allUVs, 0, allUVs.length);
			}
			return model;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return model;
	}

	/**
	 * 读取OBJ格式的文件的静态模型（包含法线）
	 * 
	 * @param programNum渲染器编号
	 * @param fileNameOBJ文件名
	 * @return Vertices3_20 静态模型
	 */
	public static GPDisplayObject3D loadWithNormals(GPShader shader,
			String fileName) {
		GPDisplayObject3D model = null;

		InputStream in = null;

		try {
			in = GPFileManager.getinstance().readAsset(fileName);
			List<String> lines = readLines(in);

			float[] vertices = new float[lines.size() * 3];
			float[] uv = new float[lines.size() * 2];

			int[] faceVertices = new int[lines.size() * 3];

			int numVertices = 0;
			int numUV = 0;
			int numFaces = 0;

			int verticeIndex = 0;
			int uvIndex = 0;
			int faceIndex = 0;

			ArrayList<Float> allVertices = new ArrayList<Float>();
			ArrayList<Float> allUVs = new ArrayList<Float>();
			ArrayList<Float> allNormals = new ArrayList<Float>();

			int size = lines.size();
			for (int i = 0; i < size; i++) {
				String line = lines.get(i);

				if (line.startsWith("v ")) {
					String[] temp = line.split("[ ]+");
					vertices[verticeIndex] = Float.parseFloat(temp[1]);
					vertices[verticeIndex + 1] = Float.parseFloat(temp[2]);
					vertices[verticeIndex + 2] = Float.parseFloat(temp[3]);
					verticeIndex += 3;
					numVertices++;
					continue;
				}

				if (line.startsWith("vt ")) {
					String[] temp = line.split("[ ]+");
					uv[uvIndex] = Float.parseFloat(temp[1]);
					uv[uvIndex + 1] = Float.parseFloat(temp[2]);
					uvIndex += 2;
					numUV++;
					continue;
				}

				if (line.startsWith("f ")) {
					String[] temp = line.split("[ ]+");

					int[] tempIndices = new int[3];

					String[] verticeDatas = temp[1].split("/");
					tempIndices[0] = getRealIndex(verticeDatas[0], numVertices);
					if (verticeDatas.length > 1) {
						int uvTempIndex = getRealIndex(verticeDatas[1], numUV);
						allUVs.add(uv[2 * uvTempIndex]);
						allUVs.add(uv[2 * uvTempIndex + 1]);
					}
					float x0 = vertices[3 * tempIndices[0]];
					float y0 = vertices[3 * tempIndices[0] + 1];
					float z0 = vertices[3 * tempIndices[0] + 2];

					allVertices.add(x0);
					allVertices.add(y0);
					allVertices.add(z0);

					faceIndex++;

					verticeDatas = temp[2].split("/");
					tempIndices[1] = faceVertices[faceIndex] = getRealIndex(
							verticeDatas[0], numVertices);
					if (verticeDatas.length > 1) {
						int uvTempIndex = getRealIndex(verticeDatas[1], numUV);
						allUVs.add(uv[2 * uvTempIndex]);
						allUVs.add(uv[2 * uvTempIndex + 1]);
					}
					float x1 = vertices[3 * tempIndices[1]];
					float y1 = vertices[3 * tempIndices[1] + 1];
					float z1 = vertices[3 * tempIndices[1] + 2];
					allVertices.add(x1);
					allVertices.add(y1);
					allVertices.add(z1);

					faceIndex++;

					verticeDatas = temp[3].split("/");
					tempIndices[2] = faceVertices[faceIndex] = getRealIndex(
							verticeDatas[0], numVertices);
					if (verticeDatas.length > 1) {
						int uvTempIndex = getRealIndex(verticeDatas[1], numUV);
						allUVs.add(uv[2 * uvTempIndex]);
						allUVs.add(uv[2 * uvTempIndex + 1]);
					}

					float x2 = vertices[3 * tempIndices[2]];
					float y2 = vertices[3 * tempIndices[2] + 1];
					float z2 = vertices[3 * tempIndices[2] + 2];
					allVertices.add(x2);
					allVertices.add(y2);
					allVertices.add(z2);

					faceIndex++;

					GPVector3f v1 = new GPVector3f(x0 - x1, y0 - y1, z0 - z1);
					GPVector3f v2 = new GPVector3f(x2 - x0, y2 - y0, z2 - z0);
					v1 = v1.crossMul(v2);
					allNormals.add(v1.x);
					allNormals.add(v1.y);
					allNormals.add(v1.z);
					allNormals.add(v1.x);
					allNormals.add(v1.y);
					allNormals.add(v1.z);
					allNormals.add(v1.x);
					allNormals.add(v1.y);
					allNormals.add(v1.z);
					numFaces++;
					continue;
				}
			}

			float[] totalVertices = new float[numFaces * 3 * 3];
			float[] totalUVs = new float[numFaces * 2 * 3];
			float[] totalNormals = new float[numFaces * 3 * 3];

			for (int i = 0; i < allVertices.size(); i++)
				totalVertices[i] = allVertices.get(i);
			for (int i = 0; i < allUVs.size(); i++)
				totalUVs[i] = 1 - allUVs.get(i);
			for (int i = 0; i < allNormals.size(); i++)
				totalNormals[i] = allNormals.get(i);

			GPVertexBuffer3D verticeBuffer = new GPVertexBuffer3D(shader);
			verticeBuffer.setVertices(totalVertices, 0, totalVertices.length);
			verticeBuffer.setNormals(totalNormals, 0, totalNormals.length);
			model = new GPDisplayObject3D(verticeBuffer);
			if (uvIndex > 0) {
				GPCoordBuffer2D texcoorBuffer = new GPCoordBuffer2D(false, 1, 0);
				texcoorBuffer.setTexCoords(totalUVs, 0, totalUVs.length);
				model.addCoordBuffer(texcoorBuffer);
				// model.setTexCoords(allUVs, 0, allUVs.length);
			}
			return model;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return model;
	}

	private static int getRealIndex(String index, int size) {
		int realIndex = Integer.parseInt(index);
		if (realIndex < 0)
			return realIndex + size;
		else
			return realIndex - 1;
	}

	private static List<String> readLines(InputStream in) throws IOException {
		List<String> lines = new ArrayList<String>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		return lines;
	}

}
