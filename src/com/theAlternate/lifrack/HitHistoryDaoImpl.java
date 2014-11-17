package com.theAlternate.lifrack;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class HitHistoryDaoImpl implements HitHistoryDao{

SQLiteDatabase db;
	
	public HitHistoryDaoImpl(){
		db = LocalDBHelper.getInstance().getWritableDatabase();
	}
	
	/*@Override
	public long insert(Hit hit) {
		ContentValues values = new ContentValues();
		if(hit.hasValidId()) values.put(Table_HabitHits.COLUMN_ID, hit.getId());
		values.put(Table_HabitHits.COLUMN_HABIT_ID, hit.getHabitId());
		values.put(Table_HabitHits.COLUMN_HIT_TIME, Utilities.getSimpleDateFormatUTC().format(hit.getHitTime()));
		long id = db.insertOrThrow(Table_HabitHits.TABLE_NAME, null, values);
		return id;
	}*/

	@Override
	public void deleteByHabitId(long habitId) {
		db.delete(Table_HabitHitsHistory.TABLE_NAME, Table_HabitHitsHistory.COLUMN_HABIT_ID + "=?", new String[]{String.valueOf(habitId)});
	}
	
}