package com.theAlternate.lifrack.LocalDB;

public class Table_HabitSchedules{
	//table name
	public static final String TABLE_NAME = "HabitSchedules";

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
	public static final String NOT_INVALIDATED= "-1";

	//create table script
	public static final String CREATE_TABLE = 
		"CREATE TABLE " + TABLE_NAME
		+ "( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" //this column is an alias for ROWID(64 byte signed auto incremented integer maintained by SQLite)
		+ ", " + COLUMN_HABIT_ID + " INTEGER NOT NULL"
		+ ", " + COLUMN_DAILY_FREQUENCY + " INTEGER NOT NULL CHECK(" + COLUMN_DAILY_FREQUENCY + " > 0)"
		+ ", " + COLUMN_MONDAY + " BOOLEAN NOT NULL"
		+ ", " + COLUMN_TUESDAY + " BOOLEAN NOT NULL"
		+ ", " + COLUMN_WEDNESDAY + " BOOLEAN NOT NULL"
		+ ", " + COLUMN_THURSDAY + " BOOLEAN NOT NULL"
		+ ", " + COLUMN_FRIDAY + " BOOLEAN NOT NULL"
		+ ", " + COLUMN_SATURDAY + " BOOLEAN NOT NULL"
		+ ", " + COLUMN_SUNDAY + " BOOLEAN NOT NULL"
		+ ", " + COLUMN_WEEK_FREQUENCY + " INTEGER NOT NULL CHECK(" + COLUMN_WEEK_FREQUENCY + " > 0)"
		+ ", " + COLUMN_CREATED_TIME + " TEXT NOT NULL"
		+ ", " + COLUMN_INVALIDATED_TIME + " TEXT DEFAULT " + NOT_INVALIDATED
		+ ", FOREIGN KEY(" + COLUMN_HABIT_ID + ") REFERENCES " + Table_Habits.TABLE_NAME + "(" + Table_Habits.COLUMN_ID + ")"
		+ ")";
	
	/*no two schedules can be active at the same time for a single habit
	i.e. two records with same habit id cannot have NULL(using -1 instead, 
	as NULL is not considered unique by SQLite) as the invalidated time.
	So creating a unique composite index to enforce this*/	
	public static final String CREATE_INDEX = 	
		"CREATE UNIQUE INDEX UIDX_SCHEDULE ON " + TABLE_NAME 
		+ "(" + COLUMN_HABIT_ID + "," + COLUMN_INVALIDATED_TIME + ");";

}
