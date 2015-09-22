package com.gust.data_management;

import android.database.sqlite.SQLiteDatabase;

public class GPDataBaseManagment {
	private SQLiteDatabase dataBase;
	/**
	 * 打开或创建数据库
	 * @param path包名+数据库名
	 */
	public SQLiteDatabase createDataBase(String path){
		this.dataBase = SQLiteDatabase.openDatabase("/data/data/"+path, null, SQLiteDatabase.CREATE_IF_NECESSARY|SQLiteDatabase.OPEN_READWRITE);
		return dataBase;
	}
	
}
