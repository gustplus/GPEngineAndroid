package com.gust.system_implement;

import android.media.SoundPool;

import com.gust.system.GPSoundEffect;

public class GPAndroidSoundEffect implements GPSoundEffect {
	SoundPool soundPool;
	int soundID;
	
	public GPAndroidSoundEffect(SoundPool soundPool, int soundID){
		this.soundPool = soundPool;
		this.soundID = soundID;
	}

	public void play(float volume)
	{
		// TODO Auto-generated method stub
		soundPool.play(soundID, volume, volume, 0, 0, 1);
	}
	
	public void stop(){
		soundPool.stop(soundID);
	}
	
	public void pause(){
		soundPool.pause(soundID);
	}
	
	public void dispose()
	{
		// TODO Auto-generated method stub
		soundPool.unload(soundID);
	}

}
