package com.gust.common.load_util;

import java.util.ArrayList;

import android.opengl.GLES20;

import com.gust.engine.core.GPShader;
import com.gust.engine.core.GPSourceManager;
import com.gust.scene3D.GPCoordBuffer2D;
import com.gust.scene3D.GPVertexBuffer3D;

/**
 * MD2��ʽ��ģ����
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
	private GPVertexBuffer3D curFrame;		// ��ǰ������ʾ��ģ�Ͷ�������
	private boolean looping;
	private float range = 1;
	private float passedTime = 0;
	private String[] frameNames;
	private GPMD2Animation curAnimation;	//��ǰִ�еĶ�������
	private int fps = 30;				//Ĭ�ϲ����ٶ�
	private int passedFrames = 0;
	public int size = 0;
	private GPShader shader;
	
	private boolean mipmap;

	/**
	 * ����MD2ģ��
	 * 
	 * @param shader ��Ⱦ��
	 */
	public GPMD2Model(GPShader shader, int numOfSkins, boolean mipmap)
	{
		this.shader = shader;
		this.mipmap = mipmap;
		this.texCoords = new GPCoordBuffer2D(false, numOfSkins, 0);
	}

	/**
	 * ���ö�������
	 * 
	 * @param indices
	 */
	protected void setIndices(float[] indices)
	{
		this.indices = indices;
	}

	/**
	 * ���֡
	 * 
	 * @param vertices�γɸ�֡�����ж�������
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
	 * ��������֡�����ƣ����ڷָ��
	 * 
	 * @param names
	 */
	protected void setActionNames(String[] names)
	{
		this.frameNames = names;
	}

	/**
	 * ����ģ�͵�������ͼ����
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
	 * ����������ͼ
	 * 
	 * @param names������ͼ���ļ���
	 */
	public void setTextures(String[] names)
	{
		int len = names.length;
		for (int i = 0; i < len; i++) {
			texCoords.addTexture(GPSourceManager.getInstance().preloadTexture(names[i], mipmap), i);
		}
	}

	/**
	 * ���ö����Ƿ�ѭ������
	 * 
	 * @param looping
	 */
	public void setLooping(boolean looping)
	{
		this.looping = looping;
	}

	/**
	 * ������֡�ָ�Ϊ�����Ķ���Ƭ�Σ���������������ִ�������Ч
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
				//����������ͬ��֡���Ϊͬһ����
				if (!name.startsWith(prefix)) {
					temp.setEndFrame(i - 1);
					animations.add(temp);
					prefix = null;
				}
			}
			if (prefix == null) {
				int j = 0;
				for (j = 0; j < name.length(); j++)
					//ȡ��֡���е�һ�����ֵ�λ��
					if (Character.isDigit(name.charAt(j)))
						break;
				//ȡ��֡���Ƶ����ֲ��֣���ͬ����Ϊͬһ������
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

	// ����ʱ���������ʣ���ģ���ӵ�ʱ�䣬Ĭ��Ϊ1
	public void setRange(float range)
	{
		this.range = range;
	}

	/**
	 * ���ò��ŵ��¶���
	 * 
	 * @param index��������
	 * @param looping�Ƿ�ѭ������
	 * @param fps֡
	 *            /�루�����ٶȣ�
	 */
	public void setAnimation(int index, boolean looping, int fps)
	{
		curAnimation = animations.get(index);
		this.looping = looping;
		this.fps = fps;
		passedFrames = 0;
	}

	/**
	 * ������ʾ��̬ģ��
	 * 
	 * @param action����ʾ�ľ�̬ģ�͵Ķ�������
	 * @param frame����ʾ�ľ�̬ģ�͵�֡���
	 *            ������붯����
	 */
	public void setFrame(int action, int frame)
	{
		int finalIndex = animations.get(action).startFrame + frame;
		float[] vertices = frames.get(finalIndex);
		curFrame.setVertices(vertices, 0, vertices.length);
	}

	/**
	 * ���¶���
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
	 * ��ʾ����
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
	 * ����ʱ�������ʾģ��
	 * 
	 * @param vertices1
	 * @param vertices2
	 * @param durTime��֡���ʱ��
	 * @param curTime����ʱ��
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
