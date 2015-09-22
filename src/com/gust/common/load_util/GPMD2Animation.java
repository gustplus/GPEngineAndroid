package com.gust.common.load_util;
/**
 * 储存MD2文件读取的动画信息（不包含动画相关绘制信息），配合MD2Model使用
 * @author gustplus
 *
 */
public class GPMD2Animation {
	int index;
	int startFrame;
	int endFrame;
	int duringFrames;

	/**
	 * 指定该动画为MD2文件的第几个动作序列
	 * @param index动作序列号
	 */
	public GPMD2Animation(int index)
	{
		this.index = index;
	}
	/**
	 * 设置动画开始帧（相对于全动画文件）
	 * @param startFrame
	 */
	public void setStartFrame(int startFrame)
	{
		this.startFrame = startFrame;
		this.duringFrames = endFrame - startFrame + 1;
	}
	/**
	 * 设置动画终止帧（相对于全动画文件）
	 * @param endFrame
	 */
	public void setEndFrame(int endFrame)
	{
		this.endFrame = endFrame;
		this.duringFrames = endFrame - startFrame + 1;
	}
}
