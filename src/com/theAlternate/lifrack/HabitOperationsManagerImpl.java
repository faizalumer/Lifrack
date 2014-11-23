package com.theAlternate.lifrack;

import java.util.Date;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class HabitOperationsManagerImpl implements HabitOperationsManager{
	
	SQLiteDatabase db;
	
	public HabitOperationsManagerImpl(){
		db = LocalDBHelper.getInstance().getWritableDatabase();
	}
	
	@Override
	public boolean deleteHabit(long habitId){
		
		boolean isSuccess = false;
		
		db.beginTransaction();
		try {
			
			//delete hit sessions
			new HitSessionDaoImpl().deleteByHabitId(habitId);
			//db.delete(Table_HabitHitSessions.TABLE_NAME, Table_HabitHitSessions.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete hits
			new HitDaoImpl().deleteByHabitId(habitId);
			//db.delete(Table_HabitHits.TABLE_NAME, Table_HabitHits.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete targets
			new TargetDaoImpl().deleteByHabitId(habitId);
			//db.delete(Table_HabitTargets.TABLE_NAME, Table_HabitTargets.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete schedules
			new ScheduleDaoImpl().deleteByHabitId(habitId);
			//db.delete(Table_HabitSchedules.TABLE_NAME, Table_HabitSchedules.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete reminders
			new ReminderDaoImpl().deleteByHabitId(habitId);
			//db.delete(Table_HabitReminders.TABLE_NAME, Table_HabitReminders.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete habit
			int habitCount = new HabitDaoImpl().delete(habitId);
			//int habitCount = db.delete(Table_Habits.TABLE_NAME, Table_Habits.COLUMN_ID + "=?", selectArgs);
			
			if(habitCount != 1) throw new RuntimeException("delete failed for habit id " + habitId 
					+ ". The expected number of deleted rows in habit table is 1. Instead " + habitCount + "rows were deleted"
					+ "The transaction will be rolled back.");
			
			db.setTransactionSuccessful();
			isSuccess = true;
			
		} catch(Exception e){
			isSuccess = false;
			e.printStackTrace();
		}finally {
			db.endTransaction();
		}
		
		return isSuccess;
	}
	
	@Override
	public boolean archiveHabit(long habitId){
		boolean isSuccess = false;
		String sql = null;
		String[] bindArgs = {String.valueOf(habitId)};
		db.beginTransaction();
		try {
			//insert habit into history table
			sql = "INSERT INTO " + Table_HabitsHistory.TABLE_NAME
					+ "(" + Table_HabitsHistory.COLUMN_ID
					+ ", " + Table_HabitsHistory.COLUMN_NAME
					+ ", " + Table_HabitsHistory.COLUMN_CREATED_TIME
					+ ", " + Table_HabitsHistory.COLUMN_ARCHIVED_TIME
					+ ")"
					+ " SELECT "
					+ Table_Habits.COLUMN_ID
					+ ", " + Table_Habits.COLUMN_NAME
					+ ", " + Table_Habits.COLUMN_CREATED_TIME
					+ ", '" + Utilities.getCurrentTimeFormattedUTC() + "'"
					+ " FROM " + Table_Habits.TABLE_NAME
					+ " WHERE " + Table_Habits.COLUMN_ID + "=?"
					;
			db.execSQL(sql, bindArgs);
			
			//insert hits into history table
			sql = "INSERT INTO " + Table_HabitHitsHistory.TABLE_NAME
					+ "(" + Table_HabitHitsHistory.COLUMN_ID
					+ ", " + Table_HabitHitsHistory.COLUMN_HABIT_ID
					+ ", " + Table_HabitHitsHistory.COLUMN_HIT_TIME
					+ ")"
					+ " SELECT "
					+ Table_HabitHits.COLUMN_ID
					+ ", " + Table_HabitHits.COLUMN_HABIT_ID
					+ ", " + Table_HabitHits.COLUMN_HIT_TIME
					+ " FROM " + Table_HabitHits.TABLE_NAME
					+ " WHERE " + Table_HabitHits.COLUMN_HABIT_ID + "=?"
					;
			db.execSQL(sql, bindArgs);
			
			//insert hit sessions into history table
			sql = "INSERT INTO " + Table_HabitHitSessionsHistory.TABLE_NAME
					+ "(" + Table_HabitHitSessionsHistory.COLUMN_ID
					+ ", " + Table_HabitHitSessionsHistory.COLUMN_HIT_ID
					+ ", " + Table_HabitHitSessionsHistory.COLUMN_HABIT_ID
					+ ", " + Table_HabitHitSessionsHistory.COLUMN_START_TIME
					+ ", " + Table_HabitHitSessionsHistory.COLUMN_END_TIME
					+ ")"
					+ " SELECT "
					+ Table_HabitHitSessions.COLUMN_ID
					+ ", " + Table_HabitHitSessions.COLUMN_HIT_ID
					+ ", " + Table_HabitHitSessions.COLUMN_HABIT_ID
					+ ", " + Table_HabitHitSessions.COLUMN_START_TIME
					+ ", " + Table_HabitHitSessions.COLUMN_END_TIME
					+ " FROM " + Table_HabitHitSessions.TABLE_NAME
					+ " WHERE " + Table_HabitHitSessions.COLUMN_HABIT_ID + "=?"
					;
			db.execSQL(sql, bindArgs);
			
			//insert targets into history table
			sql = "INSERT INTO " + Table_HabitTargetsHistory.TABLE_NAME
					+ "(" + Table_HabitTargetsHistory.COLUMN_ID
					+ ", " + Table_HabitTargetsHistory.COLUMN_HABIT_ID
					+ ", " + Table_HabitTargetsHistory.COLUMN_DESCRIPTION
					+ ", " + Table_HabitTargetsHistory.COLUMN_CREATED_TIME
					+ ", " + Table_HabitTargetsHistory.COLUMN_ACHIEVED_TIME
					+ ")"
					+ " SELECT "
					+ Table_HabitTargets.COLUMN_ID
					+ ", " + Table_HabitTargets.COLUMN_HABIT_ID
					+ ", " + Table_HabitTargets.COLUMN_DESCRIPTION
					+ ", " + Table_HabitTargets.COLUMN_CREATED_TIME
					+ ", " + Table_HabitTargets.COLUMN_ACHIEVED_TIME
					+ " FROM " + Table_HabitTargets.TABLE_NAME
					+ " WHERE " + Table_HabitTargets.COLUMN_HABIT_ID + "=?"
					;
			db.execSQL(sql, bindArgs);
			
			//insert schedule into history table
			sql = "INSERT INTO " + Table_HabitSchedulesHistory.TABLE_NAME
					+ "(" + Table_HabitSchedulesHistory.COLUMN_ID
					+ ", " + Table_HabitSchedulesHistory.COLUMN_HABIT_ID
					+ ", " + Table_HabitSchedulesHistory.COLUMN_DAILY_FREQUENCY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_MONDAY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_TUESDAY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_WEDNESDAY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_THURSDAY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_FRIDAY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_SATURDAY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_SUNDAY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_WEEK_FREQUENCY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_CREATED_TIME
					+ ", " + Table_HabitSchedulesHistory.COLUMN_INVALIDATED_TIME
					+ ")"
					+ " SELECT "
					+ Table_HabitSchedules.COLUMN_ID
					+ ", " + Table_HabitSchedules.COLUMN_HABIT_ID
					+ ", " + Table_HabitSchedules.COLUMN_DAILY_FREQUENCY
					+ ", " + Table_HabitSchedules.COLUMN_MONDAY
					+ ", " + Table_HabitSchedules.COLUMN_TUESDAY
					+ ", " + Table_HabitSchedules.COLUMN_WEDNESDAY
					+ ", " + Table_HabitSchedules.COLUMN_THURSDAY
					+ ", " + Table_HabitSchedules.COLUMN_FRIDAY
					+ ", " + Table_HabitSchedules.COLUMN_SATURDAY
					+ ", " + Table_HabitSchedules.COLUMN_SUNDAY
					+ ", " + Table_HabitSchedules.COLUMN_WEEK_FREQUENCY
					+ ", " + Table_HabitSchedules.COLUMN_CREATED_TIME
					+ ", " + Table_HabitSchedules.COLUMN_INVALIDATED_TIME
					+ " FROM " + Table_HabitSchedules.TABLE_NAME
					+ " WHERE " + Table_HabitSchedules.COLUMN_HABIT_ID + "=?"
					;
			db.execSQL(sql, bindArgs);
			
			//delete hit sessions
			new HitSessionDaoImpl().deleteByHabitId(habitId);
			
			//delete hits
			new HitDaoImpl().deleteByHabitId(habitId);
			
			//delete targets
			new TargetDaoImpl().deleteByHabitId(habitId);
			
			//delete schedule
			new ScheduleDaoImpl().deleteByHabitId(habitId);
			
			//delete reminders(there is no history table for reminders, so doing only deletion)
			new ReminderDaoImpl().deleteByHabitId(habitId);
			
			//delete habit
			int habitCount = new HabitDaoImpl().delete(habitId);
			
			if(habitCount != 1) throw new RuntimeException("archive failed for habit id " + habitId 
					+ ". The expected number of deleted rows in habit table is 1. Instead " + habitCount + "rows were deleted"
					);
			
			db.setTransactionSuccessful();
			isSuccess = true;
		} catch(Exception e){
			isSuccess = false;
			e.printStackTrace();
		}finally {
			db.endTransaction();
		}
		return isSuccess;
	}
	
	@Override
	public boolean deleteArchivedHabit(long habitId){
		
		boolean isSuccess = false;
		
		db.beginTransaction();
		try {
			
			//delete hit sessions
			new HitSessionHistoryDaoImpl().deleteByHabitId(habitId);
			//db.delete(Table_HabitHitSessions.TABLE_NAME, Table_HabitHitSessions.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete hits
			new HitHistoryDaoImpl().deleteByHabitId(habitId);
			//db.delete(Table_HabitHits.TABLE_NAME, Table_HabitHits.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete targets
			new TargetHistoryDaoImpl().deleteByHabitId(habitId);
			//db.delete(Table_HabitTargets.TABLE_NAME, Table_HabitTargets.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete schedules
			new ScheduleHistoryDaoImpl().deleteByHabitId(habitId);
			//db.delete(Table_HabitSchedules.TABLE_NAME, Table_HabitSchedules.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete reminders
			//new ReminderDaoImpl().deleteByHabitId(habitId);
			//db.delete(Table_HabitReminders.TABLE_NAME, Table_HabitReminders.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete habit
			int habitCount = new HabitHistoryDaoImpl().delete(habitId);
			//int habitCount = db.delete(Table_Habits.TABLE_NAME, Table_Habits.COLUMN_ID + "=?", selectArgs);
			
			if(habitCount != 1) throw new RuntimeException("delete failed for archived habit id " + habitId 
					+ ". The expected number of deleted rows in habit table is 1. Instead " + habitCount + "rows were deleted"
					+ "The transaction will be rolled back.");
			
			db.setTransactionSuccessful();
			isSuccess = true;
			
		} catch(Exception e){
			isSuccess = false;
			e.printStackTrace();
		}finally {
			db.endTransaction();
		}
		
		return isSuccess;
	}
	
	public boolean unarchiveHabit(long habitId){
		
		boolean isSuccess = false;
		String sql = null;
		String[] bindArgs = {String.valueOf(habitId)};
		db.beginTransaction();
		try {
			
			//insert habit
			sql = "INSERT INTO " + Table_Habits.TABLE_NAME
					+ "(" + Table_Habits.COLUMN_ID
					+ ", " + Table_Habits.COLUMN_NAME
					+ ", " + Table_Habits.COLUMN_CREATED_TIME
					+ ")"
					+ " SELECT "
					+ Table_HabitsHistory.COLUMN_ID
					+ ", " + Table_HabitsHistory.COLUMN_NAME
					+ ", " + Table_HabitsHistory.COLUMN_CREATED_TIME
					+ " FROM " + Table_HabitsHistory.TABLE_NAME
					+ " WHERE " + Table_HabitsHistory.COLUMN_ID + "=?"
					;
			db.execSQL(sql, bindArgs);
			
			//insert hits
			sql = "INSERT INTO " + Table_HabitHits.TABLE_NAME
					+ "(" + Table_HabitHits.COLUMN_ID
					+ ", " + Table_HabitHits.COLUMN_HABIT_ID
					+ ", " + Table_HabitHits.COLUMN_HIT_TIME
					+ ")"
					+ " SELECT "
					+ Table_HabitHitsHistory.COLUMN_ID
					+ ", " + Table_HabitHitsHistory.COLUMN_HABIT_ID
					+ ", " + Table_HabitHitsHistory.COLUMN_HIT_TIME
					+ " FROM " + Table_HabitHitsHistory.TABLE_NAME
					+ " WHERE " + Table_HabitHitsHistory.COLUMN_HABIT_ID + "=?"
					;
			db.execSQL(sql, bindArgs);
			
			//insert hit sessions
			sql = "INSERT INTO " + Table_HabitHitSessions.TABLE_NAME
					+ "(" + Table_HabitHitSessions.COLUMN_ID
					+ ", " + Table_HabitHitSessions.COLUMN_HIT_ID
					+ ", " + Table_HabitHitSessions.COLUMN_HABIT_ID
					+ ", " + Table_HabitHitSessions.COLUMN_START_TIME
					+ ", " + Table_HabitHitSessions.COLUMN_END_TIME
					+ ")"
					+ " SELECT "
					+ Table_HabitHitSessionsHistory.COLUMN_ID
					+ ", " + Table_HabitHitSessionsHistory.COLUMN_HIT_ID
					+ ", " + Table_HabitHitSessionsHistory.COLUMN_HABIT_ID
					+ ", " + Table_HabitHitSessionsHistory.COLUMN_START_TIME
					+ ", " + Table_HabitHitSessionsHistory.COLUMN_END_TIME
					+ " FROM " + Table_HabitHitSessionsHistory.TABLE_NAME
					+ " WHERE " + Table_HabitHitSessionsHistory.COLUMN_HABIT_ID + "=?"
					;
			db.execSQL(sql, bindArgs);
			
			//insert targets
			sql = "INSERT INTO " + Table_HabitTargets.TABLE_NAME
					+ "(" + Table_HabitTargets.COLUMN_ID
					+ ", " + Table_HabitTargets.COLUMN_HABIT_ID
					+ ", " + Table_HabitTargets.COLUMN_DESCRIPTION
					+ ", " + Table_HabitTargets.COLUMN_CREATED_TIME
					+ ", " + Table_HabitTargets.COLUMN_ACHIEVED_TIME
					+ ")"
					+ " SELECT "
					+ Table_HabitTargetsHistory.COLUMN_ID
					+ ", " + Table_HabitTargetsHistory.COLUMN_HABIT_ID
					+ ", " + Table_HabitTargetsHistory.COLUMN_DESCRIPTION
					+ ", " + Table_HabitTargetsHistory.COLUMN_CREATED_TIME
					+ ", " + Table_HabitTargetsHistory.COLUMN_ACHIEVED_TIME
					+ " FROM " + Table_HabitTargetsHistory.TABLE_NAME
					+ " WHERE " + Table_HabitTargetsHistory.COLUMN_HABIT_ID + "=?"
					;
			db.execSQL(sql, bindArgs);
			
			//insert schedule
			sql = "INSERT INTO " + Table_HabitSchedules.TABLE_NAME
					+ "(" + Table_HabitSchedules.COLUMN_ID
					+ ", " + Table_HabitSchedules.COLUMN_HABIT_ID
					+ ", " + Table_HabitSchedules.COLUMN_DAILY_FREQUENCY
					+ ", " + Table_HabitSchedules.COLUMN_MONDAY
					+ ", " + Table_HabitSchedules.COLUMN_TUESDAY
					+ ", " + Table_HabitSchedules.COLUMN_WEDNESDAY
					+ ", " + Table_HabitSchedules.COLUMN_THURSDAY
					+ ", " + Table_HabitSchedules.COLUMN_FRIDAY
					+ ", " + Table_HabitSchedules.COLUMN_SATURDAY
					+ ", " + Table_HabitSchedules.COLUMN_SUNDAY
					+ ", " + Table_HabitSchedules.COLUMN_WEEK_FREQUENCY
					+ ", " + Table_HabitSchedules.COLUMN_CREATED_TIME
					+ ", " + Table_HabitSchedules.COLUMN_INVALIDATED_TIME
					+ ")"
					+ " SELECT "
					+ Table_HabitSchedulesHistory.COLUMN_ID
					+ ", " + Table_HabitSchedulesHistory.COLUMN_HABIT_ID
					+ ", " + Table_HabitSchedulesHistory.COLUMN_DAILY_FREQUENCY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_MONDAY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_TUESDAY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_WEDNESDAY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_THURSDAY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_FRIDAY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_SATURDAY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_SUNDAY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_WEEK_FREQUENCY
					+ ", " + Table_HabitSchedulesHistory.COLUMN_CREATED_TIME
					+ ", " + Table_HabitSchedulesHistory.COLUMN_INVALIDATED_TIME
					+ " FROM " + Table_HabitSchedulesHistory.TABLE_NAME
					+ " WHERE " + Table_HabitSchedulesHistory.COLUMN_HABIT_ID + "=?"
					;
			db.execSQL(sql, bindArgs);
			
			//delete hit sessions
			new HitSessionHistoryDaoImpl().deleteByHabitId(habitId);
			
			//delete hits
			new HitHistoryDaoImpl().deleteByHabitId(habitId);
			
			//delete targets
			new TargetHistoryDaoImpl().deleteByHabitId(habitId);
			
			//delete schedule
			new ScheduleHistoryDaoImpl().deleteByHabitId(habitId);
			
			//delete habit
			int habitCount = new HabitHistoryDaoImpl().delete(habitId);
			
			if(habitCount != 1) throw new RuntimeException("unarchive failed for habit id " + habitId 
					+ ". The expected number of deleted rows in habit table is 1. Instead " + habitCount + "rows were deleted"
					);
			
			db.setTransactionSuccessful();
			isSuccess = true;
		} catch(Exception e){
			e.printStackTrace();
			isSuccess = false;
		}finally {
			db.endTransaction();
		}
		return isSuccess;
	}
	
	@Override
	public boolean achieveTarget(long targetId){
		ContentValues values = new ContentValues();
		values.put(Table_HabitTargets.COLUMN_ACHIEVED_TIME, Utilities.getSimpleDateFormatUTC().format(new Date()));
		int count = db.update(Table_HabitTargets.TABLE_NAME, values, Table_HabitTargets.COLUMN_ID + "=?", new String[]{String.valueOf(targetId)});
		if(count == 0) return false;
		else return true;
	}
	
	@Override
	public boolean unachieveTarget(long targetId){
		ContentValues values = new ContentValues();
		values.putNull(Table_HabitTargets.COLUMN_ACHIEVED_TIME);
		int count = db.update(Table_HabitTargets.TABLE_NAME, values, Table_HabitTargets.COLUMN_ID + "=?", new String[]{String.valueOf(targetId)});
		if(count == 0) return false;
		else return true;
	}
	
	
}