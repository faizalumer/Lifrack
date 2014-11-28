package com.theAlternate.lifrack.LocalDB;

public class Table_HabitsHistory{
	//table name
	public static final String TABLE_NAME = "HabitsHistory";

	//column names
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "Name";
	public static final String COLUMN_CREATED_TIME = "CreatedTime";
	public static final String COLUMN_ARCHIVED_TIME = "ArchivedTime";

	//create table script
	public static final String CREATE_TABLE = 
		"CREATE TABLE " + TABLE_NAME
		+ "( " + COLUMN_ID + " INTEGER PRIMARY KEY" //this column is an alias for ROWID(64 byte signed auto incremented integer maintained by SQLite)
		+ ", " + COLUMN_NAME + " TEXT NOT NULL"
		+ ", " + COLUMN_CREATED_TIME + " TEXT NOT NULL"
		+ ", " + COLUMN_ARCHIVED_TIME + " TEXT NOT NULL"
		+ ")";
	
}
