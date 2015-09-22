package com.gust.system;

public interface GPAudio {
	public GPMusic newMusic(String fileName);
	
	public void pauseAllMusics();
	
	public void resumeAllMusics();
	
	public GPSoundEffect newSoundEffect(String fileName);
}
