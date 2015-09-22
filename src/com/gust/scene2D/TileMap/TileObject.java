package com.gust.scene2D.TileMap;

import com.gust.physics2D.Bound;

public class TileObject {
	private Bound bound;
	private String name;
	private String type;
	
	public TileObject(Bound bound) {
		super();
		name = "";
		type ="";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	public TileObject() {
		super();
		name = "";
		type ="";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBound(Bound bound){
		this.bound = bound;
	}
	
	public Bound getBound(){
		return bound;
	}
}
