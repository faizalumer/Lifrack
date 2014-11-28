package com.theAlternate.lifrack.Dao;

import java.util.ArrayList;
import java.util.List;

import com.theAlternate.lifrack.BuildConfig;
import com.theAlternate.lifrack.Reminder;
import com.theAlternate.lifrack.LocalDB.Table_HabitReminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ReminderDaoImpl extends SqliteDaoBase implements IReminderDao{

	public ReminderDaoImpl(SQLiteDatabase db) {
		super(db);
	}

	private static final String LOG_TAG = "ReminderDAO";
	
	
	@Override
	public void delete(long reminderId) {
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"delete()");}
		db.delete(Table_HabitReminders.TABLE_NAME, Table_HabitReminders.COLUMN_ID + "=?", new String[] {String.valueOf(reminderId)});
	}
	
	@Override
	public void deleteByHabitId(long habitId) {
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"deleteByHabitId()");}
		db.delete(Table_HabitReminders.TABLE_NAME, Table_HabitReminders.COLUMN_HABIT_ID + "=?", new String[] {String.valueOf(habitId)});
	}

	@Override
	public void update(Reminder reminder){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"update()");}
		ContentValues values = new ContentValues();
		values.put(Table_HabitReminders.COLUMN_HOUR, reminder.getHour());
		values.put(Table_HabitReminders.COLUMN_MINUTE, reminder.getMinute());
		values.put(Table_HabitReminders.COLUMN_HABIT_ID, reminder.getHabitId());
		db.update(Table_HabitReminders.TABLE_NAME, values, Table_HabitReminders.COLUMN_ID + "=?", new String[] {String.valueOf(reminder.getId())});
		
	}

	@Override
	public void insert(Reminder reminder){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"insert()");}
		
		ContentValues values = new ContentValues();
		if(reminder.hasValidId()) values.put(Table_HabitReminders.COLUMN_ID, reminder.getId());
		values.put(Table_HabitReminders.COLUMN_HOUR, reminder.getHour());
		values.put(Table_HabitReminders.COLUMN_MINUTE, reminder.getMinute());
		values.put(Table_HabitReminders.COLUMN_HABIT_ID, reminder.getHabitId());
		
		db.insertOrThrow(Table_HabitReminders.TABLE_NAME, null, values);
		
	}

	@Override
	public Reminder get(long reminderId) {
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"get()");}
		
		Reminder result = null; 
		
		Cursor cursor = db.query(Table_HabitReminders.TABLE_NAME
				, new String[]{Table_HabitReminders.COLUMN_ID, Table_HabitReminders.COLUMN_HABIT_ID, Table_HabitReminders.COLUMN_HOUR, Table_HabitReminders.COLUMN_MINUTE}
				, Table_HabitReminders.COLUMN_ID + "=?"
				, new String[]{String.valueOf(reminderId)}
				, null, null, null);
		
		if(cursor.getCount() == 1){
			cursor.moveToFirst();
			result = new Reminder(cursor.getLong(0),cursor.getLong(1),cursor.getInt(2),cursor.getInt(3));
		}
		
		cursor.close();
		
		return result;
	}

	/*Function to return list of reminders
	 * */
	public List<Reminder> getAllReminders(){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"getAllActiveReminders()");}
		List<Reminder> result = new ArrayList<Reminder>();
		Reminder reminder = null;
		
		Cursor cursor = db.query(Table_HabitReminders.TABLE_NAME
				, new String[]{Table_HabitReminders.COLUMN_ID, Table_HabitReminders.COLUMN_HABIT_ID, Table_HabitReminders.COLUMN_HOUR, Table_HabitReminders.COLUMN_MINUTE}
				, null, null, null, null, null);
		
		while(cursor.moveToNext()){
			reminder = new Reminder(cursor.getLong(0),cursor.getLong(1),cursor.getInt(2),cursor.getInt(3));
			result.add(reminder);
		}
		
		cursor.close();
		if(result.size() == 0) return null;
		else return result;
	}
	
	/*Function to return list of all reminders for a provided habitId
	 * */
	public List<Reminder> getAllRemindersByHabitId(long habitId){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"getAllActiveRemindersByHabitId()");}
		List<Reminder> result = new ArrayList<Reminder>();
		Reminder reminder = null;
		
		Cursor cursor = db.query(Table_HabitReminders.TABLE_NAME
				, new String[]{Table_HabitReminders.COLUMN_ID, Table_HabitReminders.COLUMN_HABIT_ID, Table_HabitReminders.COLUMN_HOUR, Table_HabitReminders.COLUMN_MINUTE}
				, Table_HabitReminders.COLUMN_HABIT_ID + " = ?"
				, new String[]{String.valueOf(habitId)}
				, null, null, null);
		
		while(cursor.moveToNext()){
			reminder = new Reminder(cursor.getLong(0),cursor.getLong(1),cursor.getInt(2),cursor.getInt(3));
			result.add(reminder);
		}
		
		cursor.close();
		if(result.size() == 0) return null; 
		else return result;
	}
	
	/*Function to return count of reminders that are active.
	 * A reminder is active if the habit record has isScheduleEnabled = TRUE.
	 * */
	/*public int getAllActiveReminderCount(){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"getAllActiveReminders()");}
		int result = 0;
		
		//select cols from reminder join habits on habitid and isscheduleEnabled = 1
		String sql = "SELECT count(1)"
				+ " FROM " + Table_HabitReminders.TABLE_NAME + " a"
				+ " JOIN " + Table_Habits.TABLE_NAME + " b"
				+ " ON a." + Table_HabitReminders.COLUMN_HABIT_ID + " = b." + Table_Habits.COLUMN_ID
				+ " AND b." + Table_Habits.COLUMN_IS_SCHEDULE_ENABLED + "=1";
		
		Cursor cursor = db.rawQuery(sql, null);
		
		cursor.moveToFirst();
		result = cursor.getInt(0);
		
		cursor.close();
		//close();
		return result;
	}*/
	
}