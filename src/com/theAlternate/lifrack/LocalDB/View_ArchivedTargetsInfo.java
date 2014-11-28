package com.theAlternate.lifrack.LocalDB;

public class View_ArchivedTargetsInfo{
	//view name
	public static final String VIEW_NAME = "vw_ArchivedTargetsInfo";
	
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
			+ "a." + Table_HabitTargetsHistory.COLUMN_ID + " AS " + COLUMN_TARGET_ID
			+ ",a." + Table_HabitTargetsHistory.COLUMN_HABIT_ID + " AS " + COLUMN_HABIT_ID 
			+ ",a." + Table_HabitTargetsHistory.COLUMN_DESCRIPTION + " AS " + COLUMN_DESCRIPTION
			+ ",a." + Table_HabitTargetsHistory.COLUMN_CREATED_TIME + " AS " + COLUMN_CREATED_TIME
			+ ",a." + Table_HabitTargetsHistory.COLUMN_ACHIEVED_TIME + " AS " + COLUMN_ACHIEVED_TIME
			+ ",count(" + Table_HabitHitsHistory.COLUMN_HIT_TIME + ") AS " + COLUMN_HIT_COUNT
			+ ",count(distinct date(" + Table_HabitHitsHistory.COLUMN_HIT_TIME + ",'localtime')) AS " + COLUMN_HIT_DAY_COUNT
			+ ",c." + Table_HabitsHistory.COLUMN_CREATED_TIME + " AS " + COLUMN_HABIT_CREATED_TIME
			+ " FROM " + Table_HabitTargetsHistory.TABLE_NAME + " a"
			+ " LEFT JOIN " + Table_HabitHitsHistory.TABLE_NAME + " b"
			+ " ON a." + Table_HabitTargetsHistory.COLUMN_HABIT_ID + " = b." + Table_HabitHitsHistory.COLUMN_HABIT_ID
			+ " AND a." + Table_HabitTargetsHistory.COLUMN_CREATED_TIME + " <= b." + Table_HabitHitsHistory.COLUMN_HIT_TIME
			+ " AND IFNULL(a." + Table_HabitTargetsHistory.COLUMN_ACHIEVED_TIME + ",datetime('now')) >= b." + Table_HabitHitsHistory.COLUMN_HIT_TIME
			+ " JOIN " + Table_HabitsHistory.TABLE_NAME + " c"
			+ " ON a." + Table_HabitTargetsHistory.COLUMN_HABIT_ID + " = c." + Table_HabitsHistory.COLUMN_ID 
			+ " GROUP BY a." + Table_HabitTargetsHistory.COLUMN_ID;

	//hit day count from habit creation
	private static final String SELECT_CLAUSE_HIT_DAY_COUNT_FROM_HABIT_CREATION = "SELECT "
			+ "a." + Table_HabitTargetsHistory.COLUMN_ID + " AS " + COLUMN_TARGET_ID
			+ ", count(" + Table_HabitHitsHistory.COLUMN_HIT_TIME + ") AS " + COLUMN_HIT_COUNT_FROM_HABIT_CREATION
			+ ", count(distinct date(" + Table_HabitHitsHistory.COLUMN_HIT_TIME + ",'localtime')) AS " + COLUMN_HIT_DAY_COUNT_FROM_HABIT_CREATION
			+ " FROM " + Table_HabitTargetsHistory.TABLE_NAME + " a"
			+ " LEFT JOIN " + Table_HabitHitsHistory.TABLE_NAME + " b"
			+ " ON a." + Table_HabitTargetsHistory.COLUMN_HABIT_ID + " = b." + Table_HabitHitsHistory.COLUMN_HABIT_ID
			+ " AND IFNULL(a." + Table_HabitTargetsHistory.COLUMN_ACHIEVED_TIME + ",datetime('now')) >= b." + Table_HabitHitsHistory.COLUMN_HIT_TIME  
			+ " GROUP BY a." + Table_HabitTargetsHistory.COLUMN_ID;
	
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
