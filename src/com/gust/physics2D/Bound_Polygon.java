package com.gust.physics2D;

import com.gust.common.game_util.GPLogger;
import com.gust.common.math.GPVector2f;

public class Bound_Polygon extends Bound {

	private GPLine[] lines;

	private int index;

	public static final int CCW = 0; // ƒÊ ±’Î
	public static final int CW = 1; // À≥ ±’Î

	private int round_type = CCW;;

	public Bound_Polygon(int numOfLines) {
		this.lines = new GPLine[numOfLines];
		this.index = 0;
		this.type = TYPE_POLYGON;
	}
	
	public Bound_Polygon() {
		this.index = 0;
		this.type = TYPE_POLYGON;
	}
	
	public void init(int len){
		if(lines == null){
			lines = new GPLine[len];
		}
	}
	
	public GPLine[] getLines() {
		return lines;
	}

	public void setRoundType(int type) {
		round_type = type;
	}

	public boolean isPointIn(GPVector2f point) {
		// TODO Auto-generated method stub
		for (int i = 0; i < index; ++i) {
			if (round_type == CCW) {
				if (lines[i].distanceWithPiont(point) < 0) {
					return false;
				}
			}else{
				if (lines[i].distanceWithPiont(point) > 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void addLine(GPLine line){
		if(index >= lines.length){
			GPLogger.log("Bound_Ployline", "have no space to add point");
			return;
		}
		lines[index++] = line.clone();
	}

}
