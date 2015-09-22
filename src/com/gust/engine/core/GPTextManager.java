package com.gust.engine.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.gust.common.game_util.GPLogger;
import com.gust.render_engine.core.GPTexture2D;
import com.gust.render_engine.core.GPTextureRegion;
import com.gust.system_implement.GPFileManager;

public class GPTextManager {
	private HashMap<String, GPTextureRegion> cache;

	private static GPTextManager textmanager;

	private GPTextManager() {
	}

	public static GPTextManager getInstance() {
		if (textmanager == null) {
			textmanager = new GPTextManager();
		}
		return textmanager;
	}

	public GPTexture2D drawText(String info) {
		float textSize;
		String text;
		String[] infos = info.split("/");
		text = infos[0];
		textSize = Float.parseFloat(infos[1]);
		int[] color = new int[] { Integer.parseInt(infos[2]),
				Integer.parseInt(infos[3]), Integer.parseInt(infos[4]),
				Integer.parseInt(infos[5]) };

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setARGB(color[3], color[0], color[1], color[2]);

		Typeface font = null;
		if (infos.length > 6) {
			GPLogger.log("", "size = " + infos.length);
			String fontName = infos[6];
			if (fontName != null && !fontName.equals("")) {
				try {
					font = Typeface.createFromAsset(GPFileManager.getinstance()
							.getAssetManager(), fontName + ".ttf");
				} catch (Exception e) {
					GPLogger.log("GPLabel", "font can't be create",
							GPLogger.Error);
					font = null;
				}
			}
			if (font != null) {
				paint.setTypeface(font);
			}
		}

		paint.setTextSize(textSize);
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		// …œœ¬◊Û”“padding
		int realHeight = (int) (bounds.height() * 1.4f);
		int textWidth = getMinBoundLength(bounds.width() + 2);
		int textHeight = getMinBoundLength(realHeight);
		Bitmap map = Bitmap.createBitmap(textWidth, textHeight,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(map);
		canvas.drawText(text, 0, (int) (bounds.height() * 1.2f), paint);
		GPTexture2D tex = new GPTexture2D(text, map, true);
		GPLogger.log("", "load");
		return tex;
	}

	public void reload() {
		if (cache == null) {
			return;
		}
		GPLogger.log("TextManager", "reload");
		Iterator<Map.Entry<String, GPTextureRegion>> iter = cache.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry<String, GPTextureRegion> entry = iter.next();
			GPTextureRegion region = entry.getValue();
			GPTexture2D tex = drawText(entry.getKey());
			region.setTexture(tex);
		}
	}

	public GPTextureRegion getRegion(String info) {
		if (cache == null) {
			return null;
		}
		return cache.get(info);
	}

	public void addToCache(String info, GPTextureRegion region) {
		if (cache == null) {
			cache = new HashMap<String, GPTextureRegion>(5);
		}
		cache.put(info, region);
	}

	private int getMinBoundLength(int realHeight) {
		int base = 2;
		while (base < 2049) {
			if (realHeight < base) {
				return base;
			} else {
				base = base * 2;
			}
		}
		GPLogger.log("GPLabel", "string is too large!!!!", GPLogger.Error);
		return 0;
	}

	public void shutdown() {
		// Iterator<Map.Entry<String, GPTextureRegion>> iter = cache
		// .entrySet().iterator();
		// while (iter.hasNext()) {
		// Map.Entry<String, GPTextureRegion> entry = iter.next();
		// GPTextureRegion region = entry.getValue();
		// region.getTexture().release();
		// }
		if (textmanager == null) {
			return;
		}
		if (cache != null) {
			cache.clear();
		}
		cache = null;
		textmanager = null;
	}
}
