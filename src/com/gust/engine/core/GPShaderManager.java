package com.gust.engine.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.gust.common.game_util.GPLogger;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.util.Log;

public class GPShaderManager {
	private static GPShaderManager shaderManager;
	private int usingShaderProgram;
	private AssetManager assets;
	private HashMap<String, GPShader> loadedShaders;

	public static String VERTICES_NAME = "aPosition";
	public static String COLOR_NAME = "aColor";
	public static String TEXTURE_COORD_NAME = "aTexCoor";
	public static String NORMAL_NAME = "aNormal";
	public static String TANGENT_NAME = "aTangent";

	public static String CAMERA_POSITION = "cameraPosition";

	public static String AMBIENT_COLOR_NAME = "ambientColor";
	public static String DIFFUSE_COLOR_NAME = "diffuseColor";
	public static String SPCULAR_COLOR_NAME = "specularColor";
	public static String SPCULAR_SHININGESS_NAME = "shiningness";
	public static String EXPONENT_NAME = "exponent";
	public static String POINT_SIZE_NAME = "pointSize";
	public static String LIGHT_DIRECTION_NAME = "lightDirction";
	public static String LIGHT_ANGLE_NAME = "lightAngle";

	public static String MODEL_MATRIX_NAME = "modelMatrix";
	public static String CAMERA_MATRIX_NAME = "cameraMatrix";
	public static String VIEW_MATRIX_NAME = "viewMatrix";
	public static String MVP_MATRIX_NAME = "uMVPMatrix";
	public static String PROJECTION_VIEW_MATRIX_NAME = "projViewMatrix";

	public static void create(AssetManager assets) {
		shaderManager = new GPShaderManager(assets);
	}

	public static GPShaderManager getInstance() {
		return shaderManager;
	}

	private GPShaderManager(AssetManager assets) {
		usingShaderProgram = -1;
		loadedShaders = new HashMap<String, GPShader>();
		this.assets = assets;
	}

	public GPShader getShader(String name) {
		return loadShaderProgram(name);
	}

	public void useShader(GPShader shader) {
		if (usingShaderProgram == shader.getProgram()) {
			return;
		}
		usingShaderProgram = shader.getProgram();
		GLES20.glUseProgram(usingShaderProgram);
	}

	/**
	 * 加载着色程序
	 * 
	 * @param vertexSource顶点着色器程序文件名
	 * @param fragmentSource片元着色器文件名
	 * @return 着色器程序编号
	 */
	private GPShader loadShaderProgram(String name) {
		GPShader shader = null;
		if (loadedShaders.containsKey(name)) {
			shader = loadedShaders.get(name);
		} else {
			GPLogger.log("GPShaderManager", "load shader " + name);
			String vertexName = name + ".vsh";
			String fragmentName = name + ".fsh";
			String mVertexShader = loadFromAssetsFile(vertexName, assets);
			String mFragmentShader = loadFromAssetsFile(fragmentName, assets);
			int program = createProgram(mVertexShader, mFragmentShader);
			shader = new GPShader(program, name);
			loadedShaders.put(name, shader);
		}
		return shader;
	}

	private int loadProgram(String name) {
		int shader = 0;
		GPLogger.log("GPShaderManager", "load shader " + name);
		String vertexName = name + ".vsh";
		String fragmentName = name + ".fsh";
		String mVertexShader = loadFromAssetsFile(vertexName, assets);
		String mFragmentShader = loadFromAssetsFile(fragmentName, assets);
		shader = createProgram(mVertexShader, mFragmentShader);
		return shader;
	}

