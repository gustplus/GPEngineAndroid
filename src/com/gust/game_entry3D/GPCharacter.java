package com.gust.game_entry3D;


import com.gust.common.load_util.GPMD2Model;
import com.gust.common.math.GPVector3f;
import com.gust.render_engine.base.GPMatrixState;
/**
 * 游戏角色类
 * @author gustplus
 *
 */
public class GPCharacter {

	public GPMD2Model model;
	public GPVector3f position;
	// 角色面对的方向相对于y轴、与-z轴的夹角
	private float moveAngle;
	// 摄像机面对的方向相对于y轴、与-z轴的夹角
	public float faceAngle;
	private boolean acting;

	/**
	 * 
	 * @param model角色模型
	 * @param position角色位置
	 */
	public GPCharacter(GPMD2Model model, GPVector3f position)
	{
		this.model = model;
		this.position = position;
		this.acting = false;
	}

	/**
	 * 角色移动的方向角
	 * @param angle
	 */
//	public void setMoveAngle(float angle)
//	{
//		this.moveAngle = angle;
//	}
	
	/**
	 * 摄像机面向的角度
	 * @param angle
	 */
	public void setFaceAngle(float angle)
	{
		this.faceAngle = angle;
	}

	public void update(float deltaTime)
	{
		if (acting) {
			model.update(deltaTime);
		} else {
			model.setFrame(0, 0);
		}
	}

	public void move(GPVector3f move)
	{
		if (!acting)
			model.setAnimation(1, true, 10);
		if (move.len() > 1) {
			moveAngle = faceAngle;
//			moveAngle = (float) Math.atan(-move.x/move.y)*Constants.TO_DEGREES;
			position.addTo(move.rotate(faceAngle, 0, 1, 0));
			acting = true;
		} else
			acting = false;
	}
	
	public void draw(){
		GPMatrixState.rotatef(moveAngle, 0, 1, 0);
//		MatrixState.rotatef(moveAngle, 0, 1, 0);
//		MatrixState.translatef(0, -5, -60);
		GPMatrixState.rotatef(90, 0, 1, 0);
		GPMatrixState.rotatef(-90, 1, 0, 0);
		model.draw();
	}

}
