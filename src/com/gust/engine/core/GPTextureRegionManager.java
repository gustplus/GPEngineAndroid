package com.gust.engine.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.gust.common.load_util.TexturePacker_JsonLoader;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.system_implement.GPFileManager;

public class GPTextureRegionManager {
	private HashMap<String, GPTextureRegion> cachedRegions;

	private static GPTextureRegionManager manager;

	private GPTextureRegionManager() {
		cachedRegions = new HashMap<String, GPTextureRegion>(20);
	}

	public void loadFromFile(String fileName) {
		TexturePacker_JsonLoader loader = new TexturePacker_JsonLoader();
		InputStream input;
		try {
			input = GPFileManager.getinstance().readAsset(fileName);
			loader.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addToCache(String name, GPTextureRegion region,
			boolean repleaceIfExist) {
		if(region == null){
			return;
		}
		GPTextureRegion temp = cachedRegions.get(name);
		if (temp == null) {
			cachedRegions.put(name, region);
			region.retain();
		} else {
			if (repleaceIfExist) {
				cachedRegions.put(name, region);
			}
		}
	}

	public GPTextureRegion getTextureRegionByName(String name) {
		return cachedRegions.get(name);
	}

	public GPTextureRegion[] getTextureRegionsByPrefix(String prefix) {
		ArrayList<GPTextureRegion> regions = new ArrayList<GPTextureRegion>(10);

		Iterator<Entry<String, GPTextureRegion>> iter = cachedRegions
				.entrySet().iterator();
		while (iter.hasNext()) {
			HashMap.Entry<String, GPTextureRegion> entry = (HashMap.Entry<String, GPTextureRegion>) iter
					.next();
			if (entry.getKey().indexOf(prefix) == 0) {
				regions.add(entry.getValue());
			}
		}
		int len = regions.size();
		GPTextureRegion[] regionArr = new GPTextureRegion[len];
		for (int i = 0; i < len; ++i) {
			regionArr[i] = regions.get(i);
		}
		return regionArr;
	}

	public static GPTextureRegionManager getInstance() {
		if (manager == null) {
			manager = new GPTextureRegionManager();
		}
		return manager;
	}

	public void shutdown() {
		if (manager == null) {
			return;
		}
		cachedRegions.clear();
		cachedRegions = null;
		manager = null;
	}
}
