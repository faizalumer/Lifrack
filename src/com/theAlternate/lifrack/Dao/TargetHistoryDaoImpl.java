package com.theAlternate.lifrack.Dao;

import com.theAlternate.lifrack.LocalDB.Table_HabitTargetsHistory;

import android.database.sqlite.SQLiteDatabase;

public class TargetHistoryDaoImpl extends SqliteDaoBase implements ITargetHistoryDao{
	public TargetHistoryDaoImpl(SQLiteDatabase db) {
		super(db);
	}

	private static final String LOG_TAG = "TargetDaoImpl";
	
	@Override
	public void deleteByHabitId(long habitId) {
		
		db.delete(Table_HabitTargetsHistory.TABLE_NAME, Table_HabitTargetsHistory.COLUMN_HABIT_ID + "=?" , new String[] {String.valueOf(habitId)});
		
	}

}