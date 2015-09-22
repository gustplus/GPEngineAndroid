package com.gust.render_engine.base;

import com.gust.common.game_util.GPLogger;
import com.gust.common.game_util.GPStack;
import com.gust.engine.core.GPSourceManager;

public class GPDirector {
	private GPStack<GPGLScreen> scenes;
	public GPGLScreen screen;
	private GPGLGame game;
	private static GPDirector director;

	private GPDirector() {
		this.scenes = new GPStack<GPGLScreen>(4);
	}
	
	public void setGLGame(GPGLGame game){
		this.game = game;
	}
	
	public void doInGLThread(Runnable event){
		this.game.doInGLThread(event);
	}

	public void repleaceWithScreen(GPGLScreen screen) {
		if (screen == null)
			try {
				throw new IllegalAccessException("screen must not be null");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		if (this.screen != null) {
			GPLogger.log("director", "screen " + this.screen.getTag()
					+ " is out");
			this.screen.pause();
			this.screen.dispose();
		}
		String[] resources = screen.depend();
		GPSourceManager.getInstance().loadSources(resources);
		
		screen.resume();
		// screen.update(0, null);
		// scenes.push(screen);
		this.screen = screen;
	}

	public static GPDirector getInstance() {
		if (director == null) {
			director = new GPDirector();
		}
		return director;
	}

	public GPGLScreen getCurrentScreen() {
		return screen;
	}

	public void runWithScreen(GPGLScreen screen) {
		GPLogger.log("director", screen.getTag() + " is pushed in");
		pushScreen(screen);
	}

	private void pushScreen(GPGLScreen screen) {
		repleaceWithScreen(screen);
		scenes.push(screen);
	}

	public boolean isEmpty() {
		return screen == null && scenes.isEmpty();
	}

	public void popScreen() {
		if (scenes.size() > 1 && screen == scenes.getTop()) {
			scenes.pop();
		}
		if (!scenes.isEmpty()) {
			GPGLScreen screen = scenes.getTop();
			if (screen != null) {
				GPLogger.log("director", "pop");
				repleaceWithScreen(screen);
			}
		}
	}
	
	public void shutdown() {
		if (scenes != null)
			scenes.clear();
		screen = null;
		director = null;
	}

	public void listScreenStack() {
		scenes.list();
	}
	
	public void playVedio(String name){
		game.callToPlayVideo(name);
	}
}
