package com.gust.system_implement;

import java.util.ArrayList;
import java.util.List;

import com.gust.common.game_util.GPPool;
import com.gust.common.game_util.GPPool.PoolObjectFactory;
import com.gust.system.GPInput.GPKeyEvent;

import android.view.View;
import android.view.View.OnKeyListener;

public class GPKeyboardHandler implements OnKeyListener {

	boolean[] pressedKeys = new boolean[128];
	List<GPKeyEvent> keyEventsBuffer;
	List<GPKeyEvent> keyEvents;
	GPPool<GPKeyEvent> keyEventsPool;

	public GPKeyboardHandler(View view) {
		PoolObjectFactory<GPKeyEvent> factory = new PoolObjectFactory<GPKeyEvent>() {
			public GPKeyEvent createObject() {
				return new GPKeyEvent();
			}
		};
		keyEventsBuffer = new ArrayList<GPKeyEvent>();
		keyEvents = new ArrayList<GPKeyEvent>();
		keyEventsPool = new GPPool<GPKeyEvent>(factory, 100);
		view.setOnKeyListener(this);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
	}

	public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE)
			return false;

		synchronized (this) {
			GPKeyEvent keyEvent = keyEventsPool.newObject();
			keyEvent.keyCode = keyCode;
			keyEvent.keyChar = (char) event.getUnicodeChar();
			if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
				keyEvent.type = GPKeyEvent.KEY_DOWN;
				if (keyCode > 0 && keyCode < 127)
					pressedKeys[keyCode] = true;
			}
			if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
				keyEvent.type = GPKeyEvent.KEY_UP;
				if (keyCode > 0 && keyCode < 127)
					pressedKeys[keyCode] = false;
			}
			keyEventsBuffer.add(keyEvent);
		}
		return false;
	}

	public List<GPKeyEvent> getKeyEvents() {
		synchronized (this) {
			int len = keyEventsBuffer.size();
			for (int i = 0; i < len; i++)
				keyEventsPool.free(keyEventsBuffer.get(i));
			keyEvents.clear();
			keyEvents.addAll(keyEventsBuffer);
			keyEventsBuffer.clear();
			return keyEvents;
		}
	}

	public boolean isKeyPressed(int keyCode) {
		synchronized (this) {
			if (keyCode < 0 && keyCode > 127)
				return false;
			return pressedKeys[keyCode];
		}
	}

}
