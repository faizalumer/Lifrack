package com.theAlternate.lifrack.LocalDB;

public class View_HabitsCompleteInfo{
	//View name
	public static final String VIEW_NAME = "vw_Habits_CompleteInfo";
	
	//column names for columns from Habits
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "Name";
	public static final String COLUMN_CREATED_TIME = "CreatedTime";

	//column names for columns from HabitTargets
	public static final String COLUMN_TARGET_ID = "TargetId";
	public static final String COLUMN_TARGET_DESCRIPTION = "TargetDescription";
	public static final String COLUMN_TARGET_CREATED_TIME = "TargetCreatedTime";
	public static final String COLUMN_TARGET_HIT_COUNT = "TargetHitCount";
	public static final String COLUMN_TARGET_HIT_DAY_COUNT = "TargetHitDayCount";
	
	//column names for columns from HabitSchedules
	public static final String COLUMN_SCHEDULE_ID = "ScheduleId";
	public static final String COLUMN_DAILY_FREQUENCY = "DailyFrequency";
	public static final String COLUMN_MONDAY = "Monday";
	public static final String COLUMN_TUESDAY = "Tuesday";
	public static final String COLUMN_WEDNESDAY = "Wednesday";
	public static final String COLUMN_THURSDAY = "Thursday";
	public static final String COLUMN_FRIDAY = "Friday";
	public static final String COLUMN_SATURDAY = "Saturday";
	public static final String COLUMN_SUNDAY = "Sunday";
	public static final String COLUMN_WEEK_FREQUENCY = "WeekFrequency";
	public static final String COLUMN_SCHEDULE_CREATED_TIME = "ShdeduleCreatedTime";
	
	//column names for columns from Habit summary view
	public static final String COLUMN_TOTAL_HIT_COUNT = "TotalHitCount";
	public static final String COLUMN_TODAY_HIT_COUNT = "TodayHitCount";
	public static final String COLUMN_THIS_WEEK_HIT_COUNT = "ThisWeekHitCount";
	public static final String COLUMN_THIS_MONTH_HIT_COUNT = "ThisMonthHitCount";
	public static final String COLUMN_THIS_YEAR_HIT_COUNT = "ThisYearHitCount";
	public static final String COLUMN_LAST_HIT_TIME = "LastHitTime";
	
	//column names from habit hit session table
	public static final String COLUMN_HIT_SESSION_START_TIME = "HitSessionStartTime";
	public static final String COLUMN_HIT_SESSION_ID = "HitSessionId";

