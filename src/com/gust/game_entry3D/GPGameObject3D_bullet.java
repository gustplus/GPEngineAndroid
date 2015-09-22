package com.gust.game_entry3D;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
//import com.gust.game_mathematics.GPTransLation;
import com.gust.common.math.GPVector3f;
import com.gust.render_engine.base.GPNode;

/**
 * ��Ϸ����ķ�װ��
 * 
 * @author gustplus
 * 
 */
public class GPGameObject3D_bullet extends GPGameObject3D{
	GPVector3f originPosition; // �������ʼλ��


	CollisionShape shape; // ������ײ��״��������������
	RigidBody rigidBody; // ��������ģ��
	Transform trans; // ����ı任����
	Vector3f intertia; // �����ת������
	float mass; // �����������0Ϊ��̬���壩
	MotionState state;
	DynamicsWorld world;
	float restitution;
	float friction;
	boolean canRotate;

	public GPGameObject3D_bullet(DynamicsWorld world, CollisionShape shape,
			GPVector3f position, GPNode object)
	{
		// TODO Auto-generated constructor stub
		super(position, object);
		this.world = world;
		this.originPosition = position;
		this.shape = shape;
		this.object = object;
		trans = new Transform();
		trans.setIdentity();
		trans.origin.set(position.x, position.y,
				position.z);
		trans.basis.set(new Quat4f(this.object.quat));
		state = new DefaultMotionState(trans);

	}

	public void setCollisionShape(CollisionShape shape)
	{
		this.shape = shape;
	}

	public GPVector3f getPosition()
	{
		return getPosition();
	}

	/**
	 * 
	 * @param restitution����ϵ��
	 * @param frictionĦ����ϵ��
	 * @param mass
	 */
	public void init(float restitution, float friction, float mass)
	{
		this.restitution = restitution;
		this.friction = friction;
		this.mass = mass;
		intertia = new javax.vecmath.Vector3f();
		RigidBodyConstructionInfo info;
		if (mass == 0) {
			info = new RigidBodyConstructionInfo(this.mass, state, shape);
		} else {
			shape.calculateLocalInertia(mass, intertia);
			info = new RigidBodyConstructionInfo(this.mass, state, shape,
					intertia);
		}
		rigidBody = new RigidBody(info);
		rigidBody.setFriction(friction);
		rigidBody.setRestitution(restitution);
		world.addRigidBody(rigidBody);
	}

	public CollisionShape getCollionShape()
	{
		return shape;
	}

	public void setRotatable(boolean canRorate)
	{
		this.canRotate = canRorate;
	}

	public RigidBody getRigidBody()
	{
		return rigidBody;
	}

	public DynamicsWorld getWorld()
	{
		return world;
	}

	public void reset()
	{
		world.removeRigidBody(rigidBody);
		trans = new Transform();
		trans.setIdentity();
		trans.origin.set(originPosition.x, originPosition.y, originPosition.z);
		state = new DefaultMotionState(trans);
		init(this.restitution, this.friction, this.mass);
	}

	public void setLinearVelocity(Vector3f velocity)
	{
		this.rigidBody.activate();
		this.rigidBody.setLinearVelocity(velocity);
	}

	public void update(float deltaTime)
	{
		rigidBody.getMotionState().getWorldTransform(trans);
		this.getPosition()
				.set(trans.origin.x, trans.origin.y, trans.origin.z);
//		Quat4f rotate = trans.getRotation(new Quat4f());
//		float[] rotation = new float[] { 0, 0, 0, 0 };
//		rotation = GPTransLation.translateQuatToAXYZ(rotate);
//		this.setRotation(rotation);
	}
}
