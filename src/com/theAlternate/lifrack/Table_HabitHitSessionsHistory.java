package com.theAlternate.lifrack;

public class Table_HabitHitSessionsHistory{
	//table name
	public static final String TABLE_NAME = "HabitHitSessionsHistory";
	
	//column names
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_HIT_ID = "HitId";
	public static final String COLUMN_HABIT_ID = "HabitId";
	public static final String COLUMN_START_TIME = "StartTime";
	public static final String COLUMN_END_TIME = "EndTime";

	//create table script
		public static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME
			+ "( " + COLUMN_ID + " INTEGER PRIMARY KEY" //this column is an alias for ROWID(64 byte signed auto incremented integer maintained by SQLite)
			+ ", " + COLUMN_HIT_ID + " INTEGER" //cannot define HIT_ID as NOT NULL because no HIT record exists when a session is created(started) 
			+ ", " + COLUMN_HABIT_ID + " INTEGER NOT NULL"
			+ ", " + COLUMN_START_TIME + " TEXT NOT NULL"
			+ ", " + COLUMN_END_TIME + " TEXT "
			+ ", FOREIGN KEY(" + COLUMN_HABIT_ID + ") REFERENCES " + Table_HabitsHistory.TABLE_NAME + "(" + Table_HabitsHistory.COLUMN_ID + ")"
			+ ", FOREIGN KEY(" + COLUMN_HIT_ID + ") REFERENCES " + Table_HabitHitsHistory.TABLE_NAME + "(" + Table_HabitHitsHistory.COLUMN_ID + ")"
			+ ")";
}