	//select statement
	private static final String SELECT_CLAUSE = "SELECT "
		+ Table_Habits.TABLE_NAME + "." + Table_Habits.COLUMN_ID + " AS " + COLUMN_ID
		+ ", " +  Table_Habits.TABLE_NAME + "." + Table_Habits.COLUMN_NAME + " AS " + COLUMN_NAME
		+ ", " + Table_Habits.TABLE_NAME + "." + Table_Habits.COLUMN_CREATED_TIME + " AS " + COLUMN_CREATED_TIME
		+ ", " +  View_TargetInfo.VIEW_NAME + "." + View_TargetInfo.COLUMN_DESCRIPTION + " AS " + COLUMN_TARGET_DESCRIPTION
		+ ", " +  View_TargetInfo.VIEW_NAME + "." + View_TargetInfo.COLUMN_CREATED_TIME + " AS " + COLUMN_TARGET_CREATED_TIME
		+ ", " +  Table_HabitSchedules.TABLE_NAME + "." + Table_HabitSchedules.COLUMN_DAILY_FREQUENCY + " AS " + COLUMN_DAILY_FREQUENCY
		+ ", " +  Table_HabitSchedules.TABLE_NAME + "." + Table_HabitSchedules.COLUMN_MONDAY + " AS " + COLUMN_MONDAY
		+ ", " +  Table_HabitSchedules.TABLE_NAME + "." + Table_HabitSchedules.COLUMN_TUESDAY + " AS " + COLUMN_TUESDAY
		+ ", " +  Table_HabitSchedules.TABLE_NAME + "." + Table_HabitSchedules.COLUMN_WEDNESDAY + " AS " + COLUMN_WEDNESDAY
		+ ", " +  Table_HabitSchedules.TABLE_NAME + "." + Table_HabitSchedules.COLUMN_THURSDAY + " AS " + COLUMN_THURSDAY
		+ ", " +  Table_HabitSchedules.TABLE_NAME + "." + Table_HabitSchedules.COLUMN_FRIDAY + " AS " + COLUMN_FRIDAY
		+ ", " +  Table_HabitSchedules.TABLE_NAME + "." + Table_HabitSchedules.COLUMN_SATURDAY + " AS " + COLUMN_SATURDAY
		+ ", " +  Table_HabitSchedules.TABLE_NAME + "." + Table_HabitSchedules.COLUMN_SUNDAY + " AS " + COLUMN_SUNDAY
		+ ", " +  Table_HabitSchedules.TABLE_NAME + "." + Table_HabitSchedules.COLUMN_WEEK_FREQUENCY + " AS " + COLUMN_WEEK_FREQUENCY
		+ ", " +  Table_HabitSchedules.TABLE_NAME + "." + Table_HabitSchedules.COLUMN_CREATED_TIME + " AS " + COLUMN_SCHEDULE_CREATED_TIME
		+ ", " +  View_HabitHitsSummary.VIEW_NAME + "." + View_HabitHitsSummary.COLUMN_TOTAL + " AS " + COLUMN_TOTAL_HIT_COUNT
		+ ", " +  View_HabitHitsSummary.VIEW_NAME + "." + View_HabitHitsSummary.COLUMN_THIS_YEAR + " AS " + COLUMN_THIS_YEAR_HIT_COUNT
		+ ", " +  View_HabitHitsSummary.VIEW_NAME + "." + View_HabitHitsSummary.COLUMN_THIS_MONTH + " AS " + COLUMN_THIS_MONTH_HIT_COUNT
		+ ", " +  View_HabitHitsSummary.VIEW_NAME + "." + View_HabitHitsSummary.COLUMN_TODAY + " AS " + COLUMN_TODAY_HIT_COUNT
		+ ", " +  View_HabitHitsSummary.VIEW_NAME + "." + View_HabitHitsSummary.COLUMN_LAST_HIT + " AS " + COLUMN_LAST_HIT_TIME
		+ ", " +  View_HabitHitsSummary.VIEW_NAME + "." + View_HabitHitsSummary.COLUMN_THIS_WEEK + " AS " + COLUMN_THIS_WEEK_HIT_COUNT
		+ ", " +  View_TargetInfo.VIEW_NAME + "." + View_TargetInfo.COLUMN_HIT_COUNT + " AS " + COLUMN_TARGET_HIT_COUNT
		+ ", " +  View_TargetInfo.VIEW_NAME + "." + View_TargetInfo.COLUMN_HIT_DAY_COUNT + " AS " + COLUMN_TARGET_HIT_DAY_COUNT
		//+ ", datetime(" +  Table_HabitHitSessions.TABLE_NAME + "." + Table_HabitHitSessions.COLUMN_START_TIME + ",'localtime') AS " + COLUMN_HIT_SESSION_START_TIME
		+ ", " +  Table_HabitHitSessions.TABLE_NAME + "." + Table_HabitHitSessions.COLUMN_START_TIME + " AS " + COLUMN_HIT_SESSION_START_TIME
		+ ", " + View_TargetInfo.VIEW_NAME + "." + View_TargetInfo.COLUMN_TARGET_ID + " AS " + COLUMN_TARGET_ID
		+ ", " +  Table_HabitSchedules.TABLE_NAME + "." + Table_HabitSchedules.COLUMN_ID + " AS " + COLUMN_SCHEDULE_ID
		+ ", " +  Table_HabitHitSessions.TABLE_NAME + "." + Table_HabitHitSessions.COLUMN_ID + " AS " + COLUMN_HIT_SESSION_ID
		;
	
	//create view script
	public static final String CREATE_VIEW = 
		"CREATE VIEW " + VIEW_NAME + " AS " + SELECT_CLAUSE
		+ " FROM " + Table_Habits.TABLE_NAME
		+ " LEFT JOIN " + View_TargetInfo.VIEW_NAME 
		+ " ON " + Table_Habits.TABLE_NAME + "." + Table_Habits.COLUMN_ID 
			+ " = " + View_TargetInfo.VIEW_NAME + "." + View_TargetInfo.COLUMN_HABIT_ID
		+ " LEFT JOIN " + Table_HabitSchedules.TABLE_NAME
		+ " ON " + Table_Habits.TABLE_NAME + "." + Table_Habits.COLUMN_ID 
				+ " = " + Table_HabitSchedules.TABLE_NAME + "." + Table_HabitSchedules.COLUMN_HABIT_ID
		+ " AND " + Table_HabitSchedules.TABLE_NAME + "." + Table_HabitSchedules.COLUMN_INVALIDATED_TIME + "=" + Table_HabitSchedules.NOT_INVALIDATED
		+ " LEFT JOIN " + View_HabitHitsSummary.VIEW_NAME
		+ " ON " + Table_Habits.TABLE_NAME + "." + Table_Habits.COLUMN_ID
				+ " = " + View_HabitHitsSummary.VIEW_NAME + "." + View_HabitHitsSummary.COLUMN_HABIT_ID	
		+ " LEFT JOIN " + Table_HabitHitSessions.TABLE_NAME
			+ " ON " + Table_HabitHitSessions.TABLE_NAME + "." + Table_HabitHitSessions.COLUMN_HABIT_ID
				+ " = " + Table_Habits.TABLE_NAME + "." + Table_Habits.COLUMN_ID
			+ " AND " + Table_HabitHitSessions.TABLE_NAME + "." + Table_HabitHitSessions.COLUMN_END_TIME 
				+ "=" + Table_HabitHitSessions.NOT_ENDED
		; 
} 