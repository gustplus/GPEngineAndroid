package com.gust.common.load_util;
/**
 * ����MD2�ļ���ȡ�Ķ�����Ϣ��������������ػ�����Ϣ�������MD2Modelʹ��
 * @author gustplus
 *
 */
public class GPMD2Animation {
	int index;
	int startFrame;
	int endFrame;
	int duringFrames;

	/**
	 * ָ���ö���ΪMD2�ļ��ĵڼ�����������
	 * @param index�������к�
	 */
	public GPMD2Animation(int index)
	{
		this.index = index;
	}
	/**
	 * ���ö�����ʼ֡�������ȫ�����ļ���
	 * @param startFrame
	 */
	public void setStartFrame(int startFrame)
	{
		this.startFrame = startFrame;
		this.duringFrames = endFrame - startFrame + 1;
	}
	/**
	 * ���ö�����ֹ֡�������ȫ�����ļ���
	 * @param endFrame
	 */
	public void setEndFrame(int endFrame)
	{
		this.endFrame = endFrame;
		this.duringFrames = endFrame - startFrame + 1;
	}
}
