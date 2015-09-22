package com.gust.system_implement;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.gust.system.GPMusic;

public class GPAndroidMusic implements GPMusic, OnCompletionListener {
	AssetFileDescriptor assetDescriptor;
	MediaPlayer mediaPlayer;
	boolean isPrepared = false;

	public GPAndroidMusic(AssetFileDescriptor assetDescriptor) {
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
					assetDescriptor.getStartOffset(),
					assetDescriptor.getLength());
			mediaPlayer.prepare();
			isPrepared = true;
			mediaPlayer.setOnCompletionListener(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void play() {
		// TODO Auto-generated method stub
		if (mediaPlayer.isPlaying())
			return;

		try {
			synchronized (this) {
				if (!isPrepared)
					mediaPlayer.prepare();
				mediaPlayer.start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
		// TODO Auto-generated method stub
		mediaPlayer.stop();
		synchronized (this) {
			isPrepared = false;
		}
	}

	public void setLooping(boolean isLoop) {
		// TODO Auto-generated method stub
		mediaPlayer.setLooping(isLoop);
	}

	public void setVolume(float volume) {
		// TODO Auto-generated method stub
		mediaPlayer.setVolume(volume, volume);
	}

	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return mediaPlayer.isPlaying();
	}

	public boolean isStopped() {
		// TODO Auto-generated method stub
		return !isPrepared;
	}

	public boolean isLooping() {
		// TODO Auto-generated method stub
		return mediaPlayer.isLooping();
	}

	public void dispose() {
		// TODO Auto-generated method stub
		if (mediaPlayer.isPlaying())
			mediaPlayer.stop();
		mediaPlayer.release();
	}

	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		synchronized (this) {
			isPrepared = false;
		}
	}

	public void pause() {
		// TODO Auto-generated method stub
		mediaPlayer.pause();
	}

}
