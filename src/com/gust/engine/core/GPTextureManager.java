package com.gust.engine.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.gust.common.game_util.GPLogger;
import com.gust.render_engine.core.GPTexture2D;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GPTextureManager {
	private static GPTextureManager texManager;
	// private int maxTextureIndex;

	private AssetManager assets;
	private HashMap<String, GPTexture2D> loadedTextures;

	private GPTextureManager(AssetManager assets) {
		this.assets = assets;
		// this.maxTextureIndex = 0;
		loadedTextures = new HashMap<String, GPTexture2D>();
	}

	public static GPTextureManager getInstance() {
		return texManager;
	}

	public static void create(AssetManager assets) {
		if (texManager == null) {
			texManager = new GPTextureManager(assets);
		}
	}

	public Bitmap loadBitmap(String name) {
		Bitmap bitmap = null;
		if (loadedTextures.containsKey(name)) {
			return bitmap;
		}
		String path = GPSourceManager.getInstance().getSourcePath() + name;
		GPLogger.log("textureLoader", "load bitmap " + path);
		InputStream input = null;
		try {
			input = assets.open(path);
			bitmap = BitmapFactory.decodeStream(input);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	/**
	 * 
	 * @param name
	 * @param mipmap
	 * @return
	 */
	public GPTexture2D loadTexture2D(String name, boolean mipmap) {
		GPTexture2D tex = null;
		if (loadedTextures.containsKey(name)) {
			tex = loadedTextures.get(name);
		} else {
			String path = GPSourceManager.getInstance().getSourcePath() + name;
			GPLogger.log("textureLoader", "load texture " + path);
			InputStream input = null;
			try {
				input = assets.open(path);
				tex = new GPTexture2D(name, input, mipmap);
				loadedTextures.put(name, tex);
			} catch (IOException e) {
				e.printStackTrace();
				GPLogger.log("GPTextureManager", "load " + name + " failed");
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return tex;
	}

	public void addTextureToCache(String name, GPTexture2D texture) {
		if (!loadedTextures.containsKey(name)) {
			loadedTextures.put(name, texture);
			texture.retain();
		}
	}

	public void removeUnusedSources() {
		Iterator<Map.Entry<String, GPTexture2D>> iter = loadedTextures
				.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, GPTexture2D> entry = iter.next();
			GPTexture2D texture = entry.getValue();
			if (texture.retainCount() == 1) {
				texture.dispose();
			}
			loadedTextures.remove(entry.getKey());
		}
	}

	public void reload() {
		GPLogger.log("TextureManager", "reload");
		Iterator<Map.Entry<String, GPTexture2D>> iter = loadedTextures
				.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, GPTexture2D> entry = iter.next();
			GPTexture2D texture = entry.getValue();
			texture.reload();
		}
	}

	public void shutdown() {
//		Iterator<Map.Entry<String, GPTexture2D>> iter = loadedTextures
//				.entrySet().iterator();
//		while (iter.hasNext()) {
//			Map.Entry<String, GPTexture2D> entry = iter.next();
//			GPTexture2D texture = entry.getValue();
//			texture.release();
//		}
		if (texManager == null) {
			return;
		}
		loadedTextures.clear();
		loadedTextures = null;
		texManager = null;
	}

	public GPTexture2D getTexture2D(String name) {
		GPTexture2D tex = null;
		if (loadedTextures.containsKey(name)) {
			tex = loadedTextures.get(name);
		}
		return tex;
	}
}