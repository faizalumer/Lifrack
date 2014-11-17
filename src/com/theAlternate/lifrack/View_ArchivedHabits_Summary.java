package com.theAlternate.lifrack;

public class View_ArchivedHabits_Summary{
	//View name
	public static final String VIEW_NAME = "vw_ArchivedHabits_Summary";
	
	//column names for columns from Habits
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "Name";
	public static final String COLUMN_CREATED_TIME = "CreatedTime";
	public static final String COLUMN_ARCHIVED_TIME = "ArchivedTime";

	//column names for columns from HabitTargets
	public static final String COLUMN_TARGET_COUNT = "TargetCount";
	
	//column names for column names from Hits
	public static final String COLUMN_HIT_COUNT = "HitCount";
	public static final String COLUMN_HIT_DAY_COUNT = "HitDayCount";
	
	//select target count
	private static final String SELECT_TARGET = "SELECT "
			+ Table_HabitTargetsHistory.COLUMN_HABIT_ID
			+ ", count(1) AS " + COLUMN_TARGET_COUNT  
			+ " FROM " + Table_HabitTargetsHistory.TABLE_NAME
			+ " GROUP BY " + Table_HabitTargetsHistory.COLUMN_HABIT_ID; 
	
	//select hit count
	private static final String SELECT_HIT = "SELECT "
			+ Table_HabitHitsHistory.COLUMN_HABIT_ID
			+ ", count(" + Table_HabitHitsHistory.COLUMN_HIT_TIME + ") AS " + COLUMN_HIT_COUNT  
			+ ", count(distinct date(" + Table_HabitHitsHistory.COLUMN_HIT_TIME + ",'localtime')) AS " + COLUMN_HIT_DAY_COUNT
			+ " FROM " + Table_HabitHitsHistory.TABLE_NAME
			+ " GROUP BY " + Table_HabitHitsHistory.COLUMN_HABIT_ID;
	
	//select statement
	private static final String SELECT_CLAUSE = "SELECT "
		+ "a." + Table_HabitsHistory.COLUMN_ID + " AS " + COLUMN_ID
		+ ", a." + Table_HabitsHistory.COLUMN_NAME + " AS " + COLUMN_NAME
		+ ", a." + Table_HabitsHistory.COLUMN_CREATED_TIME + " AS " + COLUMN_CREATED_TIME
		+ ", a." + Table_HabitsHistory.COLUMN_ARCHIVED_TIME + " AS " + COLUMN_ARCHIVED_TIME
		+ ",b." + COLUMN_TARGET_COUNT
		+ ",c." + COLUMN_HIT_COUNT
		+ ",c." + COLUMN_HIT_DAY_COUNT
		+ " FROM " + Table_HabitsHistory.TABLE_NAME + " a"
		+ " LEFT JOIN (" +  SELECT_TARGET + ") b"
		+ " ON a." + Table_HabitsHistory.COLUMN_ID + "=b." + Table_HabitTargetsHistory.COLUMN_HABIT_ID
		+ " LEFT JOIN (" +  SELECT_HIT + ") c"
		+ " ON a." + Table_HabitsHistory.COLUMN_ID + "=c." + Table_HabitHitsHistory.COLUMN_HABIT_ID
		;
	
	//create view script
	public static final String CREATE_VIEW = 
		"CREATE VIEW " + VIEW_NAME + " AS " + SELECT_CLAUSE ;
		
} 