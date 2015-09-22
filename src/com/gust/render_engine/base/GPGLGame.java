package com.gust.render_engine.base;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gust.common.game_util.GPLogger;
import com.gust.engine.core.GPShaderManager;
import com.gust.engine.core.GPSourceManager;
import com.gust.engine.core.GPTextManager;
import com.gust.engine.core.GPTextureManager;
import com.gust.engine.core.GPTextureRegionManager;
import com.gust.render_engine.core.GPCamera2D;
import com.gust.render_engine.core.GPOpenGLStateManager;
import com.gust.render_engine.core.GPCamera2D.FixResolution;
import com.gust.scene2D.GPSprite;
import com.gust.scene2D.SpriteRect;
import com.gust.system.GPAudio;
import com.gust.system.GPGame;
import com.gust.system.GPGraphics;
import com.gust.system.GPInput;
import com.gust.system_implement.GPAndroidAudio;
import com.gust.system_implement.GPEditText;
import com.gust.system_implement.GPFileManager;
import com.gust.system_implement.GPAndroidInput;
import com.gust.system_implement.GPAndroidInteraction;
import com.gust.system_implement.GPMessageHandler;
import com.gust.system_implement.GPTextInputHandler;
import com.gust.system_implement.GPVideoPlayer;

/**
 * 控制游戏的运行状态的类
 * 
 * @author gustplus
 * 
 */
public abstract class GPGLGame extends Activity implements GPGame, Renderer {
	enum GLGameState {
		Initialized, Running, Paused, Finished, Idle // 初始化，运行中，暂停，结束，空闲
	}

	GPCamera2D camera2d;

	private GPInput input;
	private GPAudio audio;
	private GLSurfaceView glView;
	private GLGameState state = GLGameState.Initialized;
	private Object stateChanged = new Object();
	private WakeLock wakeLock;
	private long startTime = System.nanoTime();
	private float deferFrame = 1.0f / 40;
	private float retainTime = 0;

	private GPAndroidInteraction interaction;

	private AlertDialog.Builder builder;

	private boolean shouldReloadSource;

	private FrameLayout mFrameLayout;
	private LinearLayout editLayout;

	private GPVideoPlayer videoPlayer;


	private int deviceWidth;
	private int deviceHeight;

	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 取得屏幕长宽
		deviceWidth = getWindowManager().getDefaultDisplay().getWidth();
		deviceHeight = getWindowManager().getDefaultDisplay().getHeight();

		handler = new GPMessageHandler(this);

