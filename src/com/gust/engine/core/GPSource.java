package com.gust.engine.core;
/**
 * ������Ϸ��Դ�Ļ���
 * @author gustplus
 */

public abstract class GPSource {
	protected String name;
	protected int retainCount;
//	protected boolean shouldRetain;

	public GPSource(String name) {
		super();
		this.name = name;
		retainCount = 1;
//		shouldRetain = true;
	}

//	public boolean shouldRetain() {
//		return shouldRetain;
//	}
	
	public int retainCount(){
		return retainCount;
	}

	public boolean equals(GPSource other) {
		return this.name.equals(other.name);
	}
	
	public void bind(){
//		shouldRetain = false;
	}

	public void release() {
//		if (!shouldRetain) {
//			dispose();
//		}
		--retainCount;
	}
	
	public void retain(){
		++retainCount;
	}

	public abstract void dispose();
}
