package com.theAlternate.lifrack;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public class HabitTarget{
	int id;
	int habitId;
	public String description;
	
	private static final int DUMMY_ID = -1;
	
	/*//constructor to create object from existing habit in local DB
	public HabitTarget(int id){
		this.id = id;
	}
	
	//constructor to create object for new target that does not exist in local DB yet
	public HabitTarget(int habitId, String description){
		this.id = DUMMY_ID;
		this.habitId = habitId;
		this.description = description; 
	}*/
	
	//constructor to create object for new target that does not exist in local DB yet
	public HabitTarget(String description){
			this.id = DUMMY_ID;
			this.habitId = DUMMY_ID;
			this.description = description; 
	}
	
	public Uri add(Context context){
		
		if(id != DUMMY_ID){
			throw new IllegalStateException("This HabitTarget object has a valid ID."
					+ " It means it is already present in the local DB. so aborting insert operation.");
		}
		
		//get the values to add
		ContentValues values = new ContentValues();
		values.put(Table_HabitTargets.COLUMN_DESCRIPTION, description);
		values.put(Table_HabitTargets.COLUMN_HABIT_ID, habitId);
		
		//insert into local DB
		return context.getContentResolver().insert(MyContentProvider.URI_HABIT_TARGETS,values);
		
	}
	
	/*Function to postpone the habit target by setting the postponed fields in the local DB
	 */
	/*public boolean postpone(Context context){
		
		ContentValues values = new ContentValues();
		values.put(Table_HabitTargets.COLUMN_POSTPONED_TIME, Utilities.getCurrentTimeFormatted());
		
		//update the local DB
		context.getContentResolver().update(Uri.parse(MyContentProvider.URI_HABIT_TARGETS + "/" + id), values, null,null);
		
		return true;
	}*/
	
	/*Function to achieve the habit target by setting the achieved fields in the local DB
	 */
	/*public boolean achieve(Context context){
		
		ContentValues values = new ContentValues();
		values.put(Table_HabitTargets.COLUMN_ACHIEVED_TIME, Utilities.getCurrentTimeFormatted());
		
		//update the local DB
		context.getContentResolver().update(Uri.parse(MyContentProvider.URI_HABIT_TARGETS + "/" + id), values, null,null);
		
		return true;
	}*/
	
	/*Function to delete the habit target by setting the delete fields in the local DB
	 */
	/*public boolean delete(Context context){
		
		ContentValues values = new ContentValues();
		values.put(Table_Habits.COLUMN_DELETED,Table_Habits.DELETED);
		values.put(Table_Habits.COLUMN_DELETED_TIME, Utilities.getCurrentTimeFormatted());
		
		//update the local DB
		context.getContentResolver().update(Uri.parse(MyContentProvider.URI_HABIT_TARGETS + "/" + id), values, null,null);
		
		return true;
	}*/
	
	
}