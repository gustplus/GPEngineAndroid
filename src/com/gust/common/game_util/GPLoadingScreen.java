package com.gust.common.game_util;

import java.util.ArrayList;

import com.gust.render_engine.base.GPDirector;
import com.gust.render_engine.base.GPGLGame;
import com.gust.render_engine.base.GPGLScreen;
import com.gust.system.GPInput.GPTouch;

public abstract class GPLoadingScreen extends GPGLScreen {
	private Thread loadThread;
	private boolean isfinished;

	public GPLoadingScreen(GPGLGame glGame) {
		super(glGame);
		// TODO Auto-generated constructor stub
		isfinished = false;

		loadThread = new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				loadResouce();
				synchronized (this) {
					isfinished = true;
					GPLogger.log("", "load ok");
				}
			}
		});
		loadThread.start();
	}

	@Override
	public void update(float deltaTime, ArrayList<GPTouch> touchs) {
		// TODO Auto-generated method stub
		if (isfinished) {
			GPLogger.log("load", "loading finish1");
			synchronized (this) {
				finishedLoading();
				GPLogger.log("load", "loading finish2");
				GPDirector.getInstance().listScreenStack();
			}
		}
	}

	public abstract void finishedLoading();

	public abstract void loadResouce();

	public void stop() {
		while (true) {
			try {
				loadThread.join();
				break;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
