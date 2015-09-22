package com.gust.render_engine.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import android.opengl.GLES20;

import com.gust.common.math.GPVector3f;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.base.GPMatrixState;

public class GPParticleSystem {
	private int maxParticles;
	private float pastTime;

	private float liveTime;
	private float pointSize;

	private IntBuffer positions;
	private IntBuffer velocity;
	private IntBuffer time;
	private float[] beginColor;
	private float[] endColor;
	private float[] effectForce;

	private int positionHandler;
	private int velocityHandler;
	private int timeHandler;
	private int beginColorHandler;
	private int endColorHandler;
	private int liveTimeHandler;
	private int pastTimeHandler;
	private int effectForceHandler;
	private int sizeHandler;
//	private int programNum;
	private GPShader shader;
	
	private GPTexture_20 tex;
	
	private GPVector3f position;

	public GPParticleSystem(int maxParticles, float liveTime, GPShader shader)
	{
		// TODO Auto-generated constructor stub
		this.maxParticles = maxParticles;
		this.shader = shader;
		
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(maxParticles * 4 * 3);
		byteBuffer.order(ByteOrder.nativeOrder());
		positions = byteBuffer.asIntBuffer();
		byteBuffer = ByteBuffer.allocateDirect(maxParticles * 4 * 3);
		byteBuffer.order(ByteOrder.nativeOrder());
		velocity = byteBuffer.asIntBuffer();
		byteBuffer = ByteBuffer.allocateDirect(maxParticles * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		time = byteBuffer.asIntBuffer();

		this.beginColor = new float[] { 1, 1, 1, 1 };
		this.endColor = new float[] { 0, 0, 0, 0 };
		this.effectForce = new float[] { 0, 0, 0 };
		this.liveTime = liveTime;
		this.pointSize = 10f;

//		positionHandler = GLES20.glGetAttribLocation(GPShaderManager.shaders[programNum], "aPosition");
		positionHandler = shader.getAttribLocation("aPosition");
//		velocityHandler = GLES20.glGetAttribLocation(GPShaderManager.shaders[programNum], "velocity");
		velocityHandler = shader.getAttribLocation("velocity");
//		timeHandler = GLES20.glGetAttribLocation(GPShaderManager.shaders[programNum], "time");
		timeHandler = shader.getAttribLocation("time");
		
//		beginColorHandler = GLES20.glGetUniformLocation(GPShaderManager.shaders[programNum], "beginColor");
		beginColorHandler = shader.getUniformLocation("beginColor");
//		endColorHandler = GLES20.glGetUniformLocation(GPShaderManager.shaders[programNum], "endColor");
		endColorHandler = shader.getUniformLocation("endColor");
//		liveTimeHandler = GLES20.glGetUniformLocation(GPShaderManager.shaders[programNum], "liveTime");
		liveTimeHandler = shader.getUniformLocation("liveTime");
//		pastTimeHandler = GLES20.glGetUniformLocation(GPShaderManager.shaders[programNum], "pastTime");
		pastTimeHandler = shader.getUniformLocation("pastTime");
//		effectForceHandler = GLES20.glGetUniformLocation(GPShaderManager.shaders[programNum],"effectForce");
		effectForceHandler = shader.getUniformLocation("effectForce");
//		sizeHandler = GLES20.glGetUniformLocation(GPShaderManager.shaders[programNum], "pointSize");
		sizeHandler = shader.getUniformLocation("pointSize");
	}

	public void setColors(GPVector3f begin, GPVector3f end, float beginAlpha,
			float endAlpha)
	{
		beginColor[0] = begin.x;
		beginColor[1] = begin.y;
		beginColor[2] = begin.z;
		beginColor[3] = beginAlpha;
		endColor[0] = end.x;
		endColor[1] = end.y;
		endColor[2] = end.z;
		endColor[3] = endAlpha;
	}
	
	public void setTexture(GPTexture_20 tex){
		this.tex = tex;
	}

	public void update(float deltaTime)
	{
		pastTime += deltaTime;
	}

	public void setPointSize(float pointSize)
	{
		this.pointSize = pointSize;
	}

	public void addForce(GPVector3f force)
	{
		effectForce[0] += force.x;
		effectForce[1] += force.y;
		effectForce[2] += force.z;
	}
	
	public void setPosition(GPVector3f position){
		this.position = position;
	}

	public void init(GPVector3f position, GPVector3f offset, GPVector3f velocity,
			float radius)
	{
		this.position = position;
		int[] positions = new int[maxParticles * 3];
		int[] velocitys = new int[maxParticles * 3];
		int[] times = new int[maxParticles];
		float tempX, tempY, tempZ;
		GPVector3f finalPosition = new GPVector3f();
		GPVector3f finalVelocity = new GPVector3f();
		for (int i = 0; i < maxParticles; ++i) {
			tempX = (float) Math.random();
			tempY = (float) Math.random();
			tempZ = (float) Math.random();
			finalPosition.set(offset.x * tempX, offset.y * tempY, offset.z
					* tempZ);

			double fwj = 2 * Math.PI * Math.random();
			double yj = 0.35 * Math.PI * Math.random() + 0.15 * Math.PI;
			double vy = Math.sin(yj); // y方向上的速度
			double vx = radius * Math.cos(yj) * Math.sin(fwj); // x方向上的速度
			double vz = radius * Math.cos(yj) * Math.cos(fwj); // z方向上的速度

			finalVelocity.set((float) vx, (float) vy, (float) vz);
			finalVelocity.addTo(velocity);

			positions[i * 3] = Float.floatToRawIntBits(finalPosition.x);
			positions[i * 3 + 1] = Float.floatToRawIntBits(finalPosition.y);
			positions[i * 3 + 2] = Float.floatToRawIntBits(finalPosition.z);
			velocitys[i * 3] = Float.floatToRawIntBits(finalVelocity.x);
			velocitys[i * 3 + 1] = Float.floatToRawIntBits(finalVelocity.y);
			velocitys[i * 3 + 2] = Float.floatToRawIntBits(finalVelocity.z);
			
			float tmpTime = (float)(-Math.random()*liveTime*2);
			times[i] = Float.floatToRawIntBits(tmpTime);

		}

		this.positions.clear();
		this.positions.position(0);
		this.positions.put(positions, 0, maxParticles * 3);
		this.positions.flip();

		this.velocity.clear();
		this.velocity.position(0);
		this.velocity.put(velocitys, 0, maxParticles * 3);
		this.velocity.flip();
		
		this.time.clear();
		this.time.position(0);
		this.time.put(times, 0, maxParticles);
		this.time.flip();
	}

	public void draw()
	{
//		GLES20.glUseProgram(GPShaderManager.shaders[programNum]);
		shader.bind();
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		this.positions.position(0);
		GLES20.glVertexAttribPointer(positionHandler, 3, GLES20.GL_FLOAT,
				false, 3 * 4, this.positions);

		this.velocity.position(0);
		GLES20.glVertexAttribPointer(velocityHandler, 3, GLES20.GL_FLOAT,
				false, 3 * 4, this.velocity);
		this.time.position(0);
		GLES20.glVertexAttribPointer(timeHandler, 1, GLES20.GL_FLOAT,
				false, 4, this.time);

		GLES20.glUniform1f(liveTimeHandler, liveTime);
		GLES20.glUniform4fv(endColorHandler, 1, endColor, 0);
		GLES20.glUniform4fv(beginColorHandler, 1, beginColor, 0);
		GLES20.glUniform3fv(effectForceHandler, 1, effectForce, 0);
		GLES20.glUniform1f(pastTimeHandler, pastTime);
		GLES20.glUniform1f(sizeHandler, pointSize);

		GLES20.glEnableVertexAttribArray(positionHandler);
		GLES20.glEnableVertexAttribArray(velocityHandler);
		GLES20.glEnableVertexAttribArray(timeHandler);		
//		GPMatrixState.setShaderProgram(programNum);
		GPMatrixState.setShader(shader);

		this.tex.bind();
		GPMatrixState.pushMatrix();
		GPMatrixState.translatef(position.x, position.y, position.z);
		GPMatrixState.setTotalMatrix();
		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, maxParticles);
		GPMatrixState.popMatrix();
	}
}
