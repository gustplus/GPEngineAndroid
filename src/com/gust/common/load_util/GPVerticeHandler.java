package com.gust.common.load_util;

import java.util.HashMap;
import java.util.HashSet;

import com.gust.common.math.GPVector3f;

public class GPVerticeHandler {
	protected static float[] smooth(float[] vertices, short[] indices) {
		float[] normals = new float[indices.length * 3];
		HashMap<Integer, HashSet<GPVector3f>> points = new HashMap<Integer, HashSet<GPVector3f>>();
		for (int i = 0; i < indices.length; i++) {
			indices[i]--;
		}

		for (int i = 0; i < indices.length / 3; i += 3) {
			float x0 = vertices[3 * indices[i]];
			float y0 = vertices[3 * indices[i] + 1];
			float z0 = vertices[3 * indices[i] + 2];

			float x1 = vertices[3 * indices[i + 1]];
			float y1 = vertices[3 * indices[i + 1] + 1];
			float z1 = vertices[3 * indices[i + 1] + 2];

			float x2 = vertices[3 * indices[i + 2]];
			float y2 = vertices[3 * indices[i + 2] + 1];
			float z2 = vertices[3 * indices[i + 2] + 2];

			GPVector3f v1 = new GPVector3f(x1 - x0, y1 - y0, z1 - z0);
			GPVector3f v2 = new GPVector3f(x2 - x0, y2 - y0, z2 - z0);
			v1 = v1.crossMul(v2);
			int tempIndices[] = new int[] { indices[i], indices[i + 1],
					indices[i + 2] };
			for (int tempIndex : tempIndices) {
				HashSet<GPVector3f> normal = points.get(tempIndex);
				if (normal == null)
					normal = new HashSet<GPVector3f>();
				normal.add(v1);
				points.put(tempIndex, normal);
			}
		}

		for (int k = 0; k < points.size(); k++) {
			HashSet<GPVector3f> temp = points.get(k);
			GPVector3f v = GPVector3f.getAverageVector(temp);
			normals[3 * k] = -v.x;
			normals[3 * k + 1] = -v.y;
			normals[3 * k + 2] = -v.z;
		}
		return normals;
	}

	public static float[] getNormals(float[] vertices, short[] indices) {
		int len = indices.length;
		int length = vertices.length;
		float[] normals = new float[length];
		int num[] = new int[len];
		for (int x = 0; x < length; ++x) {
			normals[x] = 0;
		}

		for (int i = 0; i < len; i += 3) {
			int v = indices[i];
			GPVector3f v1 = new GPVector3f(vertices[v * 3],
					vertices[v * 3 + 1], vertices[v * 3 + 2]);
			v = indices[i + 1];
			GPVector3f v2 = new GPVector3f(vertices[v * 3],
					vertices[v * 3 + 1], vertices[v * 3 + 2]);
			v = indices[i + 2];
			GPVector3f v3 = new GPVector3f(vertices[v * 3],
					vertices[v * 3 + 1], vertices[v * 3 + 2]);
			v1 = v1.sub(v2);
			v2 = v3.sub(v1);
			v1 = v1.crossMul(v2);
			v = indices[i];
			normals[v * 3] = v1.x;
			normals[v * 3 + 1] += v1.y;
			normals[v * 3 + 2] = v1.z;
			num[v]+=1;
			v = indices[i + 1];
			normals[v * 3] = v1.x;
			normals[v * 3 + 1] += v1.y;
			normals[v * 3 + 2] = v1.z;
			num[v]+=1;
			v = indices[i + 2];
			normals[v * 3] = v1.x;
			normals[v * 3 + 1] += v1.y;
			normals[v * 3 + 2] = v1.z;
			num[v]+=1;
		}
		
		for (int i = 0; i < len; i += 3) {
			int v = indices[i];
			int s = num[v];
			normals[v * 3] /= s;
			normals[v * 3 + 1] /= s;
			normals[v * 3 + 2] /= s;
			v = indices[i + 1];
			s = num[v];
			normals[v * 3] /= s;
			normals[v * 3 + 1] /= s;
			normals[v * 3 + 2] /= s;
			v = indices[i + 2];
			s = num[v];
			normals[v * 3] /= s;
			normals[v * 3 + 1] /= s;
			normals[v * 3 + 2] /= s;
		}
		return normals;
	}

	public static float[] calNormalOnMesh(GPVector3f v1, GPVector3f v2) {
		float[] normal = new float[3];
		v1 = v1.crossMul(v2);
		normal[0] = v1.x;
		normal[1] = v1.y;
		normal[2] = v1.z;
		return normal;
	}

}
