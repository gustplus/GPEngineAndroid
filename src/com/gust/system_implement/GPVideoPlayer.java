package com.gust.system_implement;

import java.io.IOException;

import com.gust.common.game_util.GPLogger;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

public class GPVideoPlayer extends SurfaceView implements
		OnBufferingUpdateListener, OnCompletionListener,
		MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {

	private int videoWidth;
	private int videoHeight;

	private int deviceWidth;
	private int deviceHeight;

	private enum MediaState {
		Playing, Init, Initialized, Prepared, Pause, Finish
	};

	private static String filePath;

	private MediaState state;

	private SurfaceHolder holder;

	private MediaPlayer player;

	private boolean isAssetvideo;

	private FrameLayout parent;

	public Runnable completionAction;
	
	private int curPos;

	public GPVideoPlayer(Context context, AttributeSet attrs, int defStyle,
			FrameLayout parent, int width, int height) {
		super(context, attrs, defStyle);
		this.parent = parent;
		this.deviceWidth = width;
		this.deviceHeight = height;
		init();
		this.parent.addView(this);
	}

	public GPVideoPlayer(Context context, AttributeSet attrs,
			FrameLayout parent, int width, int height) {
		super(context, attrs);
		this.parent = parent;
		this.deviceWidth = width;
		this.deviceHeight = height;
		init();
		this.parent.addView(this);
	}

	public GPVideoPlayer(Context context, FrameLayout parent, int width,
			int height) {
		super(context);
		this.parent = parent;
		this.deviceWidth = width;
		this.deviceHeight = height;
		init();
		this.parent.addView(this);
	}

	private void init() {
		this.holder = getHolder();
		this.holder.addCallback(this);
		this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		this.holder.setKeepScreenOn(true);

		state = MediaState.Init;

		player = new MediaPlayer();
		this.player.setOnPreparedListener(this);
		this.player.setOnCompletionListener(this);
		this.player.setOnErrorListener(new OnErrorListener() {

			public boolean onError(MediaPlayer mp, int what, int extra) {
				GPLogger.log("", "error : " + what);
				return false;
			}
		});

		setFocusable(true);
		setFocusableInTouchMode(true);
		isAssetvideo = true;
		filePath = "";
	}

	public void play(String file) {
		if (file.equals(filePath)) {
			return;
		}
		player.reset();
		filePath = file;
		if (filePath.startsWith("/")) {
			isAssetvideo = false;
		} else {
			isAssetvideo = true;
		}

		if (state == MediaState.Playing || state == MediaState.Initialized) {
			if (isAssetvideo) {
				prepareAssetvideo(filePath);
			} else {
				preparevideo(filePath);
			}
		}
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		holder = arg0;
		this.player.setDisplay(holder);
		if (state == MediaState.Init) {
			if (!filePath.equals("")) {
				if (isAssetvideo) {
					prepareAssetvideo(filePath);
				} else {
					preparevideo(filePath);
				}
			}
			state = MediaState.Initialized;
		}

		if (state == MediaState.Prepared) {
			player.start();
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// state = MediaState.Finish;
	}

	public void pause() {
		if (state == MediaState.Playing) {
			player.pause();
			state = MediaState.Pause;
		}
	}

	public void resume() {
		if (state == MediaState.Pause) {
			player.start();
			state = MediaState.Playing;
		}
	}
	
	public void onPause(){
		if (state == MediaState.Playing) {
			curPos = player.getCurrentPosition();
			player.stop();
			state = MediaState.Pause;
		}
	}
	
	public void onRsume(){
		if (state == MediaState.Pause) {
			GPLogger.log("", "resume");
			play(filePath);
		}
	}

	private void preparevideo(String path) {
		try {
			this.player.setDataSource(path);
			this.player.prepareAsync();
			this.player.setOnBufferingUpdateListener(this);
			this.player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void prepareAssetvideo(String path) {
		try {
			AssetFileDescriptor afd = GPFileManager.getinstance()
					.getFileDescriptor(path);
			this.player.setDataSource(afd.getFileDescriptor(),
					afd.getStartOffset(), afd.getLength());
			this.player.prepareAsync();
			this.player.setOnBufferingUpdateListener(this);
			this.player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onPrepared(MediaPlayer mp) {
		this.videoWidth = mp.getVideoWidth();
		this.videoHeight = mp.getVideoHeight();
		if (videoWidth != 0 && videoHeight != 0) {
			fixSize();
			if (state == MediaState.Initialized || state == MediaState.Playing) {
				this.player.start();
				this.player.seekTo(curPos);
				curPos = 0;
				state = MediaState.Playing;
			} else {
				state = MediaState.Prepared;
			}
		}
	}

	public void fixSize() {
		int width = deviceWidth;
		int height = deviceHeight;
		int left;
		int top;
		if (videoWidth * deviceHeight > deviceWidth * videoHeight) {
			width = deviceWidth;
			height = deviceWidth * videoHeight / videoWidth;
		} else if (videoWidth * deviceHeight < deviceWidth * videoHeight) {
			width = deviceHeight * videoWidth / videoHeight;
			height = deviceHeight;
		}

		left = (deviceWidth - width) / 2;
		top = (deviceHeight - height) / 2;
		this.holder.setFixedSize(width, height);

		FrameLayout.LayoutParams lParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		lParams.leftMargin = left;
		lParams.topMargin = top;
		setLayoutParams(lParams);
	}

	public void onCompletion(MediaPlayer mp) {
		if (completionAction != null) {
			completionAction.run();
		}
		release();
	}

	public void onBufferingUpdate(MediaPlayer mp, int percent) {

	}

	public void release() {
		player.reset();
		player.release();
//		player = null;
		filePath = "";
		parent.removeView(this);
	}

	public void addCompletionlistener(Runnable r) {
		this.completionAction = r;
	}
}
