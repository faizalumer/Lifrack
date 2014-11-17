package com.theAlternate.lifrack;

public class Table_HabitHitSessions{
	//table name
	public static final String TABLE_NAME = "HabitHitSessions";
	
	//column names
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_HIT_ID = "HitId";
	public static final String COLUMN_HABIT_ID = "HabitId";
	public static final String COLUMN_START_TIME = "StartTime";
	public static final String COLUMN_END_TIME = "EndTime";
	public static final String NOT_ENDED = "-1";

	//create table script
	public static final String CREATE_TABLE = 
		"CREATE TABLE " + TABLE_NAME
		+ "( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" //this column is an alias for ROWID(64 byte signed auto incremented integer maintained by SQLite)
		+ ", " + COLUMN_HIT_ID + " INTEGER" //cannot define HIT_ID as NOT NULL because no HIT record exists when a session is created(started) 
		+ ", " + COLUMN_HABIT_ID + " INTEGER NOT NULL"
		+ ", " + COLUMN_START_TIME + " TEXT NOT NULL"
		+ ", " + COLUMN_END_TIME + " TEXT NOT NULL DEFAULT " + NOT_ENDED
		+ ", FOREIGN KEY(" + COLUMN_HABIT_ID + ") REFERENCES " + Table_Habits.TABLE_NAME + "(" + Table_Habits.COLUMN_ID + ")"
		+ ", FOREIGN KEY(" + COLUMN_HIT_ID + ") REFERENCES " + Table_HabitHits.TABLE_NAME + "(" + Table_HabitHits.COLUMN_ID + ")"
		+ ")";

	/*no two sessions can be active at the same time for a single habit
	i.e. two records with same habit id cannot have NULL(using -1 instead, 
	as NULL is not considered unique by SQLite) as the end time.
	So creating a unique composite index to enforce this*/	
	public static final String CREATE_INDEX = 	
		"CREATE UNIQUE INDEX UIDX_HIT_SESSION ON " + TABLE_NAME 
		+ "(" + COLUMN_HABIT_ID + "," + COLUMN_END_TIME + ");";


}
