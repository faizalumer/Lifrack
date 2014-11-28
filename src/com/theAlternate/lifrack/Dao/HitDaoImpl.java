package com.theAlternate.lifrack.Dao;

import java.text.ParseException;
import java.util.Date;

import com.theAlternate.lifrack.Hit;
import com.theAlternate.lifrack.Utilities;
import com.theAlternate.lifrack.LocalDB.Table_HabitHits;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HitDaoImpl extends SqliteDaoBase implements IHitDao{
	
	public HitDaoImpl(SQLiteDatabase db) {
		super(db);
	}

	@Override
	public long insert(Hit hit) {
		ContentValues values = new ContentValues();
		if(hit.hasValidId()) values.put(Table_HabitHits.COLUMN_ID, hit.getId());
		values.put(Table_HabitHits.COLUMN_HABIT_ID, hit.getHabitId());
		values.put(Table_HabitHits.COLUMN_HIT_TIME, Utilities.getSimpleDateFormatUTC().format(hit.getHitTime()));
		long id = db.insertOrThrow(Table_HabitHits.TABLE_NAME, null, values);
		return id;
	}

	@Override
	public void deleteByHabitId(long habitId) {
		db.delete(Table_HabitHits.TABLE_NAME, Table_HabitHits.COLUMN_HABIT_ID + "=?", new String[]{String.valueOf(habitId)});
	}
	
	@Override
	public boolean delete(long[] hitId) {
		if(hitId == null || hitId.length == 0) return false;
		
		String str_id = "";
		int lastIndex = hitId.length - 1;
		for(int i=0; i<hitId.length; i++){
			str_id = str_id + hitId[i];
			if(i!=lastIndex) str_id = str_id + ",";
		}
		
		int count = db.delete(Table_HabitHits.TABLE_NAME, Table_HabitHits.COLUMN_ID + " in (" + str_id + ")", null);
		if(count > 0) return true; else return false;
	}
	
	@Override
	public Hit getLastByHabitId(long habitId){
		Hit result = null;
		String[] columns = new String[]{Table_HabitHits.COLUMN_ID,Table_HabitHits.COLUMN_HIT_TIME};
		String selection = Table_HabitHits.COLUMN_HABIT_ID + "=?";
		String[] selectionArgs = new String[]{String.valueOf(habitId)};
		Cursor cur = db.query(Table_HabitHits.TABLE_NAME, columns, selection, selectionArgs, null, null, Table_HabitHits.COLUMN_ID + " desc", "1");
		
		if(cur.moveToFirst()){
			Date time;
			try {
				time = Utilities.getSimpleDateFormatUTC().parse(cur.getString(1));
				result = new Hit(cur.getLong(0), habitId, time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		
		return result;
	}
	
}