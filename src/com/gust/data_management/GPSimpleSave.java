package com.gust.data_management;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * ���ڴ�ż򵥵����ݣ�ʹ��SharedPreferencesʵ��
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
	 * �����ļ���ΪfileName��SharedPreferences�ļ��Ա�������
	 * ���ȡ�ļ���ΪfileName��SharedPreferences�ļ�
	 * @param fileName�������ݵ��ļ���
	 */
	public void loadDatas(String fileName){
		this.allDatas = context.getSharedPreferences(
				fileName, Context.MODE_WORLD_WRITEABLE
						| Context.MODE_WORLD_READABLE);
		editor = this.allDatas.edit();
	}

	/**
	 * ����֮ǰ�����������
	 */
	public void saveDatas()
	{
		this.editor.commit();
//		this.allDatas = null;
//		this.editor = null;
	}
	
	/**
	 * ����һ����Ϊkey�����ݣ�������ִ��saveDatas������Ż���Ч
	 * @param key��
	 * @param valueֵ
	 */
	public void addBoolean(String key, boolean value){
		editor.putBoolean(key, value);
	}

	/**
	 * ����һ����Ϊkey�����ݣ�������ִ��saveDatas������Ż���Ч
	 * @param key��
	 * @param valueֵ
	 */
	public void addInt(String key, int value){
		editor.putInt(key, value);
	}

	/**
	 * ����һ����Ϊkey�����ݣ�������ִ��saveDatas������Ż���Ч
	 * @param key��
	 * @param valueֵ
	 */
	public void addString(String key, String value){
		editor.putString(key, value);
	}

	/**
	 * ����һ����Ϊkey�����ݣ�������ִ��saveDatas������Ż���Ч
	 * @param key��
	 * @param valueֵ
	 */
	public void addFloat(String key, float value){
		editor.putFloat(key, value);
	}

	/**
	 * ����һ����Ϊkey�����ݣ�������ִ��saveDatas������Ż���Ч
	 * @param key��
	 * @param valueֵ
	 */
	public void addLong(String key, char value){
		editor.putLong(key, value);
	}

	/**
	 * ȡ�ü�Ϊkey����Ӧ�����ݣ���û�иü�¼�򷵻�defaultValue
	 * @param key��
	 * @param defaultValue��û�иü�¼ʱ���ص�Ĭ��ֵ
	 * @return boolean
	 */
	public boolean getBoolean(String key, boolean defaultValue){
		return allDatas.getBoolean(key, defaultValue);
	}

	/**
	 * ȡ�ü�Ϊkey����Ӧ�����ݣ���û�иü�¼�򷵻�defaultValue
	 * @param key��
	 * @param defaultValue��û�иü�¼ʱ���ص�Ĭ��ֵ
	 * @return int
	 */
	public int getInt(String key, int defaultValue){
		return allDatas.getInt(key, defaultValue);
	}

	/**
	 * ȡ�ü�Ϊkey����Ӧ�����ݣ���û�иü�¼�򷵻�defaultValue
	 * @param key��
	 * @param defaultValue��û�иü�¼ʱ���ص�Ĭ��ֵ
	 * @return String
	 */
	public String getString(String key, String defaultValue){
		return allDatas.getString(key, defaultValue);
	}

	/**
	 * ȡ�ü�Ϊkey����Ӧ�����ݣ���û�иü�¼�򷵻�defaultValue
	 * @param key��
	 * @param defaultValue��û�иü�¼ʱ���ص�Ĭ��ֵ
	 * @return float
	 */
	public float getFloat(String key, float defaultValue){
		return allDatas.getFloat(key, defaultValue);
	}

	/**
	 * ȡ�ü�Ϊkey����Ӧ�����ݣ���û�иü�¼�򷵻�defaultValue
	 * @param key��
	 * @param defaultValue��û�иü�¼ʱ���ص�Ĭ��ֵ
	 * @return long
	 */
	public long getLong(String key, long defaultValue){
		return allDatas.getLong(key, defaultValue);
	}

	/**
	 * ȡ�ü�Ϊkey����Ӧ�����ݣ���û�иü�¼�򷵻�false
	 * @param key��
	 * @param defaultValue��û�иü�¼ʱ���ص�Ĭ��ֵ
	 * @return boolean
	 */
	public boolean getBoolean(String key){
		return allDatas.getBoolean(key, false);
	}

	/**
	 * ȡ�ü�Ϊkey����Ӧ�����ݣ���û�иü�¼�򷵻�0
	 * @param key��
	 * @param defaultValue��û�иü�¼ʱ���ص�Ĭ��ֵ
	 * @return int
	 */
	public int getInt(String key){
		return allDatas.getInt(key, 0);
	}

	/**
	 * ȡ�ü�Ϊkey����Ӧ�����ݣ���û�иü�¼�򷵻�""
	 * @param key��
	 * @param defaultValue��û�иü�¼ʱ���ص�Ĭ��ֵ
	 * @return String
	 */
	public String getString(String key){
		return allDatas.getString(key, "");
	}

	/**
	 * ȡ�ü�Ϊkey����Ӧ�����ݣ���û�иü�¼�򷵻�0.0f
	 * @param key��
	 * @param defaultValue��û�иü�¼ʱ���ص�Ĭ��ֵ
	 * @return float
	 */
	public float getFloat(String key){
		return allDatas.getFloat(key, 0.0f);
	}

	/**
	 * ȡ�ü�Ϊkey����Ӧ�����ݣ���û�иü�¼�򷵻�0L
	 * @param key��
	 * @param defaultValue��û�иü�¼ʱ���ص�Ĭ��ֵ
	 * @return long
	 */
	public long getLong(String key){
		return allDatas.getLong(key, 0);
	}

	/**
	 * ���ظ��ļ��е���������
	 * @return
	 */
	public int length()
	{
		return allDatas.getAll().size();
	}
}
