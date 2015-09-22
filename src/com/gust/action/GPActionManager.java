package com.gust.action;

import java.util.ArrayList;

import com.gust.common.game_util.GPLogger;

public class GPActionManager {
	private static GPActionManager actionManager;
	ArrayList<GPAction> actions;
	
	private GPActionManager(int maxNum){
		actions = new ArrayList<GPAction>(maxNum);
	}
	
	public static GPActionManager getInstance(){
		if(actionManager == null)
			actionManager = new GPActionManager(10);
		return actionManager;
	}
	
	public void addAction(GPAction2D action){
		if(actions.contains(action)){
			return;
		}
		this.actions.add(action);
		action.start();
	}
	
	public void update(float deltaTime){
		int len = actions.size();
		for(int i = 0; i < len; ++i){
			GPAction action = actions.get(i);
			action.update(deltaTime);
			if(action.isDone()){
				GPLogger.log("GPActionManager", "romve action", GPLogger.Debug);
				actions.remove(i);
				--i;
				--len;
			}
		}
	}
	
	public void removeAction(int index){
		actions.remove(index);
	}
	
	public void removeAction(GPAction action){
		actions.remove(action);
	}
	
	public void recycle(){
		actions.removeAll(actions);
	}
}