	/**
	 * 
	 * @param shaderType
	 *            着色器类型（GLES20.GL_VERTEX_SHADER或GLES20.GL_FRAGMENT_SHADER）
	 * @param source
	 *            shader的脚本字符串
	 * @return int shader编号
	 */
	private int loadShader(int shaderType, // shader的类型
			String source // shader的脚本字符串
	) {
		String name;
		if (shaderType == GLES20.GL_VERTEX_SHADER)
			name = "vertex shader";
		else
			name = "frag shader";
		// 创建一个新shader
		int shader = GLES20.glCreateShader(shaderType);
		// 若创建成功则加载shader
		if (shader != 0) {
			// 加载shader的源代码
			GLES20.glShaderSource(shader, source);
			// 编译shader
			GLES20.glCompileShader(shader);
			// 存放编译成功shader数量的数组
			int[] compiled = new int[1];
			// 获取Shader的编译情况
			GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
			if (compiled[0] == 0) {// 若编译失败则显示错误日志并删除此shader
				Log.e("ES20_ERROR", "XXXXXXXXXXXXXXCould not compile shader "
						+ name + "XXXXXXXXXXXXXXXXXXX");
				Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
				GLES20.glDeleteShader(shader);
				shader = 0;
				return shader;
			}
		}
		Log.e("shaderLoader", "!!!!!!!!!!!!!!!!!!!" + name
				+ "  compiled ok!!!!!!!!!!!!!!!!!!!!");
		return shader;
	}

	/**
	 * 创建shader程序的方法
	 * 
	 * @param vertexSource
	 *            顶点shader的脚本字符串
	 * @param fragmentSource
	 *            片元shader的脚本字符串
	 * @return int shader程序的编号
	 */
	private int createProgram(String vertexSource, String fragmentSource) {
		// 加载顶点着色器
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
		if (vertexShader == 0)
			return 0;
		// 加载片元着色器
		int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
		if (pixelShader == 0)
			return 0;
		// 创建程序
		int program = GLES20.glCreateProgram();
		// 若程序创建成功则向程序中加入顶点着色器与片元着色器
		if (program != 0) {
			// 向程序中加入顶点着色器
			GLES20.glAttachShader(program, vertexShader);
			// 向程序中加入片元着色器
			GLES20.glAttachShader(program, pixelShader);
			// 链接程序
			GLES20.glLinkProgram(program);
			GLES20.glDeleteShader(pixelShader);
			GLES20.glDeleteShader(vertexShader);
			// 存放链接成功program数量的数组
			int[] linkStatus = new int[1];
			// 获取program的链接情况
			GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
			// 若链接失败则报错并删除程序
			if (linkStatus[0] != GLES20.GL_TRUE) {
				Log.e("ES20_ERROR",
						"XXXXXXXXXXXXXXXXXXCould not link program:XXXXXXXXXXXXXXXXX ");
				Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
				GLES20.glDeleteProgram(program);
				program = 0;
			}
			Log.e("shaderLoader",
					"!!!!!!!!!!!!!!!!!!!  link ok   !!!!!!!!!!!!!!!!!!!!");
		}
		return program;
	}

	/**
	 * 从sh脚本中加载shader内容的方法
	 * 
	 * @param fname
	 *            脚本文件名
	 * @param assets
	 * @return String 脚本字符串
	 */
	public String loadFromAssetsFile(String fname, AssetManager assets) {
		String result = null;
		try {
			String path = "shaders/" + fname;
			InputStream in = assets.open(path);
			int ch = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((ch = in.read()) != -1) {
				baos.write(ch);
			}
			byte[] buff = baos.toByteArray();
			baos.close();
			in.close();
			result = new String(buff, "UTF-8");
			result = result.replaceAll("\\r\\n", "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String loadFromFile(String fname) {
		String result = null;
		try {
			File file = new File(fname);
			FileInputStream in = new FileInputStream(file);
			int ch = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((ch = in.read()) != -1) {
				baos.write(ch);
			}
			byte[] buff = baos.toByteArray();
			baos.close();
			in.close();
			result = new String(buff, "UTF-8");
			result = result.replaceAll("\\r\\n", "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void removeUnusedSources() {
		Iterator<Map.Entry<String, GPShader>> iter = loadedShaders.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry<String, GPShader> entry = iter.next();
			GPShader shader = entry.getValue();
			if (shader.retainCount() == 1) {
				shader.dispose();
			}
			loadedShaders.remove(entry.getKey());
		}
	}

	public void shutdown() {
		if (shaderManager == null) {
			return;
		}
		loadedShaders.clear();
		loadedShaders = null;
		shaderManager = null;
	}

	public void reload() {
		Iterator<Map.Entry<String, GPShader>> iter = loadedShaders.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry<String, GPShader> entry = iter.next();
			GPShader shader = entry.getValue();
			shader.setProgram(loadProgram(entry.getKey()));
		}
		GPLogger.log("ShaderManager", "reload");
	}
}