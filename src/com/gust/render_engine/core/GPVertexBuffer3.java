package com.gust.render_engine.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.gust.common.game_util.GPLogger;
import com.gust.common.math.GPConstants;
import com.gust.engine.core.GPShader;
import com.gust.engine.core.GPShaderManager;
import com.gust.physics3D.GPAABB;
import com.gust.render_engine.base.MatrixHelper;

import android.opengl.GLES20;

/**
 * 存放顶点相关信息的类
 * 
 * @author gustplus
 * 
 */
public class GPVertexBuffer3 {
	private boolean hasColor;
	private boolean hasTexCoords;
	private boolean hasNormals;
	private IntBuffer vertices;
	private IntBuffer colors;
	private IntBuffer texCoords;
	private IntBuffer normals;
	private ShortBuffer indices;

	private int verticesLength = 0;
	
	private int verticesSize = 0;

	private int vetricesHandler;
	private int colorHandler;
	private int texCoorHandler;
	private int normalsHandler;
	private int alphaHandler;
	private int functionIDHandler;
	private int texHandler;
	private int destinationColorHandler;
	private int changeTimeHandler;

	private int indicesLength;
	private GPShader shader;

	public GPAABB AABB;

	private float alpha;
	private int functionID;

	private float[] color;
	private float changeTime;

	private GPTexture2D texture;

	private int verticeBuffer = -1;
	private int colorBuffer = -1;
	private int texCoordBuffer = -1;
	private int normalBuffer = -1;
	private int indiceBuffer = -1;

	private int drawType;

	private boolean infoChange;

	/**
	 * 
	 * @param programNum渲染器编号
	 * @param willChange顶点数据是否可能改变
	 */
	public GPVertexBuffer3(GPShader shader) {
		this.alpha = 1f;
		this.functionID = 0;
		this.shader = shader;
		this.texHandler = shader.getUniformLocation("fTexture");

		indices = null;
		AABB = new GPAABB();

		alphaHandler = shader.getUniformLocation("alpha");
		functionIDHandler = shader.getUniformLocation("functionID");

		changeTimeHandler = shader.getUniformLocation("oneOfchangeTime");
		destinationColorHandler = shader.getUniformLocation("destinationColor");

		alpha = 1;
		functionID = 0;

		drawType = GLES20.GL_DYNAMIC_DRAW;
	}

	public GPVertexBuffer3(GPShader shader, boolean change) {
		this.alpha = 1f;
		this.functionID = 0;
		this.shader = shader;
		this.texHandler = shader.getUniformLocation("fTexture");

		indices = null;
		AABB = new GPAABB();

		alphaHandler = shader.getUniformLocation("alpha");
		functionIDHandler = shader.getUniformLocation("functionID");

		changeTimeHandler = shader.getUniformLocation("oneOfchangeTime");
		destinationColorHandler = shader.getUniformLocation("destinationColor");

		alpha = 1;
		functionID = 0;

		if (change) {
			drawType = GLES20.GL_DYNAMIC_DRAW;
		} else {
			drawType = GLES20.GL_STATIC_DRAW;
		}
	}

	public void setTexture(GPTexture2D texture) {
		this.texture = texture;
	}

	public GPShader getShader() {
		return shader;
	}

	/**
	 * 设置顶点信息
	 * 
	 * @param vertices顶点数组
	 * @param offset偏移量
	 * @param length使用的长度
	 */
	public void setVertices(float[] vertices, int offset, int length) {
		if (verticesLength < length || this.vertices == null) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(4 * length);
			buffer.order(ByteOrder.nativeOrder());
			this.vertices = buffer.asIntBuffer();
			infoChange = true;
			verticesLength = length;
		}

