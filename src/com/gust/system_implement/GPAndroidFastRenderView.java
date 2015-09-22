package com.gust.system_implement;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GPAndroidFastRenderView extends SurfaceView implements Runnable {
	
	GPAndroidGame game;
	Bitmap frameBuffer;
	Thread renderThread = null;
	SurfaceHolder holder;
	volatile boolean running;

	public GPAndroidFastRenderView(GPAndroidGame game, Bitmap frameBuffer)
	{
		super(game);
		this.game = game;
		this.frameBuffer = frameBuffer;
		holder = getHolder();
		// TODO Auto-generated constructor stub
	}

	public void run()
	{
		// TODO Auto-generated method stub
		Rect dstRect = new Rect();
		//a second is 1000000000(10个0) nanosecond s;
		long startTime = System.nanoTime();
		while(running){
			if(!holder.getSurface().isValid())
				continue;
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0F;
			startTime = System.nanoTime();
			
			game.getCurrentScreen().update(deltaTime);
			game.getCurrentScreen().present(deltaTime);
			
			Canvas canvas = holder.lockCanvas();
			//取得canvas的左上、右下的数据（全屏）并赋给dstRect；
			dstRect = canvas.getClipBounds();
			canvas.drawBitmap(frameBuffer, null, dstRect, null);
			holder.unlockCanvasAndPost(canvas);
		}
	}
	
	public void pause(){
		running = false;
		while(true){
			try {
				renderThread.join();
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void resume(){
		running = true;
		renderThread = new Thread(this);
		renderThread.start();
	}

}
