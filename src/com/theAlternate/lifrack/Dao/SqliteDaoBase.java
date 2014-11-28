package com.theAlternate.lifrack.Dao;

import android.database.sqlite.SQLiteDatabase;

public class SqliteDaoBase{
	protected SQLiteDatabase db;
	
	public SqliteDaoBase(SQLiteDatabase db){
		this.db = db;
	}
}