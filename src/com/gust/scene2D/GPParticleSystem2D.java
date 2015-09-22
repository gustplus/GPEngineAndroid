package com.gust.scene2D;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.List;

import android.opengl.GLES20;

import com.gust.common.math.GPVector2f;
import com.gust.common.math.GPVector3f;
import com.gust.engine.core.GPShader;
import com.gust.engine.core.GPShaderManager;
import com.gust.render_engine.core.GPTexture2D;
import com.gust.system.GPInput.GPTouch;

public class GPParticleSystem2D extends GPNode2D {
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
	private GPShader shader;
	
	private GPTexture2D tex;
	
	public GPParticleSystem2D(int maxParticles, float liveTime)
	{
		this.maxParticles = maxParticles;
		this.shader = GPShaderManager.getInstance().getShader("particle2D");
		
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(maxParticles * 4 * 3);
		byteBuffer.order(ByteOrder.nativeOrder());
		positions = byteBuffer.asIntBuffer();
		byteBuffer = ByteBuffer.allocateDirect(maxParticles * 4 * 2);
		byteBuffer.order(ByteOrder.nativeOrder());
		velocity = byteBuffer.asIntBuffer();
		byteBuffer = ByteBuffer.allocateDirect(maxParticles * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		time = byteBuffer.asIntBuffer();

		this.beginColor = new float[] { 1, 1, 1, 1 };
		this.endColor = new float[] { 0, 0, 0, 0 };
		this.effectForce = new float[] { 0, 0};
		this.liveTime = liveTime;
		this.pointSize = 10f;

		positionHandler = shader.getAttribLocation("aPosition");
		velocityHandler = shader.getAttribLocation("velocity");
		timeHandler = shader.getAttribLocation("time");
		
		beginColorHandler = shader.getUniformLocation("beginColor");
		endColorHandler = shader.getUniformLocation("endColor");
		pastTimeHandler = shader.getUniformLocation("pastTime");
		effectForceHandler = shader.getUniformLocation("effectForce");
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
	
	public void setTexture(GPTexture2D tex){
		this.tex = tex;
	}

	public void update(List<GPTouch> touches, float deltaTime)
	{
		super.update(touches, deltaTime);
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
	
	public void setPosition(GPVector2f position){
		this.position = position;
	}

	public void init(GPVector2f position, GPVector2f offset, GPVector2f velocity,
			float radius)
	{
		this.position = position;
		int[] positions = new int[maxParticles * 3];
		int[] velocitys = new int[maxParticles * 2];
		int[] times = new int[maxParticles];
		float tempX, tempY;
		GPVector3f finalPosition = new GPVector3f();
		GPVector2f finalVelocity = new GPVector2f();
		for (int i = 0; i < maxParticles; ++i) {
			tempX = (float) Math.random();
			tempY = (float) Math.random();
			finalPosition.set(offset.x * tempX, offset.y * tempY, 0);

			double fwj = 2 * Math.PI * Math.random();
			double yj = 0.35 * Math.PI * Math.random() + 0.15 * Math.PI;
			double vy = Math.sin(yj); // y方向上的速度
			double vx = radius * Math.cos(yj) * Math.sin(fwj); // x方向上的速度

			finalVelocity.set((float) vx, (float) vy);
			finalVelocity.addTo(velocity);

			positions[i * 3] = Float.floatToRawIntBits(finalPosition.x);
			positions[i * 3 + 1] = Float.floatToRawIntBits(finalPosition.y);
			positions[i * 3 + 2] = Float.floatToRawIntBits(finalPosition.z);
			velocitys[i * 2] = Float.floatToRawIntBits(finalVelocity.x);
			velocitys[i * 2 + 1] = Float.floatToRawIntBits(finalVelocity.y);
			
			float tmpTime = (float)(-Math.random()*liveTime*2);
			times[i] = Float.floatToRawIntBits(tmpTime);

		}

		this.positions.clear();
		this.positions.position(0);
		this.positions.put(positions, 0, maxParticles * 3);
		this.positions.flip();

		this.velocity.clear();
		this.velocity.position(0);
		this.velocity.put(velocitys, 0, maxParticles * 2);
		this.velocity.flip();
		
		this.time.clear();
		this.time.position(0);
		this.time.put(times, 0, maxParticles);
		this.time.flip();
	}

	public void drawself()
	{
		shader.bind();
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		this.positions.position(0);
		GLES20.glVertexAttribPointer(positionHandler, 3, GLES20.GL_FLOAT,
				false, 3 * 4, this.positions);

		this.velocity.position(0);
		GLES20.glVertexAttribPointer(velocityHandler, 2, GLES20.GL_FLOAT,
				false, 2 * 4, this.velocity);
		this.time.position(0);
		GLES20.glVertexAttribPointer(timeHandler, 1, GLES20.GL_FLOAT,
				false, 4, this.time);

		GLES20.glUniform1f(liveTimeHandler, liveTime);
		GLES20.glUniform4fv(endColorHandler, 1, endColor, 0);
		GLES20.glUniform4fv(beginColorHandler, 1, beginColor, 0);
		GLES20.glUniform2fv(effectForceHandler, 1, effectForce, 0);
		GLES20.glUniform1f(pastTimeHandler, pastTime);
		GLES20.glUniform1f(sizeHandler, pointSize);

		GLES20.glEnableVertexAttribArray(positionHandler);
		GLES20.glEnableVertexAttribArray(velocityHandler);
		GLES20.glEnableVertexAttribArray(timeHandler);		

		if (tex != null){
			tex.bind();
		}
		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, maxParticles);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	}
}
