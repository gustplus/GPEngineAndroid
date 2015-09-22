package com.gust.physics_helpUtil;

import javax.vecmath.Vector3f;

import com.bulletphysics.dynamics.constraintsolver.HingeConstraint;
import com.gust.common.math.GPConstants;
import com.gust.game_entry3D.GPGameObject3D_bullet;

/**
 * ת���ؽڵİ�����
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
	 * ����ת���ؽ����ӵ�����GameObject3D
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
	 * ����ת���ؽڿ�ת���ĽǶ�
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
	 * ����ת���ؽ�
	 * @param offsetFromObjACenterToAxis����A��������ת�����λ��
	 * @param offsetFromObjBCenterToAxis����B��������ת�����λ��
	 * @param axisA����Aת�����λ��
	 * @param axisB����Bת�����λ��
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
	 * ����ת���ٶ�
	 * @param targetVelocityת���ٶ�
	 * @param maxMotorImpulseת��������
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
