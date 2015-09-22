package com.gust.common.game_util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gust.scene2D.TileMap.TileLayer;
import com.gust.scene2D.TileMap.TileMap;
import com.gust.scene2D.TileMap.TileSet;
import com.gust.scene2D.TileMap.TileMap.Orientation;

public class TMXHandler_Base64 extends DefaultHandler {

	private TileMap tileMap;

	private TileSet curTiles;
	private TileLayer curLayer;
	
	private String data = "";

	private int elementType;
	
	String encoding;
	String compression;

	private final static int TYPE_DATA = 4;

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		data = "";
		if (localName.equals("map")) {
			String orientation = attributes.getValue("orientation");
			TileMap.Orientation orient = Orientation.Orthogonal;
			if (orientation.equals("orthogonal")) {
			}
			if (orientation.equals("isometric")) {
				orient = Orientation.Isometric;
			}
			int width = Integer.parseInt(attributes.getValue("width"));
			int height = Integer.parseInt(attributes.getValue("height"));
			int tileWidth = Integer.parseInt(attributes.getValue("tilewidth"));
			int tileHeight = Integer
					.parseInt(attributes.getValue("tileheight"));

			tileMap = new TileMap(tileWidth, tileHeight, orient, width, height);
			return;
		}
		if (localName.equals("tileset")) {
			int stride = Integer.parseInt(attributes.getValue("firstgid"));
			int tileWidth = Integer.parseInt(attributes.getValue("tilewidth"));
			int tileHeight = Integer
					.parseInt(attributes.getValue("tileheight"));
			curTiles = new TileSet(stride, tileWidth, tileHeight);
			tileMap.addTileSet(curTiles);
			return;
		}
		if (localName.equals("image")) {
			curTiles.setTexture(attributes.getValue("source"));
			return;
		}
		if (localName.equals("layer")) {
			String name = attributes.getValue("name");
			int width = Integer.parseInt(attributes.getValue("width"));
			int height = Integer.parseInt(attributes.getValue("height"));
			curLayer = new TileLayer(tileMap, name, width, height);
			tileMap.addLayer(curLayer);
			return;
		}
		if (localName.equals("data")) {
			elementType = TYPE_DATA;
			encoding = attributes.getValue("encoding");
			compression = attributes.getValue("compression");
			return;
		}
		if (localName.equals("objectgroup")) {
			return;
		}
		if (localName.equals("object")) {
			return;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		elementType = -1;
		if (localName.equals("map")) {
			return;
		}
		if (localName.equals("tileset")) {
			return;
		}
		if (localName.equals("image")) {
			return;
		}
		if (localName.equals("layer")) {
			return;
		}
		if(localName.equals("data")){
			if(data.equals("")){
				return;
			}
			curLayer.setDataOfBase64(data, encoding, compression);
			return;
		}
		if (localName.equals("objectgroup")) {
			return;
		}
		if (localName.equals("object")) {
			return;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		String data = new String(ch, start, length);
		if (elementType == TYPE_DATA) {
			this.data = this.data + data.trim();
		}
	}
	
	public TileMap getTileMap(){
		return tileMap;
	}
}
