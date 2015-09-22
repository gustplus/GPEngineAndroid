package com.gust.action;

import java.util.ArrayList;

import com.gust.common.game_util.GPLogger;
import com.gust.scene2D.GPNode2D;

public class GPActionSpawn extends GPAction2D {
	private ArrayList<GPAction2D> actions;
	private int finishNum;
	private int num;

	public GPActionSpawn(GPAction2D... actions) {
		num = actions.length;
		this.actions = new ArrayList<GPAction2D>(num);
		for (int i = 0; i < num; ++i) {
			this.actions.add(actions[i]);
		}
	}
	
	private GPActionSpawn(){
		
	}
	
	public void setActions(GPAction2D... actions){
		num = actions.length;
		for (int i = 0; i < num; ++i) {
			this.actions.add(actions[i]);
		}
		this.actions.trimToSize();
		reset();
	}

	@Override
	public void start() {
		super.start();
		this.finishNum = 0;
		for (int i = 0; i < num; ++i) {
			actions.get(i).start();
		}
	}

	@Override
	public void initWithComponent(GPNode2D component) {
		// TODO Auto-generated method stub
		for (int i = 0; i < num; ++i) {
			actions.get(i).initWithComponent(component);
		}
	}

	public void update(float deltaTime) {
		if (!isDone() && isRunning) {
			for (int i = 0; i < num - finishNum; ++i) {
				if (!actions.get(i).isDone()) {
					actions.get(i).update(deltaTime);
				}else{
					GPLogger.log("", "remove " + i);
					++finishNum;
					actions.remove(i);
				}
			}
		}
	}

	public boolean isDone() {
		return finishNum >= num;
	}

	public void reset() {
		finishNum = 0;
		for (int i = 0; i < num; ++i) {
			actions.get(i).reset();
		}
	}
	
	public GPAction2D getAction(int index){
		if(index < num && index > -1){
			return actions.get(index);
		}
		return null;
	}

	@Override
	public GPAction clone() {
		GPActionSpawn spawn = new GPActionSpawn();
		spawn.num = num;
		for (int i = 0; i < num; ++i) {
			spawn.actions.set(i, (GPAction2D)(actions.get(i).clone()));
		}
		
		return spawn;
	}

}
