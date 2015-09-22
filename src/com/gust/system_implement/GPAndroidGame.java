package com.gust.system_implement;

import com.gust.render_engine.base.GPGLScreen;
import com.gust.system.GPAudio;
import com.gust.system.GPGame;
import com.gust.system.GPGraphics;
import com.gust.system.GPInput;
import com.gust.system.GPScreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

public class GPAndroidGame extends Activity implements GPGame {
	GPAndroidFastRenderView renderView;
	GPGraphics graphics;
	GPAudio audio;
	GPInput input;
	GPScreen screen;
	WakeLock wakeLock;
	GPAndroidInteraction interaction;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		int frameBufferWidth = isLandscape ? 960 : 540;
		int frameBufferHeight = isLandscape ? 540 : 960;
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
				frameBufferHeight, Config.RGB_565);

		float scaleX = (float) frameBufferWidth
				/ getWindowManager().getDefaultDisplay().getWidth();
		float scaleY = (float) frameBufferHeight
				/ getWindowManager().getDefaultDisplay().getHeight();

		renderView = new GPAndroidFastRenderView(this, frameBuffer);
		AssetManager assets = getAssets();
		graphics = new GPAndroidGraphics(assets, frameBuffer);
		audio = new GPAndroidAudio(this);
		input = new GPAndroidInput(this, renderView, scaleX, scaleY);
		//screen = getStartScreen();
		interaction = new GPAndroidInteraction(this);

		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"wakeLock");
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("确认退出吗？");

		builder.setTitle("提示");

		builder.setPositiveButton("确认", new OnClickListener()
		{

			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				finish();
			}
		});

		builder.setNegativeButton("取消", new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		renderView.resume();
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		wakeLock.release();
		screen.pause();
		renderView.pause();
		if (isFinishing())
			screen.dispose();
	}

	public GPAndroidInteraction getInteraction()
	{
		return interaction;
	}

	public GPInput getInput()
	{
		// TODO Auto-generated method stub
		return input;
	}

	public GPGraphics getGraphics()
	{
		// TODO Auto-generated method stub
		return graphics;
	}

	public void setScreen(GPScreen screen)
	{
		// TODO Auto-generated method stub
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null!");

		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}

	public GPScreen getCurrentScreen()
	{
		// TODO Auto-generated method stub
		return screen;
	}

	public GPGLScreen getStartScreen()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setTransparentBackgroudEnable() {
		// TODO Auto-generated method stub
		
	}

	public void setTransparentBackgroudDisable() {
		// TODO Auto-generated method stub
		
	}

}
