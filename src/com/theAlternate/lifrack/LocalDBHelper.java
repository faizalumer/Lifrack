package com.theAlternate.lifrack;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

public class LocalDBHelper extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "HabitDB";
	private static final String LOG_TAG = "LocalDBHelper";

	private static LocalDBHelper instance = null;
	
	/*private constructor to avoid direct instantiation by other classes*/
	private LocalDBHelper(){
		super(MyApplication.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"LocalDBHelper()");}
	}
	
	/*synchronized method to ensure only 1 instance of LocalDBHelper exists*/
	public static synchronized LocalDBHelper getInstance(){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"getInstance()");}
		if(instance == null){
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"calling LocalDBHelper()");}
			instance = new LocalDBHelper();
		}
		return instance;
	}
	
	

	@Override
	public void onCreate(SQLiteDatabase db){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"creating database");}
		db.execSQL(Table_Habits.CREATE_TABLE);
		db.execSQL(Table_HabitTargets.CREATE_TABLE);
		db.execSQL(Table_HabitSchedules.CREATE_TABLE);
		db.execSQL(Table_HabitSchedules.CREATE_INDEX);
		db.execSQL(Table_HabitHits.CREATE_TABLE);
		db.execSQL(Table_HabitReminders.CREATE_TABLE);
		db.execSQL(Table_HabitHitSessions.CREATE_TABLE);
		db.execSQL(Table_HabitHitSessions.CREATE_INDEX);
		
		db.execSQL(Table_HabitsHistory.CREATE_TABLE);
		db.execSQL(Table_HabitTargetsHistory.CREATE_TABLE);
		db.execSQL(Table_HabitSchedulesHistory.CREATE_TABLE);
		db.execSQL(Table_HabitHitsHistory.CREATE_TABLE);
		db.execSQL(Table_HabitHitSessionsHistory.CREATE_TABLE);
		
		db.execSQL(View_TargetsInfo.CREATE_VIEW);
		db.execSQL(View_ArchivedHabits_Summary.CREATE_VIEW);
		db.execSQL(View_ArchivedTargetsInfo.CREATE_VIEW);
		db.execSQL(View_Hits.CREATE_VIEW);
		db.execSQL(View_ArchivedHits.CREATE_VIEW);
		
		db.execSQL(View_HabitHitsSummary.CREATE_VIEW);
		db.execSQL(View_TargetInfo.CREATE_VIEW);
		db.execSQL(View_HabitsCompleteInfo.CREATE_VIEW);
		
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){		
			if(oldVersion == 1 && newVersion==2){
				if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onUpgrade(" +oldVersion +","+ newVersion +")");}
				db.execSQL("DROP VIEW IF EXISTS " + View_HabitHitsSummary.CREATE_VIEW);
				db.execSQL(View_HabitHitsSummary.CREATE_VIEW);
				/*db.execSQL("DROP VIEW IF EXISTS " + View_Hits.VIEW_NAME);
				db.execSQL("DROP VIEW IF EXISTS " + View_ArchivedHits.VIEW_NAME);
				db.execSQL(View_Hits.CREATE_VIEW);
				db.execSQL(View_ArchivedHits.CREATE_VIEW);*/
			}
				
				/*db.execSQL("DROP VIEW IF EXISTS " + View_Hits.VIEW_NAME);
				db.execSQL(View_Hits.CREATE_VIEW);*/
				//db.execSQL("DROP VIEW IF EXISTS " + View_HabitsCompleteInfo.VIEW_NAME);
				//db.execSQL(View_HabitsCompleteInfo.CREATE_VIEW);
				/*
				db.execSQL("DROP VIEW IF EXISTS " + View_ArchivedHabits_Summary.VIEW_NAME);
				db.execSQL(View_ArchivedHabits_Summary.CREATE_VIEW);
				
				db.execSQL("DROP VIEW IF EXISTS " + View_TargetsInfo.VIEW_NAME);
				db.execSQL(View_TargetsInfo.CREATE_VIEW);
				
				db.execSQL("DROP VIEW IF EXISTS " + View_ArchivedTargetsInfo.VIEW_NAME);
				db.execSQL(View_ArchivedTargetsInfo.CREATE_VIEW);*/
				
				
				//db.execSQL("DROP TABLE IF EXISTS ");
				//db.execSQL(View_ArchivedTargetsInfo.CREATE_VIEW);
				//db.execSQL(View_TargetsInfo.CREATE_VIEW);
			//}
			/*if(oldVersion == 22 && newVersion == 23){
				Log.d(LOG_TAG,"Upgrading DB version from " + oldVersion + " to " + newVersion);
				db.execSQL("DROP VIEW IF EXISTS " + View_HabitsCompleteInfo.VIEW_NAME);
				db.execSQL(View_HabitsCompleteInfo.CREATE_VIEW);
				
				db.execSQL(View_TargetInfo.CREATE_VIEW);
				db.execSQL("DROP VIEW IF EXISTS " + View_HabitsCompleteInfo.VIEW_NAME);
				db.execSQL(View_HabitsCompleteInfo.CREATE_VIEW);
				db.execSQL("DROP VIEW IF EXISTS vw_TargetHitsSummary");
				db.execSQL("DROP VIEW IF EXISTS " + View_TargetsInfo.VIEW_NAME );
				db.execSQL(View_TargetsInfo.CREATE_VIEW);
			}*/
		
		
		//db.execSQL(View_HabitsEditInfo.CREATE_VIEW);
		//do nothing for now
		/*if(BuildConfig.DEBUG){
			if(oldVersion == 1 && newVersion == 2){
				Log.d(LOG_TAG,"Upgrading DB version from " + oldVersion + " to " + newVersion);
				db.execSQL("DROP VIEW IF EXISTS " + View_HabitsCompleteInfo.VIEW_NAME);
				db.execSQL("DROP VIEW IF EXISTS " + View_HabitHitsSummary.VIEW_NAME );
				db.execSQL(View_HabitHitsSummary.CREATE_VIEW);
				db.execSQL(View_HabitsCompleteInfo.CREATE_VIEW);
			}
		}*/
		
	
	}
	
	/*<EDIT Nov 4 2014 : Latest Eclipse/ADT is showing error /> 
	 * onConfigure() is available only on API 16. But Eclipse is not showing any error/warning,
	 * even though i have set the minSDK to 11 in the manifest.
	 * It will probably fail at runtime. Not sure.
	 * This needs to be called in all methods after getWritableDatabase()
	 * to make it work for older APIs. Leaving it for now till further testing confirms the issue.
	 * */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onConfigure(SQLiteDatabase db){
		//super.onConfigure(db); //the super method is empty
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"onConfigure");}
		/*
		 * setForeignKeyConstraintsEnabled() is available only on API 16. Latest ADT is showing error
		 * since my minSDK is 11.This needs to be called in all methods after getWritableDatabase().
		 * For now, executing it only on API>=16. Have not handled for older versions.
		 * */
		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"db.setForeignKeyConstraintsEnabled(true)");}
			db.setForeignKeyConstraintsEnabled(true);
		}
		
		
	}

	
}