		// 防止手机进入休眠
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"wakelock");
		// 布局
		ViewGroup.LayoutParams framelayout_params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		mFrameLayout = new FrameLayout(this);
		mFrameLayout.setLayoutParams(framelayout_params);
		// gl画布
		glView = new GLSurfaceView(this);
		glView.setEGLContextClientVersion(2); // 设置使用OPENGL ES2.0
		glView.getHolder().setFormat(PixelFormat.TRANSPARENT);
		glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		glView.setRenderer(this);
		glView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY); // 设置渲染模式
		mFrameLayout.addView(glView);
		// 文本输入框组
		editLayout = new LinearLayout(this);
		LinearLayout.LayoutParams edittext_layout_params = new LinearLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		editLayout.setLayoutParams(edittext_layout_params);
		editLayout.setGravity(Gravity.CENTER);
		editLayout.setBackgroundColor(Color.BLACK);
		editLayout.getBackground().setAlpha(128);

		ViewGroup.LayoutParams edittext_params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		edittext_params.width = 400;
		GPEditText editText = new GPEditText(this);
		editText.setLayoutParams(edittext_params);
		editText.setGLView(glView);
		editLayout.addView(editText);
		
		GPTextInputHandler.getInstance().init(editText, mFrameLayout, editLayout, handler);

		Button okButton = new Button(this);
		okButton.setText("OK");
		okButton.setHeight(editText.getHeight());
		editLayout.addView(okButton);
		okButton.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				GPTextInputHandler.getInstance().postTextToTarget();
				GPTextInputHandler.getInstance().callToHideInput();
			}
		});
		setContentView(mFrameLayout);
		
		// 初始化公共管理器
		GPDirector.getInstance().setGLGame(this);
		GPTextureManager.create(getAssets());
		GPShaderManager.create(getAssets());
		GPSourceManager.create();
		GPFileManager.create(this);

		audio = new GPAndroidAudio(this);
		input = new GPAndroidInput(this, glView, 1, 1);
		interaction = new GPAndroidInteraction(this);

		this.camera2d = new GPCamera2D();

		builder = new Builder(this);
		builder.setMessage("确认退出吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				GPDirector.getInstance().doInGLThread(new Runnable() {
					public void run() {
						state = GLGameState.Finished;
					}
				});
				dialog.dismiss();
				finish();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				synchronized (stateChanged) {
					state = GLGameState.Running;
				}
			}
		});
		// 标记资源是否需要重新加载
		shouldReloadSource = false;

		GPLogger.log("GPGAME", "create finish");
	}

	public void setOrientation(boolean isLandscape) {
		if (isLandscape) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	public void setDesignSize(int width, int height, FixResolution solution) {
		camera2d.setDesignResolutionSize(width, height, solution);
	}

	public GPCamera2D getCamera() {
		return camera2d;
	}

	public void onBackPressed() {
//		if (editLayout.getVisibility() == View.VISIBLE) {
////			GPTextInputHandler.getInstance().hideInput();
//		} else {
			GPDirector.getInstance().doInGLThread(new Runnable() {
				public void run() {
					state = GLGameState.Paused;
				}
			});
			builder.create().show();
//		}
	}

	public GPAndroidInteraction getInteraction() {
		return interaction;
	}

	public GPInput getInput() {
		return input;
	}

	public GPAudio getAudio() {
		return audio;
	}

	public GPGraphics getGraphics() {
		try {
			throw new IllegalAccessException("we are using OpenGL");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void doInGLThread(Runnable runnable) {
		glView.queueEvent(runnable);
	}

	public abstract GPGLScreen getStartScreen();

	@Override
	public void onResume() {
		GPLogger.log("GPGAME", "resume");
		super.onResume();
		if (videoPlayer != null) {
			GPLogger.log("", "dasda");
			videoPlayer.onRsume();
		}
		wakeLock.acquire();
		glView.onResume();
		audio.resumeAllMusics();
	}

	@Override
	public void onPause() {
		GPLogger.log("GPGAME", "pause");
		super.onPause();
		if (videoPlayer != null) {
			videoPlayer.onPause();
		}
		synchronized (stateChanged) {
			if (isFinishing())
				state = GLGameState.Finished;
			else
				shouldReloadSource = true;
			state = GLGameState.Paused;
			while (true) {
				try {
					stateChanged.wait();
					break;
				} catch (InterruptedException e) {
				}
			}
		}
		GPRuntimeVaribles.SCREENWIDTH = 0;
		GPRuntimeVaribles.SCREENHEIGHT = 0;
		wakeLock.release();
		glView.onPause();
		audio.pauseAllMusics();
	}

	/**
	 * glSurfaceView创建时执行
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GPLogger.log("GPGAME", "surface create");
//		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
//		gl.glDisable(GL10.GL_DITHER);
	}

	/**
	 * glSurfaceView参数改变时执行（如屏幕方向改变等）
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GPLogger.log("gpgame", "sueface change");
		if (shouldReloadSource) {
			shouldReloadSource = false;
		}

		if (width == GPRuntimeVaribles.SCREENWIDTH
				&& height == GPRuntimeVaribles.SCREENHEIGHT) {
			return;
		}
		GPRuntimeVaribles.SCREENWIDTH = width;
		GPRuntimeVaribles.SCREENHEIGHT = height;
		GLES20.glViewport(0, 0, width, height);
		GPLogger.log("GPGLGame", "screen_width = " + width);
		GPLogger.log("GPGLGame", "screen_height = " + height);
		synchronized (stateChanged) {
			if (state == GLGameState.Initialized) {
				GPLogger.log("GPGAME", "init game");
				GPDirector.getInstance().repleaceWithScreen(getStartScreen());
				state = GLGameState.Running;
			} else {
				if (state == GLGameState.Running) {
					return;
				}
				GPLogger.log("GPGAME", "screen resume");
				reload();
				state = GLGameState.Running;
			}
			startTime = System.nanoTime();
		}
	}

	private void reload() {
		GPDirector.getInstance().screen.reloadVBO();
		GPShaderManager.getInstance().reload();
		GPOpenGLStateManager.getInstance().resume();
		GPTextureManager.getInstance().reload();
		GPTextManager.getInstance().reload();
	}

	/**
	 * 用于执行游戏循环刷新的方法（属于Render类的重载方法）
	 */
	public void onDrawFrame(GL10 gl) {
		GLGameState state = null;
		synchronized (stateChanged) {
			state = this.state;
			GPSourceManager.getInstance().update();

			if (state == GLGameState.Idle) {

			}
			if (state == GLGameState.Running) {
				float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
				retainTime += deltaTime;
				if (retainTime >= deferFrame) {
					startTime = System.nanoTime();
					if (GPDirector.getInstance().screen != null) {
						GPDirector.getInstance().screen.mainLoop(deltaTime);
					}
					retainTime -= deferFrame;
				}
			}
			if (state == GLGameState.Paused) {
				if (GPDirector.getInstance().screen != null)
					GPDirector.getInstance().screen.pause();
				state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
			if (state == GLGameState.Finished) {
				if (GPDirector.getInstance().screen != null) {
					// GPDirector.getInstance().screen.pause();
					GPDirector.getInstance().screen.finish();
					GPDirector.getInstance().screen.dispose();
				}
				state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
			this.state = state;
		}
	}

	@Override
	public void finish() {
		GPDirector.getInstance().shutdown();
		GPSourceManager.getInstance().shutdown();
		GPTextManager.getInstance().shutdown();
		GPTextureRegionManager.getInstance().shutdown();
		GPOpenGLStateManager.getInstance().shutdown();
		super.finish();
	}

	public void setTransparentBackgroudEnable() {
		glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
	}

	public void setTransparentBackgroudDisable() {
		glView.getHolder().setFormat(PixelFormat.OPAQUE);
	}

	public void callToPlayVideo(String name) {
		Message msg = new Message();
		msg.what = GPMessageHandler.MSG_PLAY_VIDEO;
		Bundle b = new Bundle();
		b.putString("info", name);
		msg.setData(b);
		handler.sendMessage(msg);
	}

	public void playVideo(String name) {
		doInGLThread(new Runnable() {
			public void run() {
				state = GLGameState.Paused;
			}
		});
		if (videoPlayer == null) {
			videoPlayer = new GPVideoPlayer(this, mFrameLayout, deviceWidth,
					deviceHeight);
			videoPlayer.addCompletionlistener(new Runnable() {
				public void run() {
					glView.setVisibility(View.VISIBLE);
					glView.onResume();
					audio.resumeAllMusics();
					videoPlayer = null;
					doInGLThread(new Runnable() {
						public void run() {
							reload();
							state = GLGameState.Running;
						}
					});
				}
			});
		}
		videoPlayer.play(name);
		glView.onPause();
		glView.setVisibility(View.INVISIBLE);
		audio.pauseAllMusics();
	}
}