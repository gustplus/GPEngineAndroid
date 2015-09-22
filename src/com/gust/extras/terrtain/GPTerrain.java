package com.gust.extras.terrtain;

import com.gust.common.math.GPVector3f;
import com.gust.render_engine.base.GPGeometry;

import android.util.Log;

public abstract class GPTerrain extends GPGeometry{
	public GPTerrain(GPVector3f position) {
		super(position, null);
		// TODO Auto-generated constructor stub
	}

	protected GPTerrainHeightData data;

	public abstract void draw();

	public GPTerrainHeightData getDatas()
	{
		if (this.data == null)
			Log.e("", "please load the terrain first!");
		return data;
	};
	
	public abstract int getRowNum();
	public abstract int getColNum();
	
	public void changeData(int row, int col, float height)
	{
		data.setData(row, col, height);
//		object.setValueAtVertices(row*data.cols+col, height);
	}
	
}
