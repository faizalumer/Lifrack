package com.theAlternate.lifrack;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TargetDaoImpl implements TargetDao{
	private static final String LOG_TAG = "TargetDaoImpl";
	SQLiteDatabase db;

	public TargetDaoImpl(){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"TargetDaoImpl()");}
		db = LocalDBHelper.getInstance().getWritableDatabase();
	}

	@Override
	public void insert(Target target) {
		
		ContentValues values = new ContentValues();
		values.put(Table_HabitTargets.COLUMN_HABIT_ID, target.gethabitId()); 
		values.put(Table_HabitTargets.COLUMN_CREATED_TIME, Utilities.getSimpleDateFormatUTC().format(target.getCreatedTime()));
		values.put(Table_HabitTargets.COLUMN_DESCRIPTION, target.getDescription());
		db.insertOrThrow(Table_HabitTargets.TABLE_NAME, null, values);
		
	}

	@Override
	public void delete(long targetId) {
		
		db.delete(Table_HabitTargets.TABLE_NAME
				, Table_HabitTargets.COLUMN_ID + "=?" 
				, new String[] {String.valueOf(targetId)});
		
	}
	
	@Override
	public void deleteByHabitId(long habitId) {
		
		db.delete(Table_HabitTargets.TABLE_NAME
				, Table_HabitTargets.COLUMN_HABIT_ID + "=?" 
				, new String[] {String.valueOf(habitId)});
		
	}

	@Override
	public void updateDescription(Target target) {
		
		ContentValues values = new ContentValues();
		values.put(Table_HabitTargets.COLUMN_DESCRIPTION, target.getDescription());

		db.update(Table_HabitTargets.TABLE_NAME, values
				, Table_HabitTargets.COLUMN_ID + "=?" 
				, new String[] {String.valueOf(target.getId())});
		
	}
	
	@Override
	public void updateAchievedTime(Target target) {
		
		ContentValues values = new ContentValues();
		values.put(Table_HabitTargets.COLUMN_ACHIEVED_TIME, Utilities.getSimpleDateFormatUTC().format(target.getAchievedTime()));

		db.update(Table_HabitTargets.TABLE_NAME, values
				, Table_HabitTargets.COLUMN_ID + "=?" 
				, new String[] {String.valueOf(target.getId())});
		
	}

	@Override
	public void getAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void get(long targetId) {
		// TODO Auto-generated method stub
		
	}
	
}
/*
private static final String LOG_TAG = "ReminderDAO";
SQLiteDatabase db;

public ReminderDaoImpl(){
	if(BuildConfig.DEBUG){Log.d(LOG_TAG,"ReminderDaoImpl()");}
	
	//create a new db connection if it was not passed in as parameter(i.e. null was passed in)
	if(existingDB == null){
		db = LocalDBHelper.getInstance().getWritableDatabase();
	}
	else db = existingDB;
	db = LocalDBHelper.getInstance().getWritableDatabase();
}

//close the db connection if it was not passed in as parameter
private void close(){
	if(BuildConfig.DEBUG){Log.d(LOG_TAG,"close()");}
	if(localDBHelper != null){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"close() inside");}
		localDBHelper.close();
	}
}

@Override
public void delete(long reminderId) {
	if(BuildConfig.DEBUG){Log.d(LOG_TAG,"delete()");}
	
	db.delete(Table_HabitReminders.TABLE_NAME, Table_HabitReminders.COLUMN_ID + "=?", new String[] {String.valueOf(reminderId)});
	
	//close();
}

@Override
public void update(Reminder reminder){
	if(BuildConfig.DEBUG){Log.d(LOG_TAG,"update()");}
	if(!reminder.hasValidId()){
		throw new IllegalStateException("This reminder does not have a valid Id. Cannot update.");
	}
	
	ContentValues values = new ContentValues();
	values.put(Table_HabitReminders.COLUMN_REMINDER_TIME, reminder.getFormattedTime());
	db.update(Table_HabitReminders.TABLE_NAME, values, Table_HabitReminders.COLUMN_ID + "=?", new String[] {String.valueOf(reminder.getId())});
	
	//close();
	
}

@Override
public void insert(Reminder reminder){
	if(BuildConfig.DEBUG){Log.d(LOG_TAG,"insert()");}
	if(reminder.hasValidId()){
		throw new IllegalStateException("This reminder already has a valid Id. Cannot insert.");
	}
	
	if(!reminder.hasValidHabitId()){
		throw new IllegalStateException("This reminder does not have a valid habit Id. Cannot insert.");
	}
	
	if(!reminder.hasTime()){
		throw new IllegalStateException("This reminder does not have a valid time. Cannot insert.");
	}
	
	ContentValues values = new ContentValues();
	values.put(Table_HabitReminders.COLUMN_REMINDER_TIME, reminder.getFormattedTime());
	values.put(Table_HabitReminders.COLUMN_HABIT_ID, reminder.getHabitId());
	values.put(Table_HabitReminders.COLUMN_CREATED_TIME, Utilities.getCurrentTimeFormattedUTC());
	
	db.insertOrThrow(Table_HabitReminders.TABLE_NAME, null, values);
	
	//close();
	
}

@Override
public Reminder get(long reminderId) {
	if(BuildConfig.DEBUG){Log.d(LOG_TAG,"get()");}
	
	Reminder result = null; 
	
	Cursor cursor = db.query(Table_HabitReminders.TABLE_NAME
			, new String[]{Table_HabitReminders.COLUMN_ID, Table_HabitReminders.COLUMN_HABIT_ID, Table_HabitReminders.COLUMN_REMINDER_TIME}
			, Table_HabitReminders.COLUMN_ID + "=?"
			, new String[]{String.valueOf(reminderId)}
			, null, null, null);
	
	if(cursor.getCount() == 1){
		cursor.moveToFirst();
		result = new Reminder(cursor.getLong(0),cursor.getLong(1),cursor.getString(2));
	}
	
	cursor.close();
	
	//close();

	return result;
}

Function to return list of reminders that are active.
 * A reminder is active if the habit record has isScheduleEnabled = TRUE.
 * 
public List<Reminder> getAllActiveReminders(){
	if(BuildConfig.DEBUG){Log.d(LOG_TAG,"getAllActiveReminders()");}
	List<Reminder> result = new ArrayList<Reminder>();
	Reminder reminder = null;
	
	//select cols from reminder join habits on habitid and isscheduleEnabled = 1
	String sql = "SELECT "
			+ "a." + Table_HabitReminders.COLUMN_ID
			+ ",a." + Table_HabitReminders.COLUMN_HABIT_ID
			+ ",a." + Table_HabitReminders.COLUMN_REMINDER_TIME
			+ " FROM " + Table_HabitReminders.TABLE_NAME + " a"
			+ " JOIN " + Table_Habits.TABLE_NAME + " b"
			+ " ON a." + Table_HabitReminders.COLUMN_HABIT_ID + " = b." + Table_Habits.COLUMN_ID
			+ " AND b." + Table_Habits.COLUMN_IS_SCHEDULE_ENABLED + "=1";
	
	Cursor cursor = db.rawQuery(sql, null);
	
	while(cursor.moveToNext()){
		reminder = new Reminder(cursor.getLong(0),cursor.getLong(1),cursor.getString(2));
		result.add(reminder);
	}
	
	cursor.close();
	//close();
	return result;
}

Function to return count of reminders that are active.
 * A reminder is active if the habit record has isScheduleEnabled = TRUE.
 * 
public int getAllActiveReminderCount(){
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
}
*/