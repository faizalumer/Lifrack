package com.theAlternate.lifrack.LocalDB;

public class View_HabitHitsSummary{
	//view name
	public static final String VIEW_NAME = "vw_HabitHitsSummary";
	
	//column names
	public static final String COLUMN_HABIT_ID = "HabitId";
	public static final String COLUMN_TOTAL = "Total";
	public static final String COLUMN_TODAY = "Today";
	public static final String COLUMN_THIS_WEEK = "ThisWeek";
	public static final String COLUMN_THIS_MONTH = "ThisMonth";
	public static final String COLUMN_THIS_YEAR = "ThisYear";
	public static final String COLUMN_LAST_HIT = "LastHit";
	
	//total count
	private static final String SELECT_CLAUSE_TOTAL = "SELECT " 
			+ Table_HabitHits.COLUMN_HABIT_ID + " AS " + COLUMN_HABIT_ID
			+ ", count(*) AS " + COLUMN_TOTAL
			+ " FROM " + Table_HabitHits.TABLE_NAME
			+ " GROUP BY " + Table_HabitHits.COLUMN_HABIT_ID;
	
	//year count
	private static final String SELECT_CLAUSE_YEAR = "SELECT " 
			+ Table_HabitHits.COLUMN_HABIT_ID + " AS " + COLUMN_HABIT_ID
			+ ", count(*) AS " + COLUMN_THIS_YEAR
			+ " FROM " + Table_HabitHits.TABLE_NAME
			+ " WHERE strftime('%Y'," + Table_HabitHits.COLUMN_HIT_TIME + ",'localtime') = strftime('%Y','now','localtime')"   
			+ " GROUP BY " + Table_HabitHits.COLUMN_HABIT_ID;
	
	//month count
	private static final String SELECT_CLAUSE_MONTH = "SELECT " 
			+ Table_HabitHits.COLUMN_HABIT_ID + " AS " + COLUMN_HABIT_ID
			+ ", count(*) AS " + COLUMN_THIS_MONTH
			+ " FROM " + Table_HabitHits.TABLE_NAME
			+ " WHERE strftime('%Y'," + Table_HabitHits.COLUMN_HIT_TIME + ",'localtime') = strftime('%Y','now','localtime')"
			+ " AND strftime('%m'," + Table_HabitHits.COLUMN_HIT_TIME + ",'localtime') = strftime('%m','now','localtime')"   
			+ " GROUP BY " + Table_HabitHits.COLUMN_HABIT_ID;
	
	//day count
	private static final String SELECT_CLAUSE_DAY = "SELECT " 
			+ Table_HabitHits.COLUMN_HABIT_ID + " AS " + COLUMN_HABIT_ID
			+ ", count(*) AS " + COLUMN_TODAY
			+ " FROM " + Table_HabitHits.TABLE_NAME
			+ " WHERE strftime('%Y'," + Table_HabitHits.COLUMN_HIT_TIME + ",'localtime') = strftime('%Y','now','localtime')"
			+ " AND strftime('%m'," + Table_HabitHits.COLUMN_HIT_TIME + ",'localtime') = strftime('%m','now','localtime')"  
			+ " AND strftime('%d'," + Table_HabitHits.COLUMN_HIT_TIME + ",'localtime') = strftime('%d','now','localtime')"   
			+ " GROUP BY " + Table_HabitHits.COLUMN_HABIT_ID;
	
	//last hit
	private static final String SELECT_CLAUSE_LAST = "SELECT " 
			+ Table_HabitHits.COLUMN_HABIT_ID + " AS " + COLUMN_HABIT_ID
			+ ", MAX(" + Table_HabitHits.COLUMN_HIT_TIME + ") AS " + COLUMN_LAST_HIT
			+ " FROM " + Table_HabitHits.TABLE_NAME
			+ " GROUP BY " + Table_HabitHits.COLUMN_HABIT_ID;
	
	//week count
	private static final String SELECT_CLAUSE_WEEK = "SELECT "
			+ Table_HabitHits.COLUMN_HABIT_ID + " AS " + COLUMN_HABIT_ID
			+ ", count(*) AS " + COLUMN_THIS_WEEK
			+ " FROM " + Table_HabitHits.TABLE_NAME
			+ " WHERE DATE(" + Table_HabitHits.COLUMN_HIT_TIME + ",'localtime') >= DATE('now','localtime','weekday 0','-6 days')"
			+ " GROUP BY " + Table_HabitHits.COLUMN_HABIT_ID;
	
	//create view script
	public static final String CREATE_VIEW = "CREATE VIEW " + VIEW_NAME
			+ " AS SELECT "
			+ "a." + COLUMN_HABIT_ID + " AS " + COLUMN_HABIT_ID 
			+ ", a." + COLUMN_TOTAL + " AS " + COLUMN_TOTAL 
			+ ", b." + COLUMN_THIS_YEAR + " AS " + COLUMN_THIS_YEAR 
			+ ", c." + COLUMN_THIS_MONTH + " AS " + COLUMN_THIS_MONTH 
			+ ", d." + COLUMN_TODAY + " AS " + COLUMN_TODAY
			+ ", datetime(e." + COLUMN_LAST_HIT + ",'localtime') AS " + COLUMN_LAST_HIT
			+ ", f." + COLUMN_THIS_WEEK + " AS " + COLUMN_THIS_WEEK
			+ " FROM (" + SELECT_CLAUSE_TOTAL + ") a"
			+ " LEFT JOIN (" + SELECT_CLAUSE_YEAR + ") b ON a." + COLUMN_HABIT_ID + " = b." + COLUMN_HABIT_ID
			+ " LEFT JOIN (" + SELECT_CLAUSE_MONTH + ") c ON a." + COLUMN_HABIT_ID + " = c." + COLUMN_HABIT_ID
			+ " LEFT JOIN (" + SELECT_CLAUSE_DAY + ") d ON a." + COLUMN_HABIT_ID + " = d." + COLUMN_HABIT_ID
			+ " LEFT JOIN (" + SELECT_CLAUSE_LAST + ") e ON a." + COLUMN_HABIT_ID + " = e." + COLUMN_HABIT_ID
			+ " LEFT JOIN (" + SELECT_CLAUSE_WEEK + ") f ON a." + COLUMN_HABIT_ID + " = f." + COLUMN_HABIT_ID
			;

}
