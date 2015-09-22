package com.gust.scene2D.TileMap;

import java.util.ArrayList;

import com.gust.scene2D.GPNode2D;

public class TileMap extends GPNode2D {
	public enum Orientation {
		Orthogonal, Isometric
	}

	private int tileWidth;
	private int tileHeight;

	private Orientation type;

	private int width; // tile的X轴个数

	private int height; // tile的Y轴个数

	private ArrayList<TileSet> tileSets;
	private ArrayList<ObjectGroup> objectGroups;

	public TileMap(int tileWidth, int tileHeight, Orientation type, int width,
			int height) {
		super();
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.type = type;
		this.width = width;
		this.height = height;
		this.tileSets = new ArrayList<TileSet>(2);
		this.objectGroups = new ArrayList<ObjectGroup>(2);
	}
	
	public int getMapHeight(){
		return height * tileHeight;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public int getWidthNum() {
		return this.width;
	}

	public int getHeightNum() {
		return this.height;
	}

	public void addLayer(TileLayer layer) {
		String name = layer.getName();
		int tag = name.hashCode();
		this.addChild(layer, tag);
	}
	
	public void addObjectGroup(ObjectGroup group){
		objectGroups.add(group);
	}

	public ObjectGroup getObjectGroup(String name) {
		int len = objectGroups.size();
		for (int i = 0; i < len; ++i) {
			ObjectGroup group = objectGroups.get(i);
			if (group.getName().equals(name)) {
				return group;
			}
		}
		return null;
	}

	public ObjectGroup getObjectGroup(int index) {
		int len = objectGroups.size();
		if (index < len) {
			return objectGroups.get(index);
		}
		
		return null;
	}

	public TileLayer getLayerByName(String name) {
		int tag = name.hashCode();
		GPNode2D child = this.getChildByTag(tag);
		if (child != null) {
			if (child instanceof TileLayer) {
				return (TileLayer) child;
			}
		}
		return null;
	}

	public void addTileSet(TileSet set) {
		tileSets.add(set);
	}

	public TileSet getTileSet(int index) {
		return tileSets.get(index);
	}

	public int getTileSetNum() {
		return tileSets.size();
	}

	@Override
	public void drawself() {
	}
}
