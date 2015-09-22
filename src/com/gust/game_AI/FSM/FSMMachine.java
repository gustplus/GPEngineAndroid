package com.gust.game_AI.FSM;

import java.util.Vector;

import com.gust.game_AI.FSM.FSMState.State;

public class FSMMachine {
	private Vector<FSMState> states;
	private FSMState curState;
	private FSMState nextState;
	private FSMState defaultState;
	private float pastTime;

	public FSMMachine(FSMState defaultState)
	{
		// TODO Auto-generated constructor stub
		this.defaultState = defaultState;
		states = new Vector<FSMState>(10);
	}

	public void addState(FSMState state)
	{
		states.add(state);
	}

	public void setState(FSMState state)
	{
		this.curState = state;
	}

	public void update(float deltaTime)
	{
		pastTime += deltaTime;

		if (states.size() == 0)
			return;

		if (curState == null)
			curState = defaultState;
		if (curState == null)
			return;

		State tempState = curState.checkTransition();
		nextState = transitionState(tempState);
		
		if (pastTime > curState.minLastTime)
			if (!nextState.equals(curState)) {
				curState.exit();
				curState = nextState;
				curState.init();
				curState.enter();
				pastTime = 0;
			}

		curState.update(deltaTime);
	}

	private FSMState transitionState(State state)
	{
		int size = states.size();
		FSMState tempState;
		for (int i = 0; i < size; i++) {
			tempState = states.get(i);
			if (tempState.state == state)
				return tempState;
		}
		return null;
	}
}