		int[] vertices_int = new int[length];

		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++) {
			vertices_int[j] = Float.floatToRawIntBits(vertices[i]);
		}
		this.vertices.clear();
		this.vertices.position(0);
		this.vertices.put(vertices_int, 0, length);
		this.vertices.flip();
		vetricesHandler = shader
				.getAttribLocation(GPShaderManager.VERTICES_NAME);
		verticesSize = length;
		
		
		this.vertices.position(0);
		if (infoChange) {
			int[] verticeBuffer = new int[]{this.verticeBuffer};
			if (this.verticeBuffer != -1) {
				GLES20.glDeleteBuffers(1, verticeBuffer, 0);
			}
			GLES20.glGenBuffers(1, verticeBuffer, 0);
			this.verticeBuffer = verticeBuffer[0];
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.verticeBuffer);
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, 4 * length,
					this.vertices, drawType);
		} else {
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.verticeBuffer);
			GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, length * 4,
					this.vertices);
		}
	}

	public void setInfoChange(boolean infoChange) {
		this.infoChange = infoChange;
	}

	/**
	 * 设置索引信息
	 * 
	 * @param indices索引数组
	 * @param offset偏移量
	 * @param length使用的长度
	 */
	public void setIndices(short[] indices, int offset, int length) {
		if (infoChange || this.indices == null) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(length * Short.SIZE
					/ 8);
			buffer.order(ByteOrder.nativeOrder());
			this.indices = buffer.asShortBuffer();
			infoChange = true;
		}

		this.indices.clear();
		this.indices.position(0);
		this.indices.put(indices, offset, length);
		this.indices.flip();

		this.indices.position(0);
		if (this.indicesLength < length) {
			int[] indiceBuffer = new int[]{this.indiceBuffer};
			if (this.indiceBuffer != -1) {
				GLES20.glDeleteBuffers(1, indiceBuffer, 0);
			}
			GLES20.glGenBuffers(1, indiceBuffer, 0);
			this.indiceBuffer = indiceBuffer[0];
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
					this.indiceBuffer);
			GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, 2 * length,
					this.indices, drawType);
		} else {
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.indiceBuffer);
			GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, length * 2,
					this.indices);
		}

		this.indicesLength = length;
	}

	/**
	 * 设置顶点颜色数组
	 * 
	 * @param colors顶点颜色数组
	 * @param offset偏移量
	 * @param length使用的长度
	 */
	public void setColors(float[] colors, int offset, int length) {
		hasColor = true;
		if (infoChange || this.colors == null) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(colors.length * 4);
			buffer.order(ByteOrder.nativeOrder());
			this.colors = buffer.asIntBuffer();
			infoChange = true;
		}
		int[] colorbuffer = new int[colors.length];
		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++) {
			colorbuffer[j] = Float.floatToRawIntBits(colors[i]);
		}
		this.colors.clear();
		this.colors.position(0);
		this.colors.put(colorbuffer, 0, length);
		this.colors.flip();
		colorHandler = shader.getAttribLocation(GPShaderManager.COLOR_NAME);

		this.colors.position(0);

		if (infoChange) {
			int[] colorBuffer = new int[]{this.colorBuffer};
			if (this.colorBuffer != -1) {
				GLES20.glDeleteBuffers(1, colorBuffer, 0);
			}
			GLES20.glGenBuffers(1, colorBuffer, 0);
			this.colorBuffer = colorBuffer[0];
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.colorBuffer);
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, 4 * length,
					this.colors, drawType);
		} else {
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.colorBuffer);
			GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, length * 4,
					this.colors);
		}
	}

	/**
	 * 设置顶点纹理坐标信息
	 * 
	 * @param texCoords纹理坐标数组
	 * @param offset偏移量
	 * @param length使用的长度
	 */
	public void setTexCoords(float[] texCoords, int offset, int length) {
		hasTexCoords = true;
		if (infoChange || this.texCoords == null) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(texCoords.length * 4);
			buffer.order(ByteOrder.nativeOrder());
			this.texCoords = buffer.asIntBuffer();
			infoChange = true;
		}

		int[] coords_int = new int[texCoords.length];
		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++) {
			coords_int[j] = Float.floatToRawIntBits(texCoords[i]);
		}
		this.texCoords.clear();
		this.texCoords.position(0);
		this.texCoords.put(coords_int, 0, length);
		this.texCoords.flip();
		texCoorHandler = shader
				.getAttribLocation(GPShaderManager.TEXTURE_COORD_NAME);

		this.texCoords.position(0);
		if (infoChange) {
			int[] texCoordBuffer = new int[]{this.texCoordBuffer};
			if (this.texCoordBuffer != -1) {
				GLES20.glDeleteBuffers(1, texCoordBuffer, 0);
			}
			GLES20.glGenBuffers(1, texCoordBuffer, 0);
			this.texCoordBuffer = texCoordBuffer[0];
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.texCoordBuffer);
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, 4 * length,
					this.texCoords, drawType);
		} else {
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.texCoordBuffer);
			GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, length * 4,
					this.texCoords);
		}
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
		if (alpha > GPConstants.almostZero) {
			this.functionID = 1;
		}
		if (this.alpha >= 1) {
			this.functionID = 0;
		}
	}

	public void changeToColor(float[] color, float time) {
		this.color = color;
		this.changeTime = time;
		this.functionID = 2;
	}

	public void setFunctionID(int functionID) {
		this.functionID = functionID;
	}

	/**
	 * 设置顶点法线坐标信息
	 * 
	 * @param normals顶点法线数组
	 * @param offset偏移量
	 * @param length使用的长度
	 */
	public void setNormals(float[] normals, int offset, int length) {
		hasNormals = true;
		if (infoChange || this.normals == null) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(normals.length * 4);
			buffer.order(ByteOrder.nativeOrder());
			this.normals = buffer.asIntBuffer();
			infoChange = true;
		}

		int[] normal_int = new int[normals.length];
		int len = offset + length;
		for (int i = offset, j = 0; i < len; i++, j++) {
			normal_int[j] = Float.floatToRawIntBits(normals[i]);
		}
		this.normals.clear();
		this.normals.position(0);
		this.normals.put(normal_int, 0, length);
		this.normals.flip();
		normalsHandler = shader.getAttribLocation(GPShaderManager.NORMAL_NAME);

		this.normals.position(0);
		if (infoChange) {
			int[] normalBuffer = new int[]{this.normalBuffer};
			if (this.normalBuffer != -1) {
				GLES20.glDeleteBuffers(1, normalBuffer, 0);
			}
			GLES20.glGenBuffers(1, normalBuffer, 0);
			this.normalBuffer = normalBuffer[0];
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.normalBuffer);
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, 4 * length,
					this.normals, drawType);
		} else {
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.normalBuffer);
			GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, length * 4,
					this.normals);
		}
	}

	public float[] getVertices() {
		if (vertices == null || verticesLength == 0) {
			return null;
		}
		this.vertices.position(0);
		float[] vertices = new float[verticesLength];
		int[] verticesInt = new int[verticesLength];
		this.vertices.get(verticesInt);
		for (int i = 0; i < verticesLength; ++i) {
			vertices[i] = Float.intBitsToFloat(verticesInt[i]);
		}
		return vertices;
	}

	public float[] getTexCoords() {
		if (texCoords == null || verticesLength == 0) {
			return null;
		}
		int len = verticesLength / 3 * 2;
		float[] texCoords = new float[len];
		int[] texCoordsInt = new int[len];
		this.texCoords.position(0);
		this.texCoords.get(texCoordsInt);
		for (int i = 0; i < len; ++i) {
			texCoords[i] = Float.intBitsToFloat(texCoordsInt[i]);
		}
		return texCoords;
	}
	
	public void reloadVBO(){
		this.vertices.position(0);
		int[] verticeBuffer = new int[]{this.verticeBuffer};
		GLES20.glGenBuffers(1, verticeBuffer, 0);
		this.verticeBuffer = verticeBuffer[0];
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.verticeBuffer);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, 4 * this.verticesSize,
				this.vertices, drawType);
		
		if (hasColor) {
			this.colors.position(0);
			int[] colorBuffer = new int[]{this.colorBuffer};
			GLES20.glGenBuffers(1, colorBuffer, 0);
			this.colorBuffer = colorBuffer[0];
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.colorBuffer);
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, 4 * this.verticesSize,
					this.colors, drawType);
		}

		if (hasTexCoords) {
			this.texCoords.position(0);
			int[] texCoordBuffer = new int[]{this.texCoordBuffer};
			GLES20.glGenBuffers(1, texCoordBuffer, 0);
			this.texCoordBuffer = texCoordBuffer[0];
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.texCoordBuffer);
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, 4 * this.verticesSize * 2 / 3,
					this.texCoords, drawType);
		}

		if (hasNormals) {
			this.normals.position(0);
			int[] normalBuffer = new int[]{this.normalBuffer};
			GLES20.glGenBuffers(1, normalBuffer, 0);
			this.normalBuffer = normalBuffer[0];
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.normalBuffer);
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, 4 * this.verticesSize,
					this.normals, drawType);
		}
		
		if(indices != null){
			this.indices.position(0);
			int[] indiceBuffer = new int[]{this.indiceBuffer};
			GLES20.glGenBuffers(1, indiceBuffer, 0);
			this.indiceBuffer = indiceBuffer[0];
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
					this.indiceBuffer);
			GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, verticesSize,
					this.indices, drawType);
		}
	}

	/**
	 * 绑定顶点信息供渲染
	 */
	public void bind() {
		shader.bind();
		if (verticesLength > 0) {
			// GLES20.glVertexAttribPointer(vetricesHandler, 3, GLES20.GL_FLOAT,
			// false, 3 * 4, vertices);
			// GLES20.glEnableVertexAttribArray(vetricesHandler);
			// if (hasColor) {
			// GLES20.glVertexAttribPointer(colorHandler, 4, GLES20.GL_FLOAT,
			// false, 4 * 4, colors);
			// GLES20.glEnableVertexAttribArray(colorHandler);
			// }
			//
			// if (hasTexCoords) {
			// GLES20.glVertexAttribPointer(texCoorHandler, 2,
			// GLES20.GL_FLOAT, false, 2 * 4, texCoords);
			// GLES20.glEnableVertexAttribArray(texCoorHandler);
			// }
			//
			// if (hasNormals) {
			// GLES20.glVertexAttribPointer(normalsHandler, 3,
			// GLES20.GL_FLOAT, false, 3 * 4, normals);
			// GLES20.glEnableVertexAttribArray(normalsHandler);
			// }

			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, verticeBuffer);
			GLES20.glVertexAttribPointer(vetricesHandler, 3, GLES20.GL_FLOAT,
					false, 3 * 4, 0);
			GLES20.glEnableVertexAttribArray(vetricesHandler);
			if (hasColor) {
				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, colorBuffer);
				GLES20.glVertexAttribPointer(colorHandler, 4, GLES20.GL_FLOAT,
						false, 4 * 4, 0);
				GLES20.glEnableVertexAttribArray(colorHandler);
			}

			if (hasTexCoords) {
				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, texCoordBuffer);
				GLES20.glVertexAttribPointer(texCoorHandler, 2,
						GLES20.GL_FLOAT, false, 2 * 4, 0);
				GLES20.glEnableVertexAttribArray(texCoorHandler);
			}

			if (hasNormals) {
				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, normalBuffer);
				GLES20.glVertexAttribPointer(normalsHandler, 3,
						GLES20.GL_FLOAT, false, 3 * 4, 0);
				GLES20.glEnableVertexAttribArray(normalsHandler);
			}

			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

			GLES20.glUniform1f(alphaHandler, alpha);
			GLES20.glUniform1i(functionIDHandler, functionID);
			if (functionID == 2) {
				GLES20.glUniform4fv(destinationColorHandler, 1, color, 0);
				GLES20.glUniform1f(changeTimeHandler, changeTime);
			}
		}
	}

	/**
	 * 必须先调用bind（），渲染顶点组
	 * 
	 * @param primitiveMode顶点的组织形状
	 *            （点、线、三角形）
	 * @param offset偏移量
	 * @param num渲染的顶点数目
	 */
	public void draw(int primitiveMode, int offset, int num) {
		shader.bind();
		MatrixHelper.getInstance().postModelMatrix(shader);
		MatrixHelper.getInstance().postProjViewMatrix(shader);
		MatrixHelper.getInstance().postTotalMatrix(shader);
		MatrixHelper.getInstance().postViewMatrix(shader);

		GLES20.glUniform1i(texHandler, 0);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		if (texture != null)
			texture.bind();

		if (indices != null) {
			indices.position(offset);
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indiceBuffer);
			GLES20.glDrawElements(primitiveMode, num, GLES20.GL_UNSIGNED_SHORT,
					0);
		} else {
			GLES20.glDrawArrays(primitiveMode, offset, num);
		}
		infoChange = false;
	}

	/**
	 * 取得顶点数目
	 * 
	 * @return int顶点数目
	 */
	public int getNumVertices() {
		return verticesLength / 3;
	}

	/**
	 * 取得索引数目
	 * 
	 * @return int索引数目
	 */
	public int getNumIndices() {
		return indicesLength;
	}

	public int getNum() {
		if (indicesLength > 0)
			return indicesLength;
		else
			return verticesLength / 3;
	}
}