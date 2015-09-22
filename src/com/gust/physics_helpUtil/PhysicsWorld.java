package com.gust.physics_helpUtil;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

public class PhysicsWorld {
	
	private DynamicsWorld world;
	
	public PhysicsWorld()
	{
		// TODO Auto-generated constructor stub
	}
	
	public void init(Vector3f worldAabbMin, Vector3f worldAabbMax, int maxHandles){
		CollisionConfiguration config = new DefaultCollisionConfiguration();
		Dispatcher dispatcher = new CollisionDispatcher(config);
		AxisSweep3 overloppingPairCache = new AxisSweep3(worldAabbMin, worldAabbMax, maxHandles);
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
		world = new DiscreteDynamicsWorld(dispatcher, overloppingPairCache, solver, config);
	}
	
	public void setGravity(Vector3f gravity){
		world.setGravity(gravity);
	}
	
	public DynamicsWorld getWorld(){
		return world;
	}
}
