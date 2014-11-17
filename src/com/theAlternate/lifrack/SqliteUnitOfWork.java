package com.theAlternate.lifrack;

import android.database.sqlite.SQLiteDatabase;

public class SqliteUnitOfWork implements UnitOfWork{
	private LocalDBHelper localDBHelper;
	private SQLiteDatabase db;
	
	public SqliteUnitOfWork(LocalDBHelper localDBHelper){
		this.localDBHelper = localDBHelper;
		this.db = localDBHelper.getWritableDatabase();
	}
	
	@Override
	public void commit(){
		try{
			if(db.inTransaction()){
				db.setTransactionSuccessful();
			}
		} catch(Exception e){
			e.printStackTrace();
		} 
		finally{
			if(db.inTransaction()) db.endTransaction();
			localDBHelper.close();
		}
	}
	
	@Override
	public UnitOfWork startTransaction(Transaction transaction){
		db.beginTransaction();
		try{
			transaction.execute();
		}catch(Exception e){
			db.endTransaction();
		}
		return this;
	}
	
}