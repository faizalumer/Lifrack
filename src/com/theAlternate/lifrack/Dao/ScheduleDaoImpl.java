package com.theAlternate.lifrack.Dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.theAlternate.lifrack.Schedule;
import com.theAlternate.lifrack.Utilities;
import com.theAlternate.lifrack.LocalDB.Table_HabitSchedules;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ScheduleDaoImpl extends SqliteDaoBase implements IScheduleDao{
	
	public ScheduleDaoImpl(SQLiteDatabase db) {
		super(db);
	}

	@Override
	public void insert(Schedule schedule){
		ContentValues values = new ContentValues();
		if(schedule.hasValidId()) values.put(Table_HabitSchedules.COLUMN_ID, schedule.getId());
		values.put(Table_HabitSchedules.COLUMN_HABIT_ID, schedule.getHabitId()); 
		values.put(Table_HabitSchedules.COLUMN_CREATED_TIME, Utilities.getSimpleDateFormatUTC().format(schedule.getCreatedTime()));
		values.put(Table_HabitSchedules.COLUMN_DAILY_FREQUENCY, schedule.getDailyFrequency());
		values.put(Table_HabitSchedules.COLUMN_MONDAY, schedule.getMonday()?1:0);
		values.put(Table_HabitSchedules.COLUMN_TUESDAY, schedule.getTuesday()?1:0);
		values.put(Table_HabitSchedules.COLUMN_WEDNESDAY, schedule.getWednesday()?1:0);
		values.put(Table_HabitSchedules.COLUMN_THURSDAY, schedule.getThursday()?1:0);
		values.put(Table_HabitSchedules.COLUMN_FRIDAY, schedule.getFriday()?1:0);
		values.put(Table_HabitSchedules.COLUMN_SATURDAY, schedule.getSaturday()?1:0);
		values.put(Table_HabitSchedules.COLUMN_SUNDAY, schedule.getSunday()?1:0);
		values.put(Table_HabitSchedules.COLUMN_WEEK_FREQUENCY, schedule.getWeekFrequency());
		values.put(Table_HabitSchedules.COLUMN_INVALIDATED_TIME
				, schedule.isInvalidated() ? 
						Utilities.getSimpleDateFormatUTC().format(schedule.getInvalidatedTime()) : Table_HabitSchedules.NOT_INVALIDATED);

		db.insertOrThrow(Table_HabitSchedules.TABLE_NAME, null, values);
	}
	
	/*only the invalidated time can be updated for a schedule,
	 *a new record needs to be inserted to change other fields of the schedule.
	 *Since this is a service layer logic it is not implemented here in the DAO layer.
	 *Instead a standard update method is implemented 
	*/
	@Override
	public void update(Schedule schedule){
		if(!schedule.isInvalidated()) throw new IllegalStateException("The provided schedule object is not invalidated. cannot update the invalidate time.");
		
		ContentValues values = new ContentValues();
		
		values.put(Table_HabitSchedules.COLUMN_HABIT_ID, schedule.getHabitId()); 
		values.put(Table_HabitSchedules.COLUMN_CREATED_TIME, Utilities.getSimpleDateFormatUTC().format(schedule.getCreatedTime()));
		values.put(Table_HabitSchedules.COLUMN_DAILY_FREQUENCY, schedule.getDailyFrequency());
		values.put(Table_HabitSchedules.COLUMN_MONDAY, schedule.getMonday()?1:0);
		values.put(Table_HabitSchedules.COLUMN_TUESDAY, schedule.getTuesday()?1:0);
		values.put(Table_HabitSchedules.COLUMN_WEDNESDAY, schedule.getWednesday()?1:0);
		values.put(Table_HabitSchedules.COLUMN_THURSDAY, schedule.getThursday()?1:0);
		values.put(Table_HabitSchedules.COLUMN_FRIDAY, schedule.getFriday()?1:0);
		values.put(Table_HabitSchedules.COLUMN_SATURDAY, schedule.getSaturday()?1:0);
		values.put(Table_HabitSchedules.COLUMN_SUNDAY, schedule.getSunday()?1:0);
		values.put(Table_HabitSchedules.COLUMN_WEEK_FREQUENCY, schedule.getWeekFrequency());
		values.put(Table_HabitSchedules.COLUMN_INVALIDATED_TIME
				, schedule.isInvalidated() ? 
						Utilities.getSimpleDateFormatUTC().format(schedule.getInvalidatedTime()) : Table_HabitSchedules.NOT_INVALIDATED);
		

		db.update(Table_HabitSchedules.TABLE_NAME, values, Table_HabitSchedules.COLUMN_ID + "=?",new String[]{String.valueOf(schedule.getId())});
	}
	
	@Override
	public List<Schedule> getAllActiveSchedules(){
		List<Schedule> result = new ArrayList<Schedule>();
		Schedule schedule = null;
		Date createdTime;
		Date invalidatedTime;
		
		String[] columns = {
				Table_HabitSchedules.COLUMN_ID
				,Table_HabitSchedules.COLUMN_HABIT_ID
				,Table_HabitSchedules.COLUMN_DAILY_FREQUENCY
				,Table_HabitSchedules.COLUMN_MONDAY
				,Table_HabitSchedules.COLUMN_TUESDAY
				,Table_HabitSchedules.COLUMN_WEDNESDAY
				,Table_HabitSchedules.COLUMN_THURSDAY
				,Table_HabitSchedules.COLUMN_FRIDAY
				,Table_HabitSchedules.COLUMN_SATURDAY
				,Table_HabitSchedules.COLUMN_SUNDAY
				,Table_HabitSchedules.COLUMN_WEEK_FREQUENCY
				,Table_HabitSchedules.COLUMN_CREATED_TIME
				,Table_HabitSchedules.COLUMN_INVALIDATED_TIME
				,
				};
		String selection = Table_HabitSchedules.COLUMN_INVALIDATED_TIME + "=" + Table_HabitSchedules.NOT_INVALIDATED; 
		Cursor cur = db.query(Table_HabitSchedules.TABLE_NAME, columns, selection, null, null, null, null);
		
		while(cur.moveToNext()){
			createdTime = null;
			invalidatedTime = null;
			
			try {
				createdTime = Utilities.getSimpleDateFormatUTC().parse(cur.getString(11));
				if(cur.getString(12).equals(Table_HabitSchedules.NOT_INVALIDATED)) invalidatedTime = null;
				else invalidatedTime = Utilities.getSimpleDateFormatUTC().parse(cur.getString(12));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			
			schedule = new Schedule(
				cur.getLong(0)
				,cur.getLong(1)
				, cur.getInt(2)
				, cur.getInt(3) == 1? true:false
				, cur.getInt(4) == 1? true:false
				, cur.getInt(5) == 1? true:false
				, cur.getInt(6) == 1? true:false
				, cur.getInt(7) == 1? true:false
				, cur.getInt(8) == 1? true:false
				, cur.getInt(9) == 1? true:false
				, cur.getInt(10)
				, createdTime
				, invalidatedTime
				);
		
			result.add(schedule);
		
		}
		
		cur.close();
		if(result.size() == 0) return null;
		else return result;
	}
	
	@Override
	public Schedule getLatestScheduleByHabitId(long habitId){
		
		String[] columns = {
				Table_HabitSchedules.COLUMN_ID
				,Table_HabitSchedules.COLUMN_HABIT_ID
				,Table_HabitSchedules.COLUMN_DAILY_FREQUENCY
				,Table_HabitSchedules.COLUMN_MONDAY
				,Table_HabitSchedules.COLUMN_TUESDAY
				,Table_HabitSchedules.COLUMN_WEDNESDAY
				,Table_HabitSchedules.COLUMN_THURSDAY
				,Table_HabitSchedules.COLUMN_FRIDAY
				,Table_HabitSchedules.COLUMN_SATURDAY
				,Table_HabitSchedules.COLUMN_SUNDAY
				,Table_HabitSchedules.COLUMN_WEEK_FREQUENCY
				,Table_HabitSchedules.COLUMN_CREATED_TIME
				,Table_HabitSchedules.COLUMN_INVALIDATED_TIME
				};
		String selection = Table_HabitSchedules.COLUMN_HABIT_ID + "=?";
		String[] selectionArgs = {String.valueOf(habitId)};
		Cursor cur = db.query(Table_HabitSchedules.TABLE_NAME, columns, selection, selectionArgs, null, null, Table_HabitSchedules.COLUMN_ID + " desc","1");
		
		Schedule schedule = null;
		Date createdTime = null;
		Date invalidatedTime = null;
		
		if(cur.getCount() == 1){
			cur.moveToFirst();
			try {
				createdTime = Utilities.getSimpleDateFormatUTC().parse(cur.getString(11));
				if(cur.getString(12).equals(Table_HabitSchedules.NOT_INVALIDATED)) invalidatedTime = null;
				else invalidatedTime = Utilities.getSimpleDateFormatUTC().parse(cur.getString(12));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			schedule = new Schedule(
					cur.getLong(0)
					,cur.getLong(1)
					, cur.getInt(2)
					, cur.getInt(3) == 1? true:false
					, cur.getInt(4) == 1? true:false
					, cur.getInt(5) == 1? true:false
					, cur.getInt(6) == 1? true:false
					, cur.getInt(7) == 1? true:false
					, cur.getInt(8) == 1? true:false
					, cur.getInt(9) == 1? true:false
					, cur.getInt(10)
					, createdTime
					, invalidatedTime
					);
		}
		cur.close();
		return schedule;
	}
	
	@Override
	public Schedule getActiveScheduleByHabitId(long habitId){
		Date createdTime;
		Date invalidatedTime;
		
		String[] columns = {
				Table_HabitSchedules.COLUMN_ID
				,Table_HabitSchedules.COLUMN_HABIT_ID
				,Table_HabitSchedules.COLUMN_DAILY_FREQUENCY
				,Table_HabitSchedules.COLUMN_MONDAY
				,Table_HabitSchedules.COLUMN_TUESDAY
				,Table_HabitSchedules.COLUMN_WEDNESDAY
				,Table_HabitSchedules.COLUMN_THURSDAY
				,Table_HabitSchedules.COLUMN_FRIDAY
				,Table_HabitSchedules.COLUMN_SATURDAY
				,Table_HabitSchedules.COLUMN_SUNDAY
				,Table_HabitSchedules.COLUMN_WEEK_FREQUENCY
				,Table_HabitSchedules.COLUMN_CREATED_TIME
				,Table_HabitSchedules.COLUMN_INVALIDATED_TIME
				};
		String selection = Table_HabitSchedules.COLUMN_HABIT_ID + "=? AND " 
				+ Table_HabitSchedules.COLUMN_INVALIDATED_TIME + "=" + Table_HabitSchedules.NOT_INVALIDATED;
		String[] selectionArgs = {String.valueOf(habitId)};
		Cursor cur = db.query(Table_HabitSchedules.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
		
		Schedule schedule = null;
		if(cur.getCount() > 0){
			cur.moveToFirst();
			
			createdTime = null;
			invalidatedTime = null;
			
			try {
				createdTime = Utilities.getSimpleDateFormatUTC().parse(cur.getString(11));
				if(cur.getString(12).equals(Table_HabitSchedules.NOT_INVALIDATED)) invalidatedTime = null;
				else invalidatedTime = Utilities.getSimpleDateFormatUTC().parse(cur.getString(12));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			schedule = new Schedule(
					cur.getLong(0)
					,cur.getLong(1)
					, cur.getInt(2)
					, cur.getInt(3) == 1? true:false
					, cur.getInt(4) == 1? true:false
					, cur.getInt(5) == 1? true:false
					, cur.getInt(6) == 1? true:false
					, cur.getInt(7) == 1? true:false
					, cur.getInt(8) == 1? true:false
					, cur.getInt(9) == 1? true:false
					, cur.getInt(10)
					, createdTime
					, invalidatedTime
					);
		}
		cur.close();
		return schedule;
	}
	
	@Override
	public void deleteByHabitId(long habitId){
		db.delete(Table_HabitSchedules.TABLE_NAME, Table_HabitSchedules.COLUMN_HABIT_ID + "=?", new String[]{String.valueOf(habitId)});
	}
	
}
