package com.gust.system_implement;

import com.gust.common.game_util.GPLogger;
import com.gust.render_engine.base.GPGLGame;

import android.os.Handler;
import android.os.Message;

public class GPMessageHandler extends Handler {

	public static final int MSG_PLAY_VIDEO = 0;
	public static final int MSG_SHOW_EDITTEXT = 1;
	public static final int MSG_HIDE_EDITTEXT = 2;
	
	private GPGLGame game;
	
	public GPMessageHandler(GPGLGame game) {
		super();
		this.game = game;
	}

	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case MSG_PLAY_VIDEO:
			GPLogger.log("", "name = " + msg.getData().getString("info"));
			game.playVideo(msg.getData().getString("info"));
			break;
		case MSG_SHOW_EDITTEXT:
			GPTextInputHandler.getInstance().showInput();
			break;
			
		case MSG_HIDE_EDITTEXT:
			GPTextInputHandler.getInstance().hideInput();
			break;

		default:
			break;
		}
	}
	
}
