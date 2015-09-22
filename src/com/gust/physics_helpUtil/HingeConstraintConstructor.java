package com.gust.physics_helpUtil;

import javax.vecmath.Vector3f;

import com.bulletphysics.dynamics.constraintsolver.HingeConstraint;
import com.gust.common.math.GPConstants;
import com.gust.game_entry3D.GPGameObject3D_bullet;

/**
 * 转动关节的帮助类
 * @author gustplus
 *
 */
public class HingeConstraintConstructor {
	private GPGameObject3D_bullet objectA;
	private GPGameObject3D_bullet objectB;
	private float maxAngle = 0;
	private float minAngle = 0;
	private HingeConstraint hinge;

	/**
	 * 设置转动关节连接的两个GameObject3D
	 * @param objectA
	 * @param objectB
	 */
	public HingeConstraintConstructor(GPGameObject3D_bullet objectA, GPGameObject3D_bullet objectB)
	{
		// TODO Auto-generated constructor stub
		this.objectA = objectA;
		this.objectB = objectB;
	}

	/**
	 * 设置转动关节可转动的角度
	 * @param minAngle
	 * @param maxAngle
	 */
	public void setLimit(float minAngle, float maxAngle)
	{
		if (minAngle > maxAngle)
			return;
		this.maxAngle = maxAngle;
		this.minAngle = minAngle;
	}

	/**
	 * 启用转动关节
	 * @param offsetFromObjACenterToAxis刚体A的重心与转动轴的位移
	 * @param offsetFromObjBCenterToAxis刚体B的重心与转动轴的位移
	 * @param axisA物体A转动轴的位移
	 * @param axisB物体B转动轴的位移
	 * @param disableCollisionsBetweenLinkedBodies
	 */
	public void setupConstraint(Vector3f offsetFromObjACenterToAxis,
			Vector3f offsetFromObjBCenterToAxis, Vector3f axisA,
			Vector3f axisB, boolean disableCollisionsBetweenLinkedBodies)
	{
		this.hinge = new HingeConstraint(this.objectA.getRigidBody(),
				this.objectB.getRigidBody(), offsetFromObjACenterToAxis,
				offsetFromObjBCenterToAxis, axisA, axisB);
		if (maxAngle != 0)
			hinge.setLimit(minAngle * GPConstants.TO_RADIANS, maxAngle
					* GPConstants.TO_RADIANS);
		objectA.getWorld().addConstraint(hinge,
				disableCollisionsBetweenLinkedBodies);
	}

	/**
	 * 设置转动速度
	 * @param targetVelocity转动速度
	 * @param maxMotorImpulse转动的力量
	 */
	public void enableAngularMotor(float targetVelocity, float maxMotorImpulse)
	{
		hinge.enableAngularMotor(true, targetVelocity, maxMotorImpulse);
	}

	public void disableAngularMotor()
	{
		hinge.enableAngularMotor(false, 0, 0);
	}
}
