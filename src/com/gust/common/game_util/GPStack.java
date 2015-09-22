package com.gust.common.game_util;

import java.util.ArrayList;

public class GPStack<T> {
	private ArrayList<T> stack;
	private int maxNum;
	private int top;

	// private int bottom;

	public GPStack(int maxNum) {
		// TODO Auto-generated constructor stub
		stack = new ArrayList<T>(maxNum);
		for(int i = 0; i<maxNum;i++)
			stack.add(null);
		this.top = -1;
		this.maxNum = maxNum;
		// this.bottom = 0;
	}

	public boolean push(T obj) {
		if (top < maxNum - 1) {
			if (stack.size() > top + 1) {
				stack.set(++top, obj);
			} else {
				stack.add(obj);
				++top;
			}
//			GPLogger.log("stack", "stack: " + (top + 1));
			return true;
		} else {
//			GPLogger.log("stack", "stack: " + (top + 1));
			return false;
		}

	}

	public T pop() {
		if (top > -1) {
//			GPLogger.log("stack", "stack: " + (top));
			return stack.remove(top--);
		}else{
			return null;
		}
		
	}

	public boolean clear() {
		// top = bottom = 0;
		top = -1;
		if (stack == null)
			return false;
		stack.clear();
		return true;
	}

	public boolean isEmpty() {
		return top == -1;
	}

	public int size() {
		return stack.size();
	}

	public T getTop() {
		if (!isEmpty())
			return stack.get(top);
		return null;
	}
	
	public void list(){
		int tempTop = top;
		StringBuffer list = new StringBuffer();
		for(;tempTop>-1;--tempTop){
			list.append((tempTop+1)+". " + stack.get(tempTop).toString());
		}
		GPLogger.log("stack", "list from top to bottom:"+list.toString());
	}

}
