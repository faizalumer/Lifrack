package com.theAlternate.lifrack.Dao;

import java.text.ParseException;
import java.util.Date;

import com.theAlternate.lifrack.Habit;
import com.theAlternate.lifrack.Utilities;
import com.theAlternate.lifrack.LocalDB.Table_Habits;
import com.theAlternate.lifrack.LocalDB.Table_HabitsHistory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HabitHistoryDaoImpl extends SqliteDaoBase implements IHabitHistoryDao{

	public HabitHistoryDaoImpl(SQLiteDatabase db) {
		super(db);
	}
	
	@Override
	public Habit get(long habitId) {
		Habit result = null;
		
		Cursor cursor = db.query(Table_HabitsHistory.TABLE_NAME
				, new String[]{Table_HabitsHistory.COLUMN_NAME, Table_HabitsHistory.COLUMN_CREATED_TIME, Table_HabitsHistory.COLUMN_ARCHIVED_TIME}
				, Table_HabitsHistory.COLUMN_ID + "=?"
				, new String[]{String.valueOf(habitId)}
				, null, null, null);
		
		if(cursor.getCount() == 1){
			cursor.moveToFirst();
			Date createdTime=null;
			Date archivedTime=null;
			try {
				createdTime = Utilities.getSimpleDateFormatUTC().parse(cursor.getString(1));
				archivedTime = Utilities.getSimpleDateFormatUTC().parse(cursor.getString(2));
				result = new Habit(habitId, cursor.getString(0),createdTime,archivedTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		cursor.close();
		
		return result;
	}
	
	@Override
	public int delete(long habitId){
		int habitCount = db.delete(Table_HabitsHistory.TABLE_NAME, Table_HabitsHistory.COLUMN_ID + "=?", new String[]{String.valueOf(habitId)});
		return habitCount;
	}
	
	
	
}