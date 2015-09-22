package com.gust.common.game_util;

import java.util.ArrayList;
import java.util.List;
/**
 * ���ݻ���أ�֧����ʱ������ظ�ʹ�ã����������������Ĺ���
 * @author gustplus
 *
 * @param <T>��ʱ��������
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
	 * �����¶���
	 * @return�¶���
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
	 * �ͷ�ָ������
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