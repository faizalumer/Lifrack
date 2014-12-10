package com.theAlternate.lifrack;

import com.theAlternate.lifrack.LocalDB.Table_HabitHits;
import com.theAlternate.lifrack.LocalDB.Table_HabitHitsHistory;
import com.theAlternate.lifrack.LocalDB.Table_HabitTargets;
import com.theAlternate.lifrack.LocalDB.Table_HabitTargetsHistory;
import com.theAlternate.lifrack.LocalDB.Table_Habits;
import com.theAlternate.lifrack.LocalDB.Table_HabitsHistory;
import com.theAlternate.lifrack.LocalDB.View_ArchivedHabits_Summary;
import com.theAlternate.lifrack.LocalDB.View_ArchivedHits;
import com.theAlternate.lifrack.LocalDB.View_ArchivedTargetsInfo;
import com.theAlternate.lifrack.LocalDB.View_HabitsCompleteInfo;
import com.theAlternate.lifrack.LocalDB.View_Hits;
import com.theAlternate.lifrack.LocalDB.View_TargetsInfo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider{
	//private LocalDBHelper localDBHelper;
	private static final String AUTHORITY = "com.theAlternate.lifrack.provider";
	private static final String LOG_TAG = "MyContentProvider"; 
	
	//define ID for URIs..to make the switch statements more readable
	private static final int URI_ID_HABITS = 1;
	//private static final int URI_ID_HABITS_SINGLEROW = 2;
	private static final int URI_ID_HABIT_TARGETS = 3;
	//private static final int URI_ID_HABIT_TARGETS_SINGLEROW = 4;
	//private static final int URI_ID_HABIT_SCHEDULES = 5;
	//private static final int URI_ID_HABIT_SCHEDULES_SINGLEROW = 6;
	private static final int URI_ID_HABIT_HITS = 7;
	//private static final int URI_ID_HABIT_HITSESSIONS = 8;
	private static final int URI_ID_ARCHIVED_HABITS = 9;
	private static final int URI_ID_ARCHIVED_HABIT_HITS = 10;
	private static final int URI_ID_ARCHIVED_HABIT_TARGETS = 11;
	//private static final int URI_ID_ARCHIVED_HABIT_HITSESSIONS = 12;
	
	
	
	//define URI patterns for provider
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static{
		sUriMatcher.addURI(AUTHORITY,Table_Habits.TABLE_NAME,URI_ID_HABITS);
		//sUriMatcher.addURI(AUTHORITY,Table_Habits.TABLE_NAME + "/#" , URI_ID_HABITS_SINGLEROW);
		sUriMatcher.addURI(AUTHORITY,Table_HabitTargets.TABLE_NAME,URI_ID_HABIT_TARGETS);
		//sUriMatcher.addURI(AUTHORITY,Table_HabitTargets.TABLE_NAME + "/#" , URI_ID_HABIT_TARGETS_SINGLEROW);
		//sUriMatcher.addURI(AUTHORITY,Table_HabitSchedules.TABLE_NAME,URI_ID_HABIT_SCHEDULES);
		//sUriMatcher.addURI(AUTHORITY,Table_HabitSchedules.TABLE_NAME + "/#" , URI_ID_HABIT_SCHEDULES_SINGLEROW);
		sUriMatcher.addURI(AUTHORITY,Table_HabitHits.TABLE_NAME, URI_ID_HABIT_HITS);
		//sUriMatcher.addURI(AUTHORITY,Table_HabitHitSessions.TABLE_NAME, URI_ID_HABIT_HITSESSIONS);
		sUriMatcher.addURI(AUTHORITY,Table_HabitsHistory.TABLE_NAME, URI_ID_ARCHIVED_HABITS);
		sUriMatcher.addURI(AUTHORITY,Table_HabitHitsHistory.TABLE_NAME, URI_ID_ARCHIVED_HABIT_HITS);
		sUriMatcher.addURI(AUTHORITY,Table_HabitTargetsHistory.TABLE_NAME,URI_ID_ARCHIVED_HABIT_TARGETS);
		//sUriMatcher.addURI(AUTHORITY,Table_HabitHitSessionsHistory.TABLE_NAME, URI_ID_ARCHIVED_HABIT_HITSESSIONS);
	}
	
	//define public URIs based on patterns defined above
	public static final Uri URI_HABITS = Uri.parse("content://" + AUTHORITY + "/" + Table_Habits.TABLE_NAME);
	public static final Uri URI_HABIT_TARGETS = Uri.parse("content://" + AUTHORITY + "/" + Table_HabitTargets.TABLE_NAME);
	//public static final Uri URI_HABIT_SCHEDULES = Uri.parse("content://" + AUTHORITY + "/" + Table_HabitSchedules.TABLE_NAME);
	public static final Uri URI_HABIT_HITS = Uri.parse("content://" + AUTHORITY + "/" + Table_HabitHits.TABLE_NAME);
	//public static final Uri URI_HABIT_HITSESSIONS = Uri.parse("content://" + AUTHORITY + "/" + Table_HabitHitSessions.TABLE_NAME);
	public static final Uri URI_ARCHIVED_HABITS = Uri.parse("content://" + AUTHORITY + "/" + Table_HabitsHistory.TABLE_NAME);
	public static final Uri URI_ARCHIVED_HABIT_HITS = Uri.parse("content://" + AUTHORITY + "/" + Table_HabitHitsHistory.TABLE_NAME);
	public static final Uri URI_ARCHIVED_HABIT_TARGETS = Uri.parse("content://" + AUTHORITY + "/" + Table_HabitTargetsHistory.TABLE_NAME);
	//public static final Uri URI_ARCHIVED_HABIT_HITSESSIONS = Uri.parse("content://" + AUTHORITY + "/" + Table_HabitHitSessionsHistory.TABLE_NAME);
	
	@Override
	public boolean onCreate(){
		//localDBHelper = new LocalDBHelper(getContext());
		return true;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values){
		/*SQLiteDatabase db = localDBHelper.getWritableDatabase();
		int uriType = sUriMatcher.match(uri);
		long id;

		switch(uriType){
		case URI_ID_HABITS:
			values.put(Table_Habits.COLUMN_CREATED_TIME, Utilities.getCurrentTimeFormatted());
			id = db.insert(Table_Habits.TABLE_NAME, null, values);
			break;
		
		case URI_ID_HABIT_TARGETS:
			values.put(Table_HabitTargets.COLUMN_CREATED_TIME, Utilities.getCurrentTimeFormatted());
			id = db.insert(Table_HabitTargets.TABLE_NAME,null,values);
			break;
			
		case URI_ID_HABIT_SCHEDULES:
			values.put(Table_HabitSchedules.COLUMN_CREATED_TIME, Utilities.getCurrentTimeFormatted());
			id = db.insert(Table_HabitSchedules.TABLE_NAME,null,values);
			break;

		default:
			throw new IllegalArgumentException("Unexpected uri pattern : " + uri);
		}

		//db.close();
		
		if(id == -1){
			throw new RuntimeException("insert failed for uri pattern : " + uri);
		}
		else{
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"row inserted.uri="+uri+"/"+id);}
			getContext().getContentResolver().notifyChange(URI_HABITS,null);
			return Uri.parse(uri + "/" + id);
		}*/
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		/*SQLiteDatabase db = localDBHelper.getWritableDatabase();
		int uriType = sUriMatcher.match(uri);
		int count = 0;
		String rowId;
		ContentValues values;
		
		switch (uriType) {
		case URI_ID_HABITS_SINGLEROW:
			rowId = uri.getLastPathSegment();
			values = new ContentValues();
			values.put(Table_Habits.COLUMN_DELETED, 1);
			values.put(Table_Habits.COLUMN_DELETED_TIME, Utilities.getCurrentTimeFormatted());
			count = db.update(Table_Habits.TABLE_NAME, values, "_id=?", new String[] { rowId });
			break;
			
		case URI_ID_HABIT_TARGETS_SINGLEROW:
			rowId = uri.getLastPathSegment();
			values = new ContentValues();
			values.put(Table_HabitTargets.COLUMN_DELETED, Table_HabitTargets.DELETED);
			values.put(Table_HabitTargets.COLUMN_DELETED_TIME, Utilities.getCurrentTimeFormatted());
			count = db.update(Table_HabitTargets.TABLE_NAME, values, "_id=?", new String[] { rowId });
			break;
			
		case URI_ID_HABIT_SCHEDULES_SINGLEROW:
			rowId = uri.getLastPathSegment();
			values = new ContentValues();
			values.put(Table_HabitSchedules.COLUMN_DELETED, Table_HabitSchedules.DELETED);
			values.put(Table_HabitSchedules.COLUMN_DELETED_TIME, Utilities.getCurrentTimeFormatted());
			count = db.update(Table_HabitSchedules.TABLE_NAME, values, "_id=?", new String[] { rowId });
			break;	

		default:
			Log.e("Content provider : delete", "unexpected URI : " + uri);
		}
		
		db.close();
		getContext().getContentResolver().notifyChange(URI_HABIT_HABITS_COMPLETEINFO,null);
		return count;*/
		return -1;
	}
	
	/*//need to move this function to a habit class. leaving it for now.
	public int archive(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = localDBHelper.getWritableDatabase();
		int uriType = sUriMatcher.match(uri);
		int count = 0;
		
		switch (uriType) {
		case URI_ID_HABITS_SINGLEROW:
			String id = uri.getLastPathSegment();
			ContentValues values = new ContentValues();
			values.put(Table_Habits.COLUMN_ARCHIVED, 1);
			values.put(Table_Habits.COLUMN_ARCHIVED_TIME, Utilities.getCurrentTimeFormatted());
			count = db.update(Table_Habits.TABLE_NAME, values, "_id=?", new String[] { id });
			break;

		default:
			Log.d("Content provider : archive", "unexpected URI : " + uri);
		}
		
		db.close();
		return count;
	}
*/
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		//SQLiteDatabase db = localDBHelper.getReadableDatabase();
		SQLiteDatabase db = LocalDBHelper.getInstance().getReadableDatabase();
		Cursor cursor = null;
		int uriType = sUriMatcher.match(uri);
		
		switch(uriType){
		case URI_ID_HABITS :
			cursor = db.query(View_HabitsCompleteInfo.VIEW_NAME, projection, selection, selectionArgs, null, null, sortOrder, null);
			break;
			
		/*case URI_ID_HABIT_TARGETS :
			//String[] columns = new String[]{Table_Habits.COLUMN_ID, Table_Habits.COLUMN_NAME, Table_Habits.COLUMN_DELETED, Table_Habits.COLUMN_ARCHIVED, Table_Habits.COLUMN_CREATED_TIME, Table_Habits.COLUMN_DELETED_TIME, Table_Habits.COLUMN_ARCHIVED_TIME};
			
			//deleted=0 is not working. need to fix. commenting for now.
			//String selection_modified = Table_Habits.COLUMN_DELETED + " = 0" + ((selection == null)?"": " AND " + selection);
			//if(BuildConfig.DEBUG){Log.d(LOG_TAG,"query : selection_modified="+selection_modified);}
			//cursor = db.query(Table_Habits.TABLE_NAME, projection, selection_modified, selectionArgs, null, null, sortOrder, null);
			cursor = db.query(Table_HabitTargets.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder, null);
			break;
			
		case URI_ID_HABIT_SCHEDULES :
			//String[] columns = new String[]{Table_Habits.COLUMN_ID, Table_Habits.COLUMN_NAME, Table_Habits.COLUMN_DELETED, Table_Habits.COLUMN_ARCHIVED, Table_Habits.COLUMN_CREATED_TIME, Table_Habits.COLUMN_DELETED_TIME, Table_Habits.COLUMN_ARCHIVED_TIME};
			
			//deleted=0 is not working. need to fix. commenting for now.
			//String selection_modified = Table_Habits.COLUMN_DELETED + " = 0" + ((selection == null)?"": " AND " + selection);
			//if(BuildConfig.DEBUG){Log.d(LOG_TAG,"query : selection_modified="+selection_modified);}
			//cursor = db.query(Table_Habits.TABLE_NAME, projection, selection_modified, selectionArgs, null, null, sortOrder, null);
			cursor = db.query(Table_HabitSchedules.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder, null);
			break;*/
			
		case URI_ID_HABIT_HITS :
			cursor = db.query(View_Hits.VIEW_NAME, projection, selection, selectionArgs, null, null, sortOrder,null);
			break;
			
		case URI_ID_HABIT_TARGETS :
			cursor = db.query(View_TargetsInfo.VIEW_NAME, projection, selection, selectionArgs, null, null, sortOrder,null);
			break;
			
		/*case URI_ID_HABIT_HITSESSIONS :
			cursor = db.query(Table_HabitHitSessions.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder, null);
			break;*/
			
		case URI_ID_ARCHIVED_HABITS :
			cursor = db.query(View_ArchivedHabits_Summary.VIEW_NAME, projection, selection, selectionArgs, null, null, sortOrder, null);
			break;
			
		case URI_ID_ARCHIVED_HABIT_HITS :
			cursor = db.query(View_ArchivedHits.VIEW_NAME, projection, selection, selectionArgs, null, null, sortOrder,null);
			break;
			
		case URI_ID_ARCHIVED_HABIT_TARGETS :
			cursor = db.query(View_ArchivedTargetsInfo.VIEW_NAME, projection, selection, selectionArgs, null, null, sortOrder,null);
			break;
			
		/*case URI_ID_ARCHIVED_HABIT_HITSESSIONS :
			cursor = db.query(Table_HabitHitSessionsHistory.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder, null);
			break;*/
			
		default :
			throw new IllegalArgumentException("Unexpected uri pattern : " + uri);
		}
		
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
			/*SQLiteDatabase db = localDBHelper.getWritableDatabase();
			int uriType = sUriMatcher.match(uri);
			int count = 0;
			String rowId;
			
			switch (uriType) {
			case URI_ID_HABITS_SINGLEROW:
				rowId = uri.getLastPathSegment();
				count = db.update(Table_Habits.TABLE_NAME, values, "_id=?", new String[] { rowId });
				break;
				
			case URI_ID_HABIT_TARGETS_SINGLEROW:
				rowId = uri.getLastPathSegment();
				count = db.update(Table_HabitTargets.TABLE_NAME, values, "_id=?", new String[] { rowId });
				break;	
				
			case URI_ID_HABIT_SCHEDULES_SINGLEROW:
				rowId = uri.getLastPathSegment();
				count = db.update(Table_HabitSchedules.TABLE_NAME, values, "_id=?", new String[] { rowId });
				break;	

			default:
				Log.e("Content provider : update", "unexpected URI : " + uri);
			}
			
			db.close();
			getContext().getContentResolver().notifyChange(URI_HABIT_HABITS_COMPLETEINFO,null);
			return count;*/
		return -1;
		}


}
