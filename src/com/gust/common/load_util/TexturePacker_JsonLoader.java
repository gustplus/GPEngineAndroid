  package com.gust.common.load_util;

import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gust.common.game_util.GPLogger;
import com.gust.engine.core.GPSourceManager;
import com.gust.engine.core.GPTextureRegionManager;
import com.gust.render_engine.core.GPTexture2D;
import com.gust.render_engine.core.GPTextureRegion;

import android.util.JsonReader;

public class TexturePacker_JsonLoader {
	JsonReader reader;
	private GPTexture2D tex;
	private int scale;

	public TexturePacker_JsonLoader() {
	}

	public void read(InputStream input) {
		StringBuffer buffer = new StringBuffer();
		Scanner reader = new Scanner(input);
		while (reader.hasNextLine()) {
			buffer.append(reader.nextLine());
		}
		reader.close();
		String json = buffer.toString();
		json = json.trim();
		try {
			JSONObject obj = new JSONObject(json);
			JSONObject infos = obj.getJSONObject("meta");
			String fileName = infos.getString("image");
			tex = GPSourceManager.getInstance().preloadTexture(fileName, true);
			GPLogger.log("TexturePacker_JsonLoader", "tex name = " + fileName);
			scale = infos.getInt("scale");
			
			JSONObject frames = obj.getJSONObject("frames");
			JSONArray names = frames.names();
			int len = names.length();
			for(int i = 0; i < len; ++i){
				String name = names.getString(i);
				GPLogger.log("TexturePacker_JsonLoader", "region name = " + name);
				JSONObject frame = frames.getJSONObject(name);
				JSONObject size = frame.getJSONObject("frame");
				int startX = size.getInt("x");
				int startY = size.getInt("y");
				int w = size.getInt("w");
				int h = size.getInt("h");
				GPTextureRegion region;
				boolean rotated = frame.getBoolean("rotated");
				if(rotated){
					region  = new GPTextureRegion(name, tex, startX, startY, h, w);
					region.setRotated(true);
				}else{
					region  = new GPTextureRegion(name, tex, startX, startY, w, h);
				}
				GPTextureRegionManager.getInstance().addToCache(name, region, false);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
