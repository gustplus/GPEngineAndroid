package com.gust.common.math;

import javax.vecmath.Quat4f;

import android.opengl.Matrix;

public class GPTransLation {
	public static float[] translateQuatToAXYZ(Quat4f q4)
	{	
		double sitaHalf=Math.acos(q4.w);
		float nx=(float) (q4.x/Math.sin(sitaHalf));
		float ny=(float) (q4.y/Math.sin(sitaHalf));
		float nz=(float) (q4.z/Math.sin(sitaHalf));
		
		return new float[]{(float) Math.toDegrees(sitaHalf*2),nx,ny,nz};
	}
	
	public static Quat4f translateAXYZToQuat(float angle, float x,float y,float z){
		float w = (float) Math.cos(angle/2*GPConstants.TO_RADIANS);
		double sin = Math.sin(angle);
		GPVector3f n = new GPVector3f(x, y, z);
		n.normalize();
		float nx = (float) (n.x*sin);
		float ny = (float) (n.y*sin);
		float nz = (float) (n.z*sin);
		return new Quat4f(w, nx, ny, nz);
	}
	
	public static float[] translateQuatToAXYZ(float[] quat)
	{	
		double sitaHalf=Math.acos(quat[0]);
		float nx=(float) (quat[1]/Math.sin(sitaHalf));
		float ny=(float) (quat[2]/Math.sin(sitaHalf));
		float nz=(float) (quat[3]/Math.sin(sitaHalf));
		
		return new float[]{(float) Math.toDegrees(sitaHalf*2),nx,ny,nz};
	}
	
	public float[] getMatrixWithAnglesAndPosition(GPVector3f angles, GPVector3f position){
		float matrix[] = new float[16];
		float result[] = new float[16];
		Matrix.setIdentityM(result, 0);
		Matrix.translateM(matrix, 0, position.x, position.y, position.z);
		Matrix.multiplyMM(result, 0, matrix, 0, result, 0);
		Matrix.rotateM(matrix, 0, angles.z, 0, 0, 1);
		Matrix.multiplyMM(result, 0, matrix, 0, result, 0);
		Matrix.rotateM(matrix, 0, angles.y, 0, 1, 0);
		Matrix.multiplyMM(result, 0, matrix, 0, result, 0);
		Matrix.rotateM(matrix, 0, angles.x, 1, 0, 0);
		Matrix.multiplyMM(result, 0, matrix, 0, result, 0);
		return result;
	}
}
