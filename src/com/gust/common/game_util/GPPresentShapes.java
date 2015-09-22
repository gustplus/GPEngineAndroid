package com.gust.common.game_util;

import com.gust.common.math.GPVector3f;
import com.gust.engine.core.GPShader;
import com.gust.render_engine.core.GPVertexBuffer3;

public class GPPresentShapes {

	public static GPVertexBuffer3 createBox(GPVector3f halfSize, GPShader shader/*,boolean hasTex*/){
		GPVertexBuffer3 box = new GPVertexBuffer3(shader);
		float halfX = halfSize.x;
		float halfY = halfSize.y;
		float halfZ = halfSize.z;
		box.setVertices(new float[] { -halfX, halfY, halfZ,   -halfX, -halfY, halfZ,   halfX, halfY, halfZ,   halfX, -halfY, halfZ,
				-halfX, halfY, -halfZ,   -halfX, -halfY, -halfZ,   halfX, halfY, -halfZ,   halfX, -halfY, -halfZ }, 0, 24);
		box.setIndices(new short[] { 0, 1, 2, 1, 2, 3, 4, 5, 0, 0, 5, 1, 2, 3,
				6, 3, 6, 7, 6, 7, 5, 6, 5, 4, 0, 6, 4, 0, 2, 6, 1, 5, 3, 3, 5,
				7 }, 0, 36);
//		if(hasTex){
//			box.setTexCoords(new float[]{0,0, 0,1, 1,0, 1,1, 0,0, 0,1, 1,0, 1,1}, 0, 16);
//		}
		return box;
	}
}
