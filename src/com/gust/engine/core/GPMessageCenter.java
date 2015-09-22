package com.gust.engine.core;

import java.util.ArrayList;

public class GPMessageCenter {
	private GPMessageCenter g_messsageCenter;
	public ArrayList<GPMessageHandler> handlers;
	
	private GPMessageCenter() {
		// TODO Auto-generated constructor stub
		handlers = new ArrayList<GPMessageHandler>();
	}
	
	public GPMessageCenter getInstance(){
		if(g_messsageCenter == null){
			g_messsageCenter = new GPMessageCenter();
		}
		return g_messsageCenter;
	}
	
	public void seedMessage(String event){
		int len = handlers.size();
		for (int i = 0; i < len; ++i) {
			if (handlers.get(i).isInstance(event)) {
				handlers.get(i).handleMessage();
			}
		}
	}
	
	public void addHandler(String event, GPMessageHandler handler){
		handler.setType(event);
		handlers.add(handler);
	}
}
 