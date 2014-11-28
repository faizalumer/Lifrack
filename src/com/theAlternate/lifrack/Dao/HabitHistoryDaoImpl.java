package com.theAlternate.lifrack.Dao;

import com.theAlternate.lifrack.LocalDB.Table_HabitsHistory;

import android.database.sqlite.SQLiteDatabase;

public class HabitHistoryDaoImpl extends SqliteDaoBase implements IHabitHistoryDao{

	public HabitHistoryDaoImpl(SQLiteDatabase db) {
		super(db);
	}
	
	@Override
	public int delete(long habitId){
		int habitCount = db.delete(Table_HabitsHistory.TABLE_NAME, Table_HabitsHistory.COLUMN_ID + "=?", new String[]{String.valueOf(habitId)});
		return habitCount;
	}
	
	
	
}