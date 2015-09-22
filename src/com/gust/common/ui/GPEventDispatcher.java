package com.gust.common.ui;

import java.util.ArrayList;

public class GPEventDispatcher {
	protected ArrayList<GPEventListener> listeners;
	private ArrayList<String> eventQueue;

	private final int maxHandleNum = 4;

	public GPEventDispatcher() {
		listeners = new ArrayList<GPEventListener>(3);
		eventQueue = new ArrayList<String>(3);
	}

	public synchronized void dispatchEvent(String eventType) {
		if (listeners.size() > 0) {
			eventQueue.add(eventType);
		}
	}

	public synchronized void updateEvents(float deltaTime) {
		int eventNum = eventQueue.size();
		eventNum = eventNum > maxHandleNum ? maxHandleNum : eventNum;
		for (int x = 0; x < eventNum; ++x) {
			String eventType = eventQueue.get(x);
			int len = listeners.size();
			for (int i = 0; i < len; ++i) {
				if (listeners.get(i).isInstance(eventType)) {
					boolean remove = listeners.get(i).doEvent(this);
					if (remove) {
						listeners.remove(i);
					}
				}
			}
			eventQueue.remove(x);
			--eventNum;
			--x;
		}
	}

	public synchronized void addEventListener(String type,
			GPEventListener listener) {
		listener.setType(type);
		listeners.add(listener);
	}

	public synchronized void removeEventListener(String type) {
		int len = listeners.size();
		for (int i = 0; i < len; ++i) {
			if (listeners.get(i).type.equals(type)) {
				listeners.remove(i);
			}
		}
	}
}
