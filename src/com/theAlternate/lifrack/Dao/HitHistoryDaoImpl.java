package com.theAlternate.lifrack.Dao;

import com.theAlternate.lifrack.LocalDB.Table_HabitHitsHistory;

import android.database.sqlite.SQLiteDatabase;

public class HitHistoryDaoImpl extends SqliteDaoBase implements IHitHistoryDao{
	
	public HitHistoryDaoImpl(SQLiteDatabase db) {
		super(db);
	}

	@Override
	public void deleteByHabitId(long habitId) {
		db.delete(Table_HabitHitsHistory.TABLE_NAME, Table_HabitHitsHistory.COLUMN_HABIT_ID + "=?", new String[]{String.valueOf(habitId)});
	}
	
}