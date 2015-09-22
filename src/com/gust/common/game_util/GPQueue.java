package com.gust.common.game_util;

import java.util.ArrayList;

public class GPQueue<T> {
	ArrayList<T> queue;
	private int in;
	private int out;

	public GPQueue() {
		queue = new ArrayList<T>();
		in = 0;
		out = 0;
	}

	public void push(T object) {
		if (out > 0) {
			for (int i = 0; i < out; ++i) {
				queue.remove(i);
			}
			out = 0;
		}
		queue.add(object);
		in = queue.size();
	}

	public T pop() {
		if (in > out) {
			return queue.get(out++);
		} else {
			return null;
		}
	}
	
	public void clear(){
		queue.clear();
		in = 0;
		out = 0;
	}
	
	public boolean isEmpty(){
		return (in - out) > 0;
	}
}