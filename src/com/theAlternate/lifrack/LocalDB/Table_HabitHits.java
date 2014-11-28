package com.theAlternate.lifrack.LocalDB;

public class Table_HabitHits{
	//table name
	public static final String TABLE_NAME = "HabitHits";
	
	//column names
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_HABIT_ID = "HabitId";
	public static final String COLUMN_HIT_TIME = "HitTime";

	//create table script
	public static final String CREATE_TABLE = 
		"CREATE TABLE " + TABLE_NAME
		+ "( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" //this column is an alias for ROWID(64 byte signed auto incremented integer maintained by SQLite)
		+ ", " + COLUMN_HABIT_ID + " INTEGER NOT NULL"
		+ ", " + COLUMN_HIT_TIME + " TEXT NOT NULL "
		+ ", FOREIGN KEY(" + COLUMN_HABIT_ID + ") REFERENCES " + Table_Habits.TABLE_NAME + "(" + Table_Habits.COLUMN_ID + ")"
		+ ")";


}
