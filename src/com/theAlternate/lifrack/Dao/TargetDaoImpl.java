package com.theAlternate.lifrack.Dao;

import java.util.ArrayList;
import java.util.List;

import com.theAlternate.lifrack.Target;
import com.theAlternate.lifrack.Utilities;
import com.theAlternate.lifrack.LocalDB.Table_HabitTargets;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TargetDaoImpl extends SqliteDaoBase implements ITargetDao{
	public TargetDaoImpl(SQLiteDatabase db) {
		super(db);
	}

	private static final String LOG_TAG = "TargetDaoImpl";


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
	public void update(Target target){
		ContentValues values = new ContentValues();
		values.put(Table_HabitTargets.COLUMN_DESCRIPTION, target.getDescription());
		if(target.isAchieved()) values.put(Table_HabitTargets.COLUMN_ACHIEVED_TIME, Utilities.getSimpleDateFormatUTC().format(target.getAchievedTime()));

		db.update(Table_HabitTargets.TABLE_NAME, values
				, Table_HabitTargets.COLUMN_ID + "=?" 
				, new String[] {String.valueOf(target.getId())});

		
	}
}