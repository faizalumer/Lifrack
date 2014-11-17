package com.theAlternate.lifrack;

public class Table_HabitTargets{
	//table name
	public static final String TABLE_NAME = "HabitTargets";
	
	//column names
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_HABIT_ID = "HabitId";
	public static final String COLUMN_DESCRIPTION = "Description";
	public static final String COLUMN_CREATED_TIME = "CreatedTime";
	public static final String COLUMN_ACHIEVED_TIME = "AchievedTime";

	//create table script
	public static final String CREATE_TABLE = 
		"CREATE TABLE " + TABLE_NAME
		+ "( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" //this column is an alias for ROWID(64 byte signed auto incremented integer maintained by SQLite)
		+ ", " + COLUMN_HABIT_ID + " INTEGER NOT NULL"
		+ ", " + COLUMN_DESCRIPTION + " TEXT NOT NULL"
		+ ", " + COLUMN_CREATED_TIME + " TEXT NOT NULL"
		+ ", " + COLUMN_ACHIEVED_TIME + " TEXT"
		+ ", FOREIGN KEY(" + COLUMN_HABIT_ID + ") REFERENCES " + Table_Habits.TABLE_NAME + "(" + Table_Habits.COLUMN_ID + ")"
		+ ")";


}
