package com.gust.scene2D.TileMap;

import java.util.ArrayList;

public class ObjectGroup {
	private ArrayList<TileObject> objects;

	private String name;
	
	public ObjectGroup() {
		objects = new ArrayList<TileObject>(2);
		name = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TileObject getObject(String name) {
		int len = objects.size();
		for (int i = 0; i < len; ++i) {
			TileObject obj = objects.get(i);
			if (name.equals(obj.getName())) {
				return obj;
			}
		}
		return null;
	}
	
	public ArrayList<TileObject> getObjects(){
		return objects;
	}

	public TileObject getObject(int index) {
		if (index < objects.size()) {
			return objects.get(index);
		}
		return null;
	}
	
	public void addObject(TileObject obj){
		objects.add(obj);
	}
}
