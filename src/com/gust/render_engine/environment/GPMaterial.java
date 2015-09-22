package com.gust.render_engine.environment;

import com.gust.engine.core.GPShader;

import android.opengl.GLES20;

public class GPMaterial {
	private float[] ambient = {0.2f,0.2f,0.2f,1};
	private float[] diffuse = {1,1,1,1};
	private float[] specular = {0,0,0,0};
	private float refractness = 1;
	private float shiningness = 10;
	
	private int refractHandler;
	private int shiningnessHandler;
	
	public GPMaterial(GPShader shader){
		refractHandler = shader.getUniformLocation("refractness");
        shiningnessHandler = shader.getUniformLocation("shiningness");
	}
	
	public void setAmbient(float r, float g, float b, float a){
		ambient[0] = r;
		ambient[1] = g;
		ambient[2] = b;
		ambient[3] = a;
	}
	
	public void setDiffuse(float r, float g, float b, float a){
		diffuse[0] = r;
		diffuse[1] = g;
		diffuse[2] = b;
		diffuse[3] = a;
	}
	
	public void setSpecular(float r, float g, float b, float a){
		specular[0] = r;
		specular[1] = g;
		specular[2] = b;
		specular[3] = a;
	}
	
	public void setRefractness(float refractness){
		this.refractness = refractness;
	}
	
	public void setShiningness(float shiningness){
		this.shiningness = shiningness;
	}
	
	public void enable(){
		GLES20.glUniform1f(refractHandler, refractness);
		GLES20.glUniform1f(shiningnessHandler, shiningness);
	}
}
