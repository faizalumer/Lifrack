package com.theAlternate.lifrack.LocalDB;

public class View_TargetsInfo{
	//view name
	public static final String VIEW_NAME = "vw_TargetsInfo";
	
	//column names
	public static final String COLUMN_TARGET_ID = "_id";
	public static final String COLUMN_HABIT_ID = "HabitId";
	public static final String COLUMN_DESCRIPTION = "Description";
	public static final String COLUMN_CREATED_TIME = "CreatedTime";
	public static final String COLUMN_ACHIEVED_TIME = "AchievedTime";
	public static final String COLUMN_HIT_COUNT = "HitCount";
	public static final String COLUMN_HIT_DAY_COUNT = "HitDayCount";
	public static final String COLUMN_HIT_COUNT_FROM_HABIT_CREATION = "HitCountFromHabitCreation";
	public static final String COLUMN_HIT_DAY_COUNT_FROM_HABIT_CREATION = "HitDayCountFromHabitCreation";
	public static final String COLUMN_HABIT_CREATED_TIME = "HabitCreatedTime";
	
	//hit count
	private static final String SELECT_CLAUSE_HIT_COUNT = "SELECT "
			+ "a." + Table_HabitTargets.COLUMN_ID + " AS " + COLUMN_TARGET_ID
			+ ",a." + Table_HabitTargets.COLUMN_HABIT_ID + " AS " + COLUMN_HABIT_ID 
			+ ",a." + Table_HabitTargets.COLUMN_DESCRIPTION + " AS " + COLUMN_DESCRIPTION
			+ ",a." + Table_HabitTargets.COLUMN_CREATED_TIME + " AS " + COLUMN_CREATED_TIME
			+ ",a." + Table_HabitTargets.COLUMN_ACHIEVED_TIME + " AS " + COLUMN_ACHIEVED_TIME
			+ ",count(" + Table_HabitHits.COLUMN_HIT_TIME + ") AS " + COLUMN_HIT_COUNT
			+ ",count(distinct date(" + Table_HabitHits.COLUMN_HIT_TIME + ",'localtime')) AS " + COLUMN_HIT_DAY_COUNT
			+ ",c." + Table_Habits.COLUMN_CREATED_TIME + " AS " + COLUMN_HABIT_CREATED_TIME
			+ " FROM " + Table_HabitTargets.TABLE_NAME + " a"
			+ " LEFT JOIN " + Table_HabitHits.TABLE_NAME + " b"
			+ " ON a." + Table_HabitTargets.COLUMN_HABIT_ID + " = b." + Table_HabitHits.COLUMN_HABIT_ID
			+ " AND a." + Table_HabitTargets.COLUMN_CREATED_TIME + " <= b." + Table_HabitHits.COLUMN_HIT_TIME
			+ " AND IFNULL(a." + Table_HabitTargets.COLUMN_ACHIEVED_TIME + ",datetime('now')) >= b." + Table_HabitHits.COLUMN_HIT_TIME
			+ " JOIN " + Table_Habits.TABLE_NAME + " c"
			+ " ON a." + Table_HabitTargets.COLUMN_HABIT_ID + " = c." + Table_Habits.COLUMN_ID 
			+ " GROUP BY a." + Table_HabitTargets.COLUMN_ID;

	//hit day count from habit creation
	private static final String SELECT_CLAUSE_HIT_DAY_COUNT_FROM_HABIT_CREATION = "SELECT "
			+ "a." + Table_HabitTargets.COLUMN_ID + " AS " + COLUMN_TARGET_ID
			+ ", count(" + Table_HabitHits.COLUMN_HIT_TIME + ") AS " + COLUMN_HIT_COUNT_FROM_HABIT_CREATION
			+ ", count(distinct date(" + Table_HabitHits.COLUMN_HIT_TIME + ",'localtime')) AS " + COLUMN_HIT_DAY_COUNT_FROM_HABIT_CREATION
			+ " FROM " + Table_HabitTargets.TABLE_NAME + " a"
			+ " LEFT JOIN " + Table_HabitHits.TABLE_NAME + " b"
			+ " ON a." + Table_HabitTargets.COLUMN_HABIT_ID + " = b." + Table_HabitHits.COLUMN_HABIT_ID
			+ " AND IFNULL(a." + Table_HabitTargets.COLUMN_ACHIEVED_TIME + ",datetime('now')) >= b." + Table_HabitHits.COLUMN_HIT_TIME  
			+ " GROUP BY a." + Table_HabitTargets.COLUMN_ID;
	
	//create view script
	public static final String CREATE_VIEW = "CREATE VIEW " + VIEW_NAME
				+ " AS SELECT "
				+ "a." + COLUMN_TARGET_ID + " AS " + COLUMN_TARGET_ID
				+ ", a." + COLUMN_HABIT_ID + " AS " + COLUMN_HABIT_ID 
				+ ", a." + COLUMN_DESCRIPTION + " AS " + COLUMN_DESCRIPTION
				+ ", a." + COLUMN_CREATED_TIME + " AS " + COLUMN_CREATED_TIME
				+ ", a." + COLUMN_ACHIEVED_TIME + " AS " + COLUMN_ACHIEVED_TIME
				+ ", a." + COLUMN_HIT_COUNT + " AS " + COLUMN_HIT_COUNT 
				+ ", a." + COLUMN_HIT_DAY_COUNT + " AS " + COLUMN_HIT_DAY_COUNT
				+ ", b." + COLUMN_HIT_COUNT_FROM_HABIT_CREATION + " AS " + COLUMN_HIT_COUNT_FROM_HABIT_CREATION
				+ ", b." + COLUMN_HIT_DAY_COUNT_FROM_HABIT_CREATION + " AS " + COLUMN_HIT_DAY_COUNT_FROM_HABIT_CREATION
				+ ", a." + COLUMN_HABIT_CREATED_TIME + " AS " + COLUMN_HABIT_CREATED_TIME
				+ " FROM (" + SELECT_CLAUSE_HIT_COUNT + ") a"
				+ " LEFT JOIN (" + SELECT_CLAUSE_HIT_DAY_COUNT_FROM_HABIT_CREATION + ") b ON a." + COLUMN_TARGET_ID + " = b." + COLUMN_TARGET_ID
				;

}
