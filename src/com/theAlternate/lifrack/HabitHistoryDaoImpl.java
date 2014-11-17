package com.theAlternate.lifrack;

import java.text.ParseException;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class HabitHistoryDaoImpl implements HabitHistoryDao{

	SQLiteDatabase db;
	
	public HabitHistoryDaoImpl(){
		db = LocalDBHelper.getInstance().getWritableDatabase();
	}
	
	/*@Override
	public long insert(Habit habit) {
		ContentValues values = new ContentValues();
		if(habit.hasValidId()) values.put(Table_Habits.COLUMN_ID, habit.getId());
		values.put(Table_Habits.COLUMN_NAME, habit.getName());
		values.put(Table_Habits.COLUMN_CREATED_TIME, Utilities.getSimpleDateFormatUTC().format(habit.getCreatedTime()));
		long habitId = db.insertOrThrow(Table_Habits.TABLE_NAME, null, values);
		return habitId;
	}
	
	@Override
	public Habit get(long habitId) {
		Habit result = null;
		
		Cursor cursor = db.query(Table_Habits.TABLE_NAME
				, new String[]{Table_Habits.COLUMN_NAME, Table_Habits.COLUMN_CREATED_TIME}
				, Table_Habits.COLUMN_ID + "=?"
				, new String[]{String.valueOf(habitId)}
				, null, null, null);
		
		if(cursor.getCount() == 1){
			cursor.moveToFirst();
			Date createdTime=null;
			try {
				createdTime = Utilities.getSimpleDateFormatUTC().parse(cursor.getString(1));
				result = new Habit(habitId, cursor.getString(0),createdTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		cursor.close();
		
		return result;
	}
	
	@Override
	public void update(Habit habit){
		ContentValues values = new ContentValues();
		values.put(Table_Habits.COLUMN_NAME,habit.getName());
		
		db.update(Table_Habits.TABLE_NAME, values, Table_Habits.COLUMN_ID + "= ?", new String[]{String.valueOf(habit.getId())});
	}*/
	
	@Override
	public int delete(long habitId){
		int habitCount = db.delete(Table_HabitsHistory.TABLE_NAME, Table_HabitsHistory.COLUMN_ID + "=?", new String[]{String.valueOf(habitId)});
		return habitCount;
	}
	
	
	
}