package com.gust.game_AI.FSM;

public abstract class FSMState {
	public enum State{STATE_IDLE,STATE_WALKING,STATE_ATTACK,STATE_DIED};
	State state;
	State nextState; 
	float minLastTime; 				//最短维持时间

	public abstract void update(float deltaTime);
	
	public abstract void init();
	
	public abstract State checkTransition();
	
	public abstract void enter();
	
	public abstract void exit();
	
	
}
