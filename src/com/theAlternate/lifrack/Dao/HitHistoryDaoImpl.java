package com.theAlternate.lifrack.Dao;

import java.util.ArrayList;
import java.util.List;

import com.theAlternate.lifrack.BuildConfig;
import com.theAlternate.lifrack.DayCount;
import com.theAlternate.lifrack.LocalDB.Table_HabitHits;
import com.theAlternate.lifrack.LocalDB.Table_HabitHitsHistory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class HitHistoryDaoImpl extends SqliteDaoBase implements IHitHistoryDao{
	
	public HitHistoryDaoImpl(SQLiteDatabase db) {
		super(db);
	}

	@Override
	public void deleteByHabitId(long habitId) {
		db.delete(Table_HabitHitsHistory.TABLE_NAME, Table_HabitHitsHistory.COLUMN_HABIT_ID + "=?", new String[]{String.valueOf(habitId)});
	}
	
	@Override
	public List<DayCount> getHitCountByHabitId(long habitId, int month) {
		
		List<DayCount> result = new ArrayList<DayCount>();
		
		String sql = "select CAST(strftime('%d', x.date1) AS INTEGER) day, count from"
				+ " ( select date(" + Table_HabitHitsHistory.COLUMN_HIT_TIME + ",'localtime') as date1, count(*) as count from " + Table_HabitHitsHistory.TABLE_NAME
				+ " where " + Table_HabitHitsHistory.COLUMN_HABIT_ID + " = " + String.valueOf(habitId) 
				+ " group by date1 ) x"
				+ " where CAST(strftime('%m',x.date1) as INTEGER) = " + String.valueOf(month)
				+ " order by day asc" ;
		
		Cursor cur = db.rawQuery(sql, null);
		while(cur.moveToNext()){
			result.add(new DayCount(cur.getInt(0),cur.getInt(1)));
		}
		
		return result;
	}
	
}