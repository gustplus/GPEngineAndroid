package com.gust.system_implement;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class GPEditText extends EditText {
	
	private GLSurfaceView glview;

	public GPEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GPEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GPEditText(Context context) {
		super(context);
	}
	
	public void setGLView(GLSurfaceView view){
		glview = view;
	}
	
	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pKeyEvent) {
		super.onKeyDown(pKeyCode, pKeyEvent);

		if (pKeyCode == KeyEvent.KEYCODE_BACK) {
			GPTextInputHandler.getInstance().hideInput();
			this.glview.requestFocus();
		}
		return true;
	}

}
