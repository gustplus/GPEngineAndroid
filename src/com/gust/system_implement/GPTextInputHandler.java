package com.gust.system_implement;

import com.gust.common.game_util.GPLogger;
import com.gust.common.ui.GPUIEditText;
import com.gust.render_engine.base.GPDirector;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class GPTextInputHandler {
	private static GPTextInputHandler instance;

	private GPEditText editText;
	private LinearLayout parent;
	private FrameLayout root;

	private Handler handler;

	private com.gust.common.ui.GPUIEditText target;

	private GPTextInputHandler() {
	}

	public void init(GPEditText editText, FrameLayout root, LinearLayout parent, Handler handler) {
		this.editText = editText;
		this.parent = parent;
		this.handler = handler;
		this.root = root;
	}

	public static GPTextInputHandler getInstance() {
		if (instance == null) {
			instance = new GPTextInputHandler();
		}
		return instance;
	}

	public void setTarget(GPUIEditText target) {
		this.target = target;
	}

	public void showInput() {
//		parent.bringToFront();
//		parent.setVisibility(View.VISIBLE);
		root.addView(parent);
		parent.setClickable(true);
//		parent.requestFocus();
		editText.requestFocus();
		editText.setText(target.getText());
	}

	public void hideInput() {
		root.removeView(parent);
		parent.setClickable(false);
//		parent.setVisibility(View.INVISIBLE);
		target = null;
	}

	public String getText() {
		return editText.getText().toString();
	}

	public void postTextToTarget() {
		if (target != null) {
			final GPUIEditText text = target;
			GPDirector.getInstance().doInGLThread(new Runnable() {
				public void run() {
					text.setText(getText());
				}
			});
		}
	}

	public void callToShowInput() {
		Message msg = new Message();
		msg.what = GPMessageHandler.MSG_SHOW_EDITTEXT;
		handler.sendMessage(msg);
	}

	public void callToHideInput() {
		Message msg = new Message();
		msg.what = GPMessageHandler.MSG_HIDE_EDITTEXT;
		handler.sendMessage(msg);
	}
}
