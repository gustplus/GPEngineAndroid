package com.gust.common.load_util;

import java.io.InputStream;

import com.gust.common.game_util.GPXMLParser;
import com.gust.common.game_util.TMXHandler_Base64;
import com.gust.common.game_util.TMXHandler_XML;
import com.gust.scene2D.TileMap.TileMap;

public class TileMapLoader {
	public static final int TYPE_BASE64 = 0;
	public static final int TYPE_XML = 1;

	public static TileMap loadTileMap(int type, InputStream in) {
		GPXMLParser parser = new GPXMLParser();
		if (type == TYPE_BASE64) {
			TMXHandler_Base64 handler = new TMXHandler_Base64();
			parser.parse(in, handler);
			return handler.getTileMap();
		}else{
			TMXHandler_XML handler = new TMXHandler_XML();
			parser.parse(in, handler);
			return handler.getTileMap();
		}
	}
}
