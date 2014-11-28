package com.theAlternate.lifrack.LocalDB;

public class View_ArchivedHits{
	//view name
	public static final String VIEW_NAME = "vw_ArchivedHits";
	
	//column names
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_HABIT_ID = "HabitId";
	public static final String COLUMN_HIT_ID = "HitId";
	public static final String COLUMN_HIT_TIME = "HitTime";
	public static final String COLUMN_SESSION_ID = "SessionId";
	public static final String COLUMN_SESSION_START_TIME = "SessionStartTime";
	
	//create view script
	public static final String CREATE_VIEW = "CREATE VIEW " + VIEW_NAME
			+ " AS SELECT "
			+ "a." + Table_HabitHitsHistory.COLUMN_ID + " AS " + COLUMN_ID 
			+ ", a." + Table_HabitHitsHistory.COLUMN_HABIT_ID + " AS " + COLUMN_HABIT_ID
			+ ", a." + Table_HabitHitsHistory.COLUMN_ID + " AS " + COLUMN_HIT_ID
			+ ", a." + Table_HabitHitsHistory.COLUMN_HIT_TIME + " AS " + COLUMN_HIT_TIME 
			+ ", b." + Table_HabitHitSessionsHistory.COLUMN_ID + " AS " + COLUMN_SESSION_ID
			+ ", b." + Table_HabitHitSessionsHistory.COLUMN_START_TIME + " AS " + COLUMN_SESSION_START_TIME 
			+ " FROM " + Table_HabitHitsHistory.TABLE_NAME + " a"
			+ " LEFT JOIN " + Table_HabitHitSessionsHistory.TABLE_NAME + " b"
			+ " ON a." + Table_HabitHitsHistory.COLUMN_ID + " = b." + Table_HabitHitSessionsHistory.COLUMN_HIT_ID
			;
}
