package com.gust.common.game_util;

import java.util.ArrayList;
import java.util.List;
/**
 * 数据缓冲池，支持临时对象的重复使用，减少垃圾回收器的工作
 * @author gustplus
 *
 * @param <T>临时对象类型
 */
public class GPPool<T> {

	public interface PoolObjectFactory<T> {
		public T createObject();
	}

	private List<T> freeObjects;
	private PoolObjectFactory<T> factory;
	private int maxSize;

	public GPPool(PoolObjectFactory<T> factory, int maxSize)
	{
		this.factory = factory;
		this.maxSize = maxSize;
		this.freeObjects = new ArrayList<T>(maxSize);
	}

	/**
	 * 创建新对象
	 * @return新对象
	 */
	public T newObject()
	{
		T object = null;
		if (freeObjects.size() == 0)
			object = factory.createObject();
		else {
			object = freeObjects.remove(freeObjects.size() - 1);
		}
		return object;
	}

	/**
	 * 释放指定对象
	 * @param object
	 */
	public void free(T object)
	{
		if(object == null){
			return;
		}
		if (freeObjects.size() < maxSize) {
			freeObjects.add(object);
		}
	}
}