package com.gust.engine.core;

import android.util.Pair;

import com.gust.common.ui.GPEventDispatcher;

public class GPSourceLoader extends GPEventDispatcher {
	public void load(String name){
		GPSourceManager.getInstance().loadSourceAsynchronous(new Pair<String, GPEventDispatcher>(name, this));
	}
}
