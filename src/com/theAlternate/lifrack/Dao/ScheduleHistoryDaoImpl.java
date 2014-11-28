package com.theAlternate.lifrack.Dao;

import com.theAlternate.lifrack.LocalDB.Table_HabitSchedulesHistory;

import android.database.sqlite.SQLiteDatabase;

public class ScheduleHistoryDaoImpl extends SqliteDaoBase implements IScheduleHistoryDao{

	public ScheduleHistoryDaoImpl(SQLiteDatabase db) {
		super(db);
	}
	
	@Override
	public void deleteByHabitId(long habitId){
		db.delete(Table_HabitSchedulesHistory.TABLE_NAME, Table_HabitSchedulesHistory.COLUMN_HABIT_ID + "=?", new String[]{String.valueOf(habitId)});
	}
}
