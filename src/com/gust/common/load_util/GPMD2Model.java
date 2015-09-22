package com.gust.common.load_util;

import java.util.ArrayList;

import android.opengl.GLES20;

import com.gust.engine.core.GPShader;
import com.gust.engine.core.GPSourceManager;
import com.gust.scene3D.GPCoordBuffer2D;
import com.gust.scene3D.GPVertexBuffer3D;

/**
 * MD2格式的模型类
 * @author gustplus
 *
 */
public class GPMD2Model {
	private ArrayList<float[]> frames = new ArrayList<float[]>();
	private ArrayList<GPMD2Animation> animations = new ArrayList<GPMD2Animation>();
//	private float[] texCoords;
	private GPCoordBuffer2D texCoords;
	// private float[] normals;
	private float[] indices;
	private GPVertexBuffer3D curFrame;		// 当前用于显示的模型顶点数据
	private boolean looping;
	private float range = 1;
	private float passedTime = 0;
	private String[] frameNames;
	private GPMD2Animation curAnimation;	//当前执行的动作数据
	private int fps = 30;				//默认播放速度
	private int passedFrames = 0;
	public int size = 0;
	private GPShader shader;
	
	private boolean mipmap;

	/**
	 * 生成MD2模型
	 * 
	 * @param shader 渲染器
	 */
	public GPMD2Model(GPShader shader, int numOfSkins, boolean mipmap)
	{
		this.shader = shader;
		this.mipmap = mipmap;
		this.texCoords = new GPCoordBuffer2D(false, numOfSkins, 0);
	}

	/**
	 * 设置顶点索引
	 * 
	 * @param indices
	 */
	protected void setIndices(float[] indices)
	{
		this.indices = indices;
	}

	/**
	 * 添加帧
	 * 
	 * @param vertices形成该帧的所有顶点坐标
	 */
	protected void addFrame(float[] vertices)
	{
		frames.add(vertices);
		if (curFrame == null) {
			curFrame = new GPVertexBuffer3D(shader);
			curFrame.setVertices(vertices, 0, vertices.length);
		}
	}

	/**
	 * 设置所有帧的名称，用于分割动作
	 * 
	 * @param names
	 */
	protected void setActionNames(String[] names)
	{
		this.frameNames = names;
	}

	/**
	 * 设置模型的纹理贴图坐标
	 * 
	 * @param texCoords
	 */
	protected void setTexCoords(float[] texCoords)
	{
//		this.texCoords = texCoords;
		this.texCoords.setTexCoords(texCoords, 0, texCoords.length);
	}

	// public void setNormals(float[] normals)
	// {
	// this.normals = normals;
	// }

	/**
	 * 设置纹理贴图
	 * 
	 * @param names纹理贴图的文件名
	 */
	public void setTextures(String[] names)
	{
		int len = names.length;
		for (int i = 0; i < len; i++) {
			texCoords.addTexture(GPSourceManager.getInstance().preloadTexture(names[i], mipmap), i);
		}
	}

	/**
	 * 设置动作是否循环播放
	 * 
	 * @param looping
	 */
	public void setLooping(boolean looping)
	{
		this.looping = looping;
	}

	/**
	 * 将所有帧分割为独立的动作片段，必须在所有设置执行完后生效
	 */
	public void divideActions()
	{
		String prefix = null;
		int index = 0;
		GPMD2Animation temp = new GPMD2Animation(-1);
		for (int i = 0; i < frames.size(); i++) {
			String name = frameNames[i];
			// Log.e("", "frameName = " + name);

			if (prefix != null) {
				//将动作名相同的帧组合为同一动作
				if (!name.startsWith(prefix)) {
					temp.setEndFrame(i - 1);
					animations.add(temp);
					prefix = null;
				}
			}
			if (prefix == null) {
				int j = 0;
				for (j = 0; j < name.length(); j++)
					//取得帧名中第一个数字的位置
					if (Character.isDigit(name.charAt(j)))
						break;
				//取得帧名称的文字部分（相同代表为同一动作）
				prefix = name.substring(0, j);
				temp = new GPMD2Animation(index++);
				temp.setStartFrame(i);
			}
		}
		temp.setEndFrame(frames.size() - 1);
		animations.add(temp);
		size = animations.size();
		// Log.e("", "animationNum = " + animations.size());
//		curFrame.setTexCoords(texCoords, 0, texCoords.length);
	}

	// 设置时间流逝速率，可模仿子弹时间，默认为1
	public void setRange(float range)
	{
		this.range = range;
	}

	/**
	 * 设置播放的新动作
	 * 
	 * @param index动作索引
	 * @param looping是否循环播放
	 * @param fps帧
	 *            /秒（播放速度）
	 */
	public void setAnimation(int index, boolean looping, int fps)
	{
		curAnimation = animations.get(index);
		this.looping = looping;
		this.fps = fps;
		passedFrames = 0;
	}

	/**
	 * 用于显示静态模型
	 * 
	 * @param action需显示的静态模型的动作索引
	 * @param frame需显示的静态模型的帧序号
	 *            （相对与动作）
	 */
	public void setFrame(int action, int frame)
	{
		int finalIndex = animations.get(action).startFrame + frame;
		float[] vertices = frames.get(finalIndex);
		curFrame.setVertices(vertices, 0, vertices.length);
	}

	/**
	 * 更新动作
	 * 
	 * @param deltaTime
	 */
	public void update(float deltaTime)
	{
		float dur = 1.0f / fps;
		float time = deltaTime * range;
		int frame1;
		int frame2;

		passedTime += time;
		if (passedTime > dur) {
			passedTime -= dur;
			passedFrames++;
		}
		if (looping) {
			frame1 = passedFrames % curAnimation.duringFrames
					+ curAnimation.startFrame;
			frame2 = (passedFrames + 1) % curAnimation.duringFrames
					+ curAnimation.startFrame;
		} else {
			frame1 = passedFrames + curAnimation.startFrame;
			frame2 = (passedFrames + 1) + curAnimation.startFrame;
			frame1 = frame1 > curAnimation.endFrame ? curAnimation.endFrame
					: frame1;
			frame2 = frame2 > curAnimation.endFrame ? curAnimation.endFrame
					: frame2;
		}

		float[] curVertices = calculateVertices(frames.get(frame1),
				frames.get(frame2), dur, passedTime);
		curFrame.setVertices(curVertices, 0, curVertices.length);
	}

	/**
	 * 显示动作
	 */
	public void draw()
	{
//		skins[0].bind();
		texCoords.bind();
		curFrame.bind();
		if (indices != null)
			curFrame.draw(GLES20.GL_TRIANGLES, 0, curFrame.getNumIndices());
		else
			curFrame.draw(GLES20.GL_TRIANGLES, 0, curFrame.getNumVertices());
	}

	/**
	 * 根据时间计算显示模型
	 * 
	 * @param vertices1
	 * @param vertices2
	 * @param durTime两帧间隔时间
	 * @param curTime运行时间
	 * @return
	 */
	private float[] calculateVertices(float[] vertices1, float[] vertices2,
			float durTime, float curTime)
	{
		int len = vertices1.length;
		float[] finalVertices = new float[len];
		for (int i = 0; i < len; i++) {
			finalVertices[i] = vertices1[i] + (vertices2[i] - vertices1[i])
					* curTime / durTime;
		}
		return finalVertices;
	}
}
