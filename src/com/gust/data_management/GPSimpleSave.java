package com.gust.data_management;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * 用于存放简单的数据，使用SharedPreferences实现
 * @author gustplus
 *
 */
public class GPSimpleSave {
	private Context context;
	private Editor editor;
	SharedPreferences allDatas;
	
	public GPSimpleSave(Context context)
	{
		this.context = context;
	}
	
	/**
	 * 创建文件名为fileName的SharedPreferences文件以保存数据
	 * 或读取文件名为fileName的SharedPreferences文件
	 * @param fileName保存数据的文件名
	 */
	public void loadDatas(String fileName){
		this.allDatas = context.getSharedPreferences(
				fileName, Context.MODE_WORLD_WRITEABLE
						| Context.MODE_WORLD_READABLE);
		editor = this.allDatas.edit();
	}

	/**
	 * 保存之前所输入的数据
	 */
	public void saveDatas()
	{
		this.editor.commit();
//		this.allDatas = null;
//		this.editor = null;
	}
	
	/**
	 * 保存一个名为key的数据，必须在执行saveDatas方法后才会生效
	 * @param key键
	 * @param value值
	 */
	public void addBoolean(String key, boolean value){
		editor.putBoolean(key, value);
	}

	/**
	 * 保存一个名为key的数据，必须在执行saveDatas方法后才会生效
	 * @param key键
	 * @param value值
	 */
	public void addInt(String key, int value){
		editor.putInt(key, value);
	}

	/**
	 * 保存一个名为key的数据，必须在执行saveDatas方法后才会生效
	 * @param key键
	 * @param value值
	 */
	public void addString(String key, String value){
		editor.putString(key, value);
	}

	/**
	 * 保存一个名为key的数据，必须在执行saveDatas方法后才会生效
	 * @param key键
	 * @param value值
	 */
	public void addFloat(String key, float value){
		editor.putFloat(key, value);
	}

	/**
	 * 保存一个名为key的数据，必须在执行saveDatas方法后才会生效
	 * @param key键
	 * @param value值
	 */
	public void addLong(String key, char value){
		editor.putLong(key, value);
	}

	/**
	 * 取得键为key的相应的数据，若没有该记录则返回defaultValue
	 * @param key键
	 * @param defaultValue若没有该记录时返回的默认值
	 * @return boolean
	 */
	public boolean getBoolean(String key, boolean defaultValue){
		return allDatas.getBoolean(key, defaultValue);
	}

	/**
	 * 取得键为key的相应的数据，若没有该记录则返回defaultValue
	 * @param key键
	 * @param defaultValue若没有该记录时返回的默认值
	 * @return int
	 */
	public int getInt(String key, int defaultValue){
		return allDatas.getInt(key, defaultValue);
	}

	/**
	 * 取得键为key的相应的数据，若没有该记录则返回defaultValue
	 * @param key键
	 * @param defaultValue若没有该记录时返回的默认值
	 * @return String
	 */
	public String getString(String key, String defaultValue){
		return allDatas.getString(key, defaultValue);
	}

	/**
	 * 取得键为key的相应的数据，若没有该记录则返回defaultValue
	 * @param key键
	 * @param defaultValue若没有该记录时返回的默认值
	 * @return float
	 */
	public float getFloat(String key, float defaultValue){
		return allDatas.getFloat(key, defaultValue);
	}

	/**
	 * 取得键为key的相应的数据，若没有该记录则返回defaultValue
	 * @param key键
	 * @param defaultValue若没有该记录时返回的默认值
	 * @return long
	 */
	public long getLong(String key, long defaultValue){
		return allDatas.getLong(key, defaultValue);
	}

	/**
	 * 取得键为key的相应的数据，若没有该记录则返回false
	 * @param key键
	 * @param defaultValue若没有该记录时返回的默认值
	 * @return boolean
	 */
	public boolean getBoolean(String key){
		return allDatas.getBoolean(key, false);
	}

	/**
	 * 取得键为key的相应的数据，若没有该记录则返回0
	 * @param key键
	 * @param defaultValue若没有该记录时返回的默认值
	 * @return int
	 */
	public int getInt(String key){
		return allDatas.getInt(key, 0);
	}

	/**
	 * 取得键为key的相应的数据，若没有该记录则返回""
	 * @param key键
	 * @param defaultValue若没有该记录时返回的默认值
	 * @return String
	 */
	public String getString(String key){
		return allDatas.getString(key, "");
	}

	/**
	 * 取得键为key的相应的数据，若没有该记录则返回0.0f
	 * @param key键
	 * @param defaultValue若没有该记录时返回的默认值
	 * @return float
	 */
	public float getFloat(String key){
		return allDatas.getFloat(key, 0.0f);
	}

	/**
	 * 取得键为key的相应的数据，若没有该记录则返回0L
	 * @param key键
	 * @param defaultValue若没有该记录时返回的默认值
	 * @return long
	 */
	public long getLong(String key){
		return allDatas.getLong(key, 0);
	}

	/**
	 * 返回该文件中的数据总数
	 * @return
	 */
	public int length()
	{
		return allDatas.getAll().size();
	}
}
