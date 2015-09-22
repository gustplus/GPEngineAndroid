package com.gust.system;

public interface GPMusic {
	public void play();
	
	public void stop();
	
	public void pause();
	
	public void setLooping(boolean isLoop);
	
	public void setVolume(float volume);
	
	public boolean isPlaying();
	
	public boolean isStopped();
	
	public boolean isLooping();
	
	public void dispose();
}
