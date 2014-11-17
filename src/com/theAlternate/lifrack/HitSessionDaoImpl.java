package com.theAlternate.lifrack;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class HitSessionDaoImpl implements HitSessionDao{

SQLiteDatabase db;
	
	public HitSessionDaoImpl(){
		db = LocalDBHelper.getInstance().getWritableDatabase();
	}
	
	@Override
	public void insert(HitSession hitSession) {
		ContentValues values = new ContentValues();
		if(hitSession.hasValidId()) values.put(Table_HabitHitSessions.COLUMN_ID, hitSession.getId());
		if(hitSession.getHitId() != null) values.put(Table_HabitHitSessions.COLUMN_HIT_ID, hitSession.getHitId());
		values.put(Table_HabitHitSessions.COLUMN_HABIT_ID, hitSession.getHabitId());
		values.put(Table_HabitHitSessions.COLUMN_START_TIME, Utilities.getSimpleDateFormatUTC().format(hitSession.getStartTime()));
		if(hitSession.hasEnded())values.put(Table_HabitHitSessions.COLUMN_END_TIME, Utilities.getSimpleDateFormatUTC().format(hitSession.getEndTime()));
		db.insertOrThrow(Table_HabitHitSessions.TABLE_NAME, null, values);
		
	}

	@Override
	public void delete(long hitSessionId) {
		db.delete(Table_HabitHitSessions.TABLE_NAME, Table_HabitHitSessions.COLUMN_ID + "=?", new String[]{String.valueOf(hitSessionId)});
		
	}
	
	@Override
	public void deleteByHabitId(long habitId) {
		db.delete(Table_HabitHitSessions.TABLE_NAME, Table_HabitHitSessions.COLUMN_HABIT_ID + "=?", new String[]{String.valueOf(habitId)});
		
	}

	@Override
	public void update(HitSession hitSession) {
		ContentValues values = new ContentValues();
		if(hitSession.getHitId() != null) values.put(Table_HabitHitSessions.COLUMN_HIT_ID, hitSession.getHitId());
		values.put(Table_HabitHitSessions.COLUMN_HABIT_ID, hitSession.getHabitId());
		values.put(Table_HabitHitSessions.COLUMN_START_TIME, Utilities.getSimpleDateFormatUTC().format(hitSession.getStartTime()));
		values.put(Table_HabitHitSessions.COLUMN_END_TIME, Utilities.getSimpleDateFormatUTC().format(hitSession.getEndTime()));
		db.update(Table_HabitHitSessions.TABLE_NAME, values, Table_HabitHitSessions.COLUMN_ID + "=?",new String[]{String.valueOf(hitSession.getId())});
	}

}