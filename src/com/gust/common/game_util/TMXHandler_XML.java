package com.gust.common.game_util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gust.common.math.GPVector2f;
import com.gust.physics2D.Bound_Polygon;
import com.gust.physics2D.Bound_Rectangle;
import com.gust.physics2D.GPLine;
import com.gust.scene2D.TileMap.ObjectGroup;
import com.gust.scene2D.TileMap.TileLayer;
import com.gust.scene2D.TileMap.TileMap;
import com.gust.scene2D.TileMap.TileObject;
import com.gust.scene2D.TileMap.TileSet;
import com.gust.scene2D.TileMap.TileMap.Orientation;

public class TMXHandler_XML extends DefaultHandler {

	private TileMap tileMap;

	private TileSet curTiles;
	private TileLayer curLayer;
	private ObjectGroup curGroup;
	private TileObject curObject;
	private Bound_Polygon curPolygon;
	private GPVector2f originPoint;

	private int[] datas;
	private int index;

	@Override
	public void startDocument() throws SAXException {}

	@Override
	public void endDocument() throws SAXException {}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
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
			if (attributes.getValue("spacing") != null) {
				int spacing = Integer.parseInt(attributes.getValue("spacing"));
				curTiles.setSpacing(spacing);
			}
			if (attributes.getValue("margin") != null) {
				int margin = Integer.parseInt(attributes.getValue("margin"));
				curTiles.setMargin(margin);
			}
			tileMap.addTileSet(curTiles);
			return;
		}
		if (localName.equals("tileoffset")) {
			int x = Integer.parseInt(attributes.getValue("x"));
			int y = Integer.parseInt(attributes.getValue("y"));
			curTiles.setTileOffset(x, y);
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

			datas = new int[width * height];
			return;
		}
		if (localName.equals("data")) {
			index = 0;
			return;
		}
		if (localName.equals("tile")) {
			datas[index++] = Integer.parseInt(attributes.getValue("gid"));
			return;
		}
		if (localName.equals("objectgroup")) {
			curGroup = new ObjectGroup();
			curGroup.setName(attributes.getValue("name"));
			return;
		}
		if (localName.equals("object")) {
			curObject = new TileObject();
			int x = Integer.parseInt(attributes.getValue("x"));
			int y = Integer.parseInt(attributes.getValue("y"));
			originPoint = new GPVector2f(x, y);

			String name = attributes.getValue("name");
			if(name != null){
				curObject.setName(name);
			}
			name = attributes.getValue("type");
			if(name != null){
				curObject.setType(name);
			}
			
			String widthString = attributes.getValue("width");
			if (widthString != null) {
				int width = Integer.parseInt(widthString);
				int height = Integer.parseInt(attributes.getValue("height"));
				Bound_Rectangle rect = new Bound_Rectangle(x,
						tileMap.getMapHeight() - y - height, width, height);
				curObject.setBound(rect);
			} else {
				curPolygon = new Bound_Polygon();
				curObject.setBound(curPolygon);
			}
			return;
		}

		if (localName.equals("polyline")) {
			String points = attributes.getValue("points");
			String[] arr = points.split("\\s+");
			int len = arr.length;
			curPolygon.init(len - 1);

			GPVector2f translate = new GPVector2f(0, tileMap.getMapHeight());
			GPVector2f prePoint = null;
			for (int i = 0; i < len; ++i) {
				String[] point = arr[i].split(",");
				int x = Integer.parseInt(point[0]);
				int y = Integer.parseInt(point[1]);
				GPVector2f tmp = originPoint.add(x, y);
				tmp = translate.sub(-tmp.x, tmp.y);
				if (prePoint != null) {
					GPLine line = new GPLine(prePoint, tmp);
					curPolygon.addLine(line);
				}
				prePoint = tmp;
			}
		}

		if (localName.equals("polygon")) {
			String points = attributes.getValue("points");
			String[] arr = points.split("\\s+");
			int len = arr.length;
			curPolygon.init(len);
			
			GPVector2f translate = new GPVector2f(0, tileMap.getMapHeight());
			GPVector2f firstPoint = null;
			GPVector2f prePoint = null;
			for (int i = 0; i < len; ++i) {
				String[] point = arr[i].split(",");
				int x = Integer.parseInt(point[0]);
				int y = Integer.parseInt(point[1]);
				GPVector2f tmp = originPoint.add(x, y);
				tmp = translate.sub(-tmp.x, tmp.y);
				if (i == 0) {
					firstPoint = tmp;
				}
				if (prePoint != null) {
					GPLine line = new GPLine(prePoint, tmp);
					curPolygon.addLine(line);
				}
				prePoint = tmp;
			}
			GPLine line = new GPLine(prePoint, firstPoint);
			curPolygon.addLine(line);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
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
		if (localName.equals("data")) {
			curLayer.setDataOfXML(datas);
			return;
		}
		if (localName.equals("objectgroup")) {
			tileMap.addObjectGroup(curGroup);
			return;
		}
		if (localName.equals("object")) {
				curGroup.addObject(curObject);
			return;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {}

	public TileMap getTileMap() {
		return tileMap;
	}
}
