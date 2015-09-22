package com.gust.system_implement;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.gust.common.game_util.GPLogger;
import com.gust.system.GPAudio;
import com.gust.system.GPMusic;
import com.gust.system.GPSoundEffect;

/**
 * 管理背景音乐及音效的类
 * @author gustplus
 *
 */
public class GPAndroidAudio implements GPAudio {
	AssetManager assets;
	SoundPool soundPool;
	
	public GPAndroidAudio(Activity activity)
	{
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}

	public GPMusic newMusic(String fileName)
	{
		// TODO Auto-generated method stub
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(fileName);
			return new GPAndroidMusic(assetDescriptor);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			GPLogger.log("","can't load music: "+fileName);
			e.printStackTrace();
		}
		return null;
	}

	public GPSoundEffect newSoundEffect(String fileName)
	{
		// TODO Auto-generated method stub
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(fileName);
			int soundID = soundPool.load(assetDescriptor, 0);
			return new GPAndroidSoundEffect(soundPool,soundID);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			GPLogger.log("","can't load soundeffect: "+fileName);
			e.printStackTrace();
		}

		return null;
	}

	public void pauseAllMusics() {
		// TODO Auto-generated method stub
		
	}

	public void resumeAllMusics() {
		// TODO Auto-generated method stub
		
	}
	
}
