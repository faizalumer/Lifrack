package com.theAlternate.lifrack;

public class Table_HabitSchedulesHistory{
	//table name
	public static final String TABLE_NAME = "HabitSchedulesHistory";

	//column names
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_HABIT_ID = "HabitId";
	public static final String COLUMN_DAILY_FREQUENCY = "DailyFrequency";
	public static final String COLUMN_MONDAY = "Monday";
	public static final String COLUMN_TUESDAY = "Tuesday";
	public static final String COLUMN_WEDNESDAY = "Wednesday";
	public static final String COLUMN_THURSDAY = "Thursday";
	public static final String COLUMN_FRIDAY = "Friday";
	public static final String COLUMN_SATURDAY = "Saturday";
	public static final String COLUMN_SUNDAY = "Sunday";
	public static final String COLUMN_WEEK_FREQUENCY = "WeekFrequency";
	public static final String COLUMN_CREATED_TIME = "CreatedTime";
	public static final String COLUMN_INVALIDATED_TIME = "InvalidatedTime";

	//create table script
	public static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME
			+ "( " + COLUMN_ID + " INTEGER PRIMARY KEY" //this column is an alias for ROWID(64 byte signed auto incremented integer maintained by SQLite)
			+ ", " + COLUMN_HABIT_ID + " INTEGER NOT NULL"
			+ ", " + COLUMN_DAILY_FREQUENCY + " INTEGER NOT NULL"
			+ ", " + COLUMN_MONDAY + " BOOLEAN NOT NULL"
			+ ", " + COLUMN_TUESDAY + " BOOLEAN NOT NULL"
			+ ", " + COLUMN_WEDNESDAY + " BOOLEAN NOT NULL"
			+ ", " + COLUMN_THURSDAY + " BOOLEAN NOT NULL"
			+ ", " + COLUMN_FRIDAY + " BOOLEAN NOT NULL"
			+ ", " + COLUMN_SATURDAY + " BOOLEAN NOT NULL"
			+ ", " + COLUMN_SUNDAY + " BOOLEAN NOT NULL"
			+ ", " + COLUMN_WEEK_FREQUENCY + " INTEGER NOT NULL"
			+ ", " + COLUMN_CREATED_TIME + " TEXT NOT NULL"
			+ ", " + COLUMN_INVALIDATED_TIME + " TEXT"
			+ ", FOREIGN KEY(" + COLUMN_HABIT_ID + ") REFERENCES " + Table_HabitsHistory.TABLE_NAME + "(" + Table_HabitsHistory.COLUMN_ID + ")"
			+ ")";
	
}
