package com.gust.data_management;

import android.database.sqlite.SQLiteDatabase;

public class GPDataBaseManagment {
	private SQLiteDatabase dataBase;
	/**
	 * �򿪻򴴽����ݿ�
	 * @param path����+���ݿ���
	 */
	public SQLiteDatabase createDataBase(String path){
		this.dataBase = SQLiteDatabase.openDatabase("/data/data/"+path, null, SQLiteDatabase.CREATE_IF_NECESSARY|SQLiteDatabase.OPEN_READWRITE);
		return dataBase;
	}
	
}
