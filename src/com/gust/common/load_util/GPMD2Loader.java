package com.gust.common.load_util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import com.gust.engine.core.GPShader;
import com.gust.system_implement.GPFileManager;

/**
 * MD2�ļ���ȡ������
 * 
 * @author gustplus
 * 
 */
public class GPMD2Loader extends GPModelLoader {

	/**
	 * ��ȡMD2ģ���ļ�
	 * 
	 * @param shader��Ⱦ��
	 * @param fileNameMD2�ļ���
	 * @param mipmap�Ƿ�Ӧ��mipmap����
	 * @param scalesģ������ֵ�������ԭ�㣩
	 * @return MD2Model���ɵ�MD2ģ��
	 */
	public static GPMD2Model load(GPShader shader, String fileName,
			boolean mipmap, float scales) {
		GPMD2Model animation;
		InputStream in;
		try {
			in = GPFileManager.getinstance().readAsset(fileName);
			GPASCIIInputStream dataIn = new GPASCIIInputStream(in);
			/* int MagicName = */dataIn.readInt(); 				// ����ȷ����ȡ���ļ�ΪMD2��ʽ
			/* int version = */dataIn.readInt(); 				// �汾
			int skinWidth = dataIn.readInt(); 					// ������ͼ�Ŀ��
			int skinHeight = dataIn.readInt(); 					// ������ͼ�ĸ߶�
			/* int frameSize = */dataIn.readInt(); 				// ÿһ֡�Ĵ�С��in bytes��
			int numSkins = dataIn.readInt(); 					// ������ͼ����
			int numVertices = dataIn.readInt(); 				// ÿһ֡�Ķ�������
			int numTexCoords = dataIn.readInt(); 				// ÿһ֡����ͼ��������
			int numTriangles = dataIn.readInt(); 				// ÿһ֡������������
			/* int numGlCommands = */dataIn.readInt(); 			// Open GLָ������
			int numFrames = dataIn.readInt(); 					// ��֡��
			int offSkins = dataIn.readInt(); 					// ������ͼ����������ļ���ͷ��ƫ������in bytes��
			int offTexCoords = dataIn.readInt(); 				// ������ͼ����������ļ���ͷ��ƫ������in bytes��
			int offTriangles = dataIn.readInt(); 				// ����������������ļ���ͷ��ƫ������in bytes��
			int offFrames = dataIn.readInt(); 					// ֡����������ļ���ͷ��ƫ������in bytes��
			/* int offGlCommands = */dataIn.readInt(); 			// OpenGLָ������������ļ���ͷ��ƫ������in bytes��
			int fileSize = dataIn.readInt(); 					// �ļ���С��in bytes��

			Log.e("length", "num of vertice = " + numTriangles * 3);
			
			animation = new GPMD2Model(shader, numSkins, mipmap);

			// 68Ϊ�ļ�ͷ��ռ�ֽ���
			byte[] buffer = new byte[fileSize - 68];
			dataIn.read(buffer);

			dataIn = new GPASCIIInputStream(new ByteArrayInputStream(buffer,
					offTriangles - 68, buffer.length - offTriangles));
			short[] verticeIndices = new short[numTriangles * 3];
			short[] texCoordIndices = new short[numTriangles * 3];
			int faceIndex = 0;
			for (int triangles = 0; triangles < numTriangles; triangles++) {
				verticeIndices[faceIndex] = dataIn.readUnsignedShort();
				verticeIndices[faceIndex + 1] = dataIn.readUnsignedShort();
				verticeIndices[faceIndex + 2] = dataIn.readUnsignedShort();

				texCoordIndices[faceIndex] = dataIn.readUnsignedShort();
				texCoordIndices[faceIndex + 1] = dataIn.readUnsignedShort();
				texCoordIndices[faceIndex + 2] = dataIn.readUnsignedShort();
				faceIndex += 3;
			}

			dataIn = new GPASCIIInputStream(new ByteArrayInputStream(buffer,
					offTexCoords - 68, buffer.length - offTexCoords));
			float[] tempTexCoords = new float[numTexCoords * 2];
			int index = 0;
			for (int texCoord = 0; texCoord < numTexCoords; texCoord++) {
				tempTexCoords[index++] = (float) dataIn.readShort() / skinWidth;
				tempTexCoords[index++] = (float) dataIn.readShort()
						/ skinHeight;
			}

			float[] texCoords = new float[numTriangles * 2 * 3];
			int tNum = 0;
			for (int i = 0; i < numTriangles * 3; i++) {
				texCoords[tNum++] = tempTexCoords[2 * texCoordIndices[i]];
				texCoords[tNum++] = tempTexCoords[2 * texCoordIndices[i] + 1];
			}
			animation.setTexCoords(texCoords);

			ByteArrayInputStream bs = new ByteArrayInputStream(buffer,
					offFrames - 68, buffer.length - offFrames);
			dataIn = new GPASCIIInputStream(bs);

			String[] frameNames = new String[numFrames];

			for (int frame = 0; frame < numFrames; frame++) {
				float[] scale = new float[3];
				float[] translation = new float[3];
				scale[0] = dataIn.readFloat();
				scale[1] = dataIn.readFloat();
				scale[2] = dataIn.readFloat();
				translation[0] = dataIn.readFloat();
				translation[1] = dataIn.readFloat();
				translation[2] = dataIn.readFloat();

				frameNames[frame] = dataIn.readString(16);

				float[] tempVertices = new float[numVertices * 3];
				int index2 = 0;
				for (int vertice = 0; vertice < numVertices; vertice++) {
					tempVertices[index2++] = scale[0]
							* dataIn.readUnsignedChar() + translation[0];
					tempVertices[index2++] = scale[1]
							* dataIn.readUnsignedChar() + translation[1];
					tempVertices[index2++] = scale[2]
							* dataIn.readUnsignedChar() + translation[2];
					dataIn.readUnsignedChar();
				}

				float[] vertices = new float[numTriangles * 3 * 3];
				int vNum = 0;
				for (int i = 0; i < numTriangles * 3; i++) {
					vertices[vNum++] = tempVertices[3 * verticeIndices[i]]
							* scales;
					vertices[vNum++] = tempVertices[3 * verticeIndices[i] + 1]
							* scales;
					vertices[vNum++] = tempVertices[3 * verticeIndices[i] + 2]
							* scales;
				}

				animation.addFrame(vertices);
			}
			animation.setActionNames(frameNames);

			String[] texNames = new String[numSkins];
			dataIn = new GPASCIIInputStream(new ByteArrayInputStream(buffer,
					offSkins - 68, buffer.length - offSkins));
			for (int names = 0; names < numSkins; names++) {
				texNames[names] = dataIn.readString(64);
			}
			animation.divideActions();
			return animation;
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("", "load failed!!!!!!!");
			return null;
		}
	}

}
