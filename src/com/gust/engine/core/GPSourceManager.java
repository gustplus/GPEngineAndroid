package com.gust.engine.core;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.util.Pair;

import com.gust.common.game_util.GPLogger;
import com.gust.common.ui.GPEventDispatcher;
import com.gust.render_engine.base.GPDirector;
import com.gust.render_engine.core.GPTexture2D;
import com.gust.scene2D.GPProcessEvent;

public class GPSourceManager {
	private static GPSourceManager manager;
	private String sourcePath;
	private ArrayList<Pair<String, GPEventDispatcher>> waitSource;

	private int retainThread;

	private GPSourceManager() {
		sourcePath = "";
		retainThread = Config.MAX_LOAD_THREAD;
		waitSource = new ArrayList<Pair<String, GPEventDispatcher>>(5);
	}

	public static void create() {
		if (manager == null) {
			manager = new GPSourceManager();
		}
	}

	public static GPSourceManager getInstance() {
		return manager;
	}

	public void setSourcePath(String path) {
		String lastChar = path.substring(path.length() - 1);
		if (lastChar == "/" || lastChar == "\\") {
			// path = path.substring(0, path.length() - 1);
		} else {
			path = path + "/";
		}
		sourcePath = path;
	}

	public void loadSources(String[] sources) {
		if (sources == null) {
			return;
		}
		int len = sources.length;
		for (int i = 0; i < len; ++i) {
			String source = sources[i];
			loadSource(source);
		}
		GPTextureManager.getInstance().removeUnusedSources();
		GPShaderManager.getInstance().removeUnusedSources();
	}

	public void loadSource(String source) {
		String[] arr = source.split("\\.");
		if (arr.length < 2) {
			GPLogger.log("PGSourceManager", source
					+ "load failed, format error");
			return;
		}
		String type = arr[0];
		if (type.equals("shader")) {
			preloadShader(arr[1]);
			return;
		}
		if (type.equals("texture")) {
			if (arr.length > 4 && arr[3].equals("mipmap")) {
				preloadTexture(arr[1] + "." + arr[2], true);
			} else {
				preloadTexture(arr[1] + "." + arr[2], false);
			}
		} else
			GPLogger.log("GPSourceManager", source
					+ " load failed, source type error");
	}

	public void loadSourceAsynchronous(
			final Pair<String, GPEventDispatcher> pair) {
		if (0 == retainThread) {
			waitSource.add(pair);
			return;
		}

		final String[] arr = pair.first.split("\\.");
		if (arr.length < 2) {
			GPLogger.log("PGSourceManager", pair.first
					+ "load failed, format error");
			return;
		}
		String type = arr[0];
		Thread thread = null;
		if (type.equals("texture")) {
			final String name = arr[1] + "." + arr[2];
			thread = new Thread(new Runnable() {
				public void run() {
					final Bitmap bitmap = GPTextureManager.getInstance()
							.loadBitmap(name);
					GPDirector.getInstance().doInGLThread(new Runnable() {
						public void run() {
							if (bitmap != null) {
								if (arr.length > 4 && arr[3].equals("mipmap")) {
									GPTexture2D tex = new GPTexture2D(name,
											bitmap, true);
									GPTextureManager.getInstance()
											.addTextureToCache(tex.name, tex);
								} else {
									GPTexture2D tex = new GPTexture2D(name,
											bitmap, false);
									GPTextureManager.getInstance()
											.addTextureToCache(tex.name, tex);
								}
								GPLogger.log("GPSourceManager", "finished ");
							}
							pair.second
							.dispatchEvent(GPProcessEvent.PROCESS_FINISH);
							++retainThread;
						}
					});
				}
			});
		} else {
			thread = new Thread(new Runnable() {
				public void run() {
					loadSource(pair.first);
					pair.second.dispatchEvent(GPProcessEvent.PROCESS_FINISH);
					// pair.second.removeEventListener(GPProcessEvent.PROCESS_FINISH);
					++retainThread;
				}
			});
		}
		--retainThread;
		thread.start();
	}

	public void update() {
		int len = waitSource.size();
		int num = len > Config.MAX_LOAD_THREAD ? Config.MAX_LOAD_THREAD : len;
		for (int i = 0; i < num; ++i) {
			loadSourceAsynchronous(waitSource.remove(i));
		}
	}

	/**
	 * you should first set the source path at GPSourceManager and then pass the
	 * name of the bitmap
	 * 
	 * @param name
	 *            name of the bitmap
	 * @param mipmap
	 *            should be mipmap
	 * @return texture
	 */
	public GPTexture2D preloadTexture(String name, boolean mipmap) {
		return GPTextureManager.getInstance().loadTexture2D(name, mipmap);
	}

	public GPShader preloadShader(String name) {
		return GPShaderManager.getInstance().getShader(name);
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void shutdown() {
		GPTextureManager.getInstance().shutdown();
		GPShaderManager.getInstance().shutdown();
	}
}