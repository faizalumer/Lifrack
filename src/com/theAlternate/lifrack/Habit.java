package com.theAlternate.lifrack;

import java.util.Date;

public class Habit{
	private static final long DUMMY_VALUE = -1;
	private long id;
	private Date createdTime;
	private String name;
	
	//private HabitTarget habitTarget;
	//private Schedule habitSchedule;
	//private ArrayList<String> reminderList;
	//constructor to create object from existing habit in local DB
	/*public Habit(long id){
		this.id = id;
	}*/
	
	//constructor to create object from existing habit in local DB
	public Habit(long id, String name, Date createdTime){
		this.id = id;
		this.name = name;
		this.createdTime = createdTime;
	}
	
	//constructor to create object NOT existing in local DB
	public Habit(String name, Date createdTime){
		this.id = DUMMY_VALUE;
		this.name = name;
		this.createdTime = createdTime;
		
	}
	
	public boolean hasValidId(){
		if(id == DUMMY_VALUE) return false;
		else return true;
	}
	
	public long getId(){
		if(!hasValidId()) throw new IllegalStateException("this habit object does not have a valid ID");
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public Date getCreatedTime(){
		return createdTime;
	}
	
	/*//constructor to create object for a new habit that does not exist in the local DB yet
	public Habit(String name, Schedule habitSchedule, HabitTarget habitTarget, ArrayList<String> reminderList){
		this.id = DUMMY_VALUE;
		this.name = name;
		this.habitSchedule = habitSchedule;
		this.habitTarget = habitTarget;
		this.reminderList = reminderList; 
	}*/
	
	//create a new Habit in the local DB
	/*public static boolean create(String name, Schedule schedule, ArrayList<Reminder> reminderList){
		//public static boolean create(Context context,String name, Schedule habitSchedule, HabitTarget habitTarget, ArrayList<String> reminderList){
		if(this.id != DUMMY_VALUE){
			throw new IllegalStateException("Cannot create this habit in the local DB as this habit object already has an ID.");
		}
		//LocalDBHelper localDBHelper = new LocalDBHelper(context);
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();

		int reminderCount = 0;
		String now = Utilities.getCurrentTimeFormattedUTC(); 
		boolean isSuccess = true;
		db.beginTransaction();
		try {
			ContentValues values = new ContentValues();
			
			//insert habit row
			values.put(Table_Habits.COLUMN_NAME, name);
			values.put(Table_Habits.COLUMN_CREATED_TIME, now);
			long habitId = db.insertOrThrow(Table_Habits.TABLE_NAME, null, values);
			
			//insert habit schedule row
			
			if(schedule != null){
				values.clear();
				values.put(Table_HabitSchedules.COLUMN_HABIT_ID, habitId); 
				values.put(Table_HabitSchedules.COLUMN_CREATED_TIME, now);
				values.put(Table_HabitSchedules.COLUMN_DAILY_FREQUENCY, schedule.dailyFrequency);
				values.put(Table_HabitSchedules.COLUMN_MONDAY, schedule.monday?1:0);
				values.put(Table_HabitSchedules.COLUMN_TUESDAY, schedule.tuesday?1:0);
				values.put(Table_HabitSchedules.COLUMN_WEDNESDAY, schedule.wednesday?1:0);
				values.put(Table_HabitSchedules.COLUMN_THURSDAY, schedule.thursday?1:0);
				values.put(Table_HabitSchedules.COLUMN_FRIDAY, schedule.friday?1:0);
				values.put(Table_HabitSchedules.COLUMN_SATURDAY, schedule.saturday?1:0);
				values.put(Table_HabitSchedules.COLUMN_SUNDAY, schedule.sunday?1:0);
				values.put(Table_HabitSchedules.COLUMN_WEEK_FREQUENCY, schedule.weekFrequency);
				db.insertOrThrow(Table_HabitSchedules.TABLE_NAME, null, values);
				
				//insert reminder rows
				reminderCount = reminderList.size();
				for(int i=0;i<reminderCount;i++){
					values.clear();
					values.put(Table_HabitReminders.COLUMN_HABIT_ID, habitId); 
					values.put(Table_HabitReminders.COLUMN_CREATED_TIME, now);
					values.put(Table_HabitReminders.COLUMN_REMINDER_TIME, reminderList.get(i).getFormattedTime());
					db.insertOrThrow(Table_HabitReminders.TABLE_NAME, null, values);
				}
				
				
			}
			
			db.setTransactionSuccessful();
		} catch(Exception e){
			isSuccess = false;
		}finally {
			db.endTransaction();
			//localDBHelper.close();
			if(reminderCount > 0) Reminder.setNextReminderAlarm();
		}
		return isSuccess;
	}*/
	
	//return the count of habits that have been archived
	/*public static int getArchivedCount(){
		//LocalDBHelper localDBHelper = new LocalDBHelper(context);
		SQLiteDatabase db = LocalDBHelper.getInstance().getReadableDatabase();

		Cursor cur = db.query(Table_HabitHitsHistory.TABLE_NAME, new String[]{"count(1)"}, null, null, null, null, null);
		cur.moveToFirst();
		int result = cur.getInt(0);
		cur.close();
		//localDBHelper.close();
		
		return result;
	}*/
	
	
	
	/*public boolean hasOpenTarget(){
		boolean result = false;
		//LocalDBHelper localDBHelper = new LocalDBHelper(context);
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		
		Cursor cur = db.query(Table_HabitTargets.TABLE_NAME, new String[]{"1"}
		, Table_HabitTargets.COLUMN_HABIT_ID + "=? AND " + Table_HabitTargets.COLUMN_ACHIEVED_TIME + " IS NULL"
		, new String[]{String.valueOf(id)}
		, null, null, null);
		if(cur.getCount() > 0) result = true;
		
		cur.close();
		//localDBHelper.close();
		
		return result;
	}*/
	
	/*public boolean hasOpenHitSession(){
		boolean result = false;
		//LocalDBHelper localDBHelper = new LocalDBHelper(context);
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		
		Cursor cur = db.query(Table_HabitHitSessions.TABLE_NAME, new String[]{"1"}
		, Table_HabitHitSessions.COLUMN_HABIT_ID + "=? AND " + Table_HabitHitSessions.COLUMN_END_TIME + " =?"
		, new String[]{String.valueOf(id),Table_HabitHitSessions.NOT_ENDED}
		, null, null, null);
		if(cur.getCount() > 0) result = true;
		
		cur.close();
		//localDBHelper.close();
		
		return result;
	}*/
	
/*	// increase the hit counter for this habit in the local DB
	public void hit() {
		if (id == DUMMY_VALUE) {
			throw new IllegalStateException(
					"Cannot increase the hit counter of this habit "
							+ "in the local DB as the ID of this object is the dummy value.");
		}

		//LocalDBHelper localDBHelper = new LocalDBHelper(context);
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		ContentValues values = new ContentValues();

		// insert habit hit row
		values.put(Table_HabitHits.COLUMN_HABIT_ID, id);
		//values.put(Table_HabitHits.COLUMN_HIT_TIME, now);
		long hitId = db.insertOrThrow(Table_HabitHits.TABLE_NAME, null, values);
		if (hitId == -1)
			throw new RuntimeException("hit insert failed for habit id=" + id);
		
		//localDBHelper.close();

		
	}*/
	
	// Start a hit session for this habit in the local DB
	/*public void startHitSession() {
		if (id == DUMMY_VALUE) {
			throw new IllegalStateException(
					"Cannot start hit session for this habit "
							+ "in the local DB as the ID of this object is the dummy value.");
		}

		//LocalDBHelper localDBHelper = new LocalDBHelper(context);
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		ContentValues values = new ContentValues();

		// insert session row
		values.put(Table_HabitHitSessions.COLUMN_HABIT_ID, id);
		long sessionId = db.insertOrThrow(Table_HabitHitSessions.TABLE_NAME, null, values);
		if (sessionId == -1){
			//probably a unique constraint violation occured because the existing session
			//has not been ended yet. 
			throw new RuntimeException("session insert failed for habit id=" + id);
		}
		//localDBHelper.close();

	}*/
	
	// Get hit session start time for this habit from the local DB
	/*public Date getHitSessionStartTime() {
		if (id == DUMMY_VALUE) {
			throw new IllegalStateException(
					"Cannot start hit session for this habit "
							+ "in the local DB as the ID of this object is the dummy value.");
		}

		Date result = null;
		//LocalDBHelper localDBHelper = new LocalDBHelper(context);
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();

		// get session row
		Cursor cur = db.query(Table_HabitHitSessions.TABLE_NAME
				//, new String[]{"datetime(" + Table_HabitHitSessions.COLUMN_START_TIME + ",'localtime')"}
				, new String[]{Table_HabitHitSessions.COLUMN_START_TIME}
				, Table_HabitHitSessions.COLUMN_HABIT_ID + "=? AND " + Table_HabitHitSessions.COLUMN_END_TIME + "=?"
				, new String[]{String.valueOf(this.id),Table_HabitHitSessions.NOT_ENDED}
				, null, null, null);
		
		if(cur.getCount() != 1){
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"getHitSessionStartTime : expecting 1 row. but returned " + cur.getCount());}
		}
		else{
			cur.moveToFirst();
			try {
				result = Utilities.getSimpleDateFormatUTC().parse(cur.getString(0));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		cur.close();
		//localDBHelper.close();
		return result;

	}*/
	
	/*// cancel a hit session for this habit in the local DB
	public boolean cancelHitSession() {
		if (id == DUMMY_VALUE) {
			throw new IllegalStateException(
					"Cannot cancel hit session for this habit "
							+ "in the local DB as the ID of this object is the dummy value.");
		}

		//LocalDBHelper localDBHelper = new LocalDBHelper(context);
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		boolean isSuccess = true;
		db.beginTransaction();
		try{
		// delete session row
		int rows = db.delete(Table_HabitHitSessions.TABLE_NAME
				, Table_HabitHitSessions.COLUMN_HABIT_ID + "=? AND " + Table_HabitHitSessions.COLUMN_END_TIME + "=?" 
				, new String[]{String.valueOf(id),Table_HabitHitSessions.NOT_ENDED});

		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"cancelHitSession : " + rows + " deleted");}
		
		if (rows != 1)
			throw new RuntimeException("hit session delete failed for habit id=" + id);
		
		db.setTransactionSuccessful();
		}
		catch(Exception e){
			isSuccess = false;
			e.printStackTrace();
			}
		finally{
			db.endTransaction();
			//localDBHelper.close();	
		}
		
		return isSuccess;
		
		

	}
	*/
	// end a hit session for this habit in the local DB
	/*public boolean endHitSession() {
		if (id == DUMMY_VALUE) {
			throw new IllegalStateException(
					"Cannot end the hit session for this habit "
							+ "in the local DB as the ID of this object is the dummy value.");
		}

		//LocalDBHelper localDBHelper = new LocalDBHelper(context);
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		boolean isSuccess = true;
		db.beginTransaction();
		try{
			//insert hit row
			ContentValues values = new ContentValues();
			values.put(Table_HabitHits.COLUMN_HABIT_ID, id);
			String now = Utilities.getCurrentTimeFormattedUTC();
			values.put(Table_HabitHits.COLUMN_HIT_TIME, now);
			long hitId = db.insertOrThrow(Table_HabitHits.TABLE_NAME, null, values);
			if (hitId == -1)
				throw new RuntimeException("hit insert failed for habit id=" + id);

			//update session row with end time
			values = new ContentValues();
			values.put(Table_HabitHitSessions.COLUMN_HIT_ID, hitId);
			values.put(Table_HabitHitSessions.COLUMN_END_TIME, now);
			int rows = db.update(Table_HabitHitSessions.TABLE_NAME, values
					, Table_HabitHitSessions.COLUMN_HABIT_ID + "=? AND " + Table_HabitHitSessions.COLUMN_END_TIME + "=?" 
					, new String[]{String.valueOf(id),Table_HabitHitSessions.NOT_ENDED});
			
			if (rows != 1)
				throw new RuntimeException("hit session update failed for habit id=" + id);
			
			db.setTransactionSuccessful();
		}
		catch(Exception e){
			isSuccess = false;
			e.printStackTrace();
		}
		finally{
			db.endTransaction();
			//localDBHelper.close();
		}
		
		if(BuildConfig.DEBUG){if(isSuccess) Log.d(LOG_TAG,"endHitSession success");}
		return isSuccess;
	}*/
	


	
	/*public boolean delete(){
		
		if(this.id == DUMMY_VALUE){
			throw new IllegalStateException("Cannot delete this habit in the local DB as this habit object's ID is a dummy value.");
		}
		//LocalDBHelper localDBHelper = new LocalDBHelper(context);
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		boolean isSuccess = true;
		db.beginTransaction();
		try {

			String[] selectArgs = {String.valueOf(id)};
			//delete hit sessions
			db.delete(Table_HabitHitSessions.TABLE_NAME, Table_HabitHitSessions.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete hits
			db.delete(Table_HabitHits.TABLE_NAME, Table_HabitHits.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete targets
			db.delete(Table_HabitTargets.TABLE_NAME, Table_HabitTargets.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete targets history
			//db.delete(Table_HabitTargetsHistory.TABLE_NAME, Table_HabitTargetsHistory.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete schedules
			db.delete(Table_HabitSchedules.TABLE_NAME, Table_HabitSchedules.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete schedules history
			//db.delete(Table_HabitSchedulesHistory.TABLE_NAME, Table_HabitSchedulesHistory.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete reminders
			int reminderCount = db.delete(Table_HabitReminders.TABLE_NAME, Table_HabitReminders.COLUMN_HABIT_ID + "=?", selectArgs);
			
			//delete habit
			int habitCount = db.delete(Table_Habits.TABLE_NAME, Table_Habits.COLUMN_ID + "=?", selectArgs);
			
			if(habitCount != 1) throw new RuntimeException("delete failed for habit id " + id 
					+ ". The expected number of deleted rows in habit table is 1. Instead " + habitCount + "rows were deleted"
					+ "The transaction has been rolled back.");
			
			db.setTransactionSuccessful();
			if(reminderCount > 0) Reminder.setNextReminderAlarm();
			
		} catch(Exception e){
			isSuccess = false;
			e.printStackTrace();
		}finally {
			db.endTransaction();
			//localDBHelper.close();
		}
		return isSuccess;
	}*/
	
	/*public boolean archive(){
			
			if(this.id == DUMMY_VALUE){
				throw new IllegalStateException("Cannot archive this habit in the local DB as this habit object's ID is a dummy value.");
			}
			//LocalDBHelper localDBHelper = new LocalDBHelper(context);
			SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
			boolean isSuccess = true;
			String sql = null;
			String[] bindArgs = {String.valueOf(id)};
			db.beginTransaction();
			try {
				//insert hit sessions into history table
				sql = "INSERT INTO " + Table_HabitHitSessionsHistory.TABLE_NAME
						+ "(" + Table_HabitHitSessionsHistory.COLUMN_SESSION_ID
						+ ", " + Table_HabitHitSessionsHistory.COLUMN_HIT_ID
						+ ", " + Table_HabitHitSessionsHistory.COLUMN_HABIT_ID
						+ ", " + Table_HabitHitSessionsHistory.COLUMN_START_TIME
						+ ", " + Table_HabitHitSessionsHistory.COLUMN_END_TIME
						+ ")"
						+ " SELECT "
						+ Table_HabitHitSessions.COLUMN_ID
						+ ", " + Table_HabitHitSessions.COLUMN_HIT_ID
						+ ", " + Table_HabitHitSessions.COLUMN_HABIT_ID
						+ ", " + Table_HabitHitSessions.COLUMN_START_TIME
						+ ", " + Table_HabitHitSessions.COLUMN_END_TIME
						+ " FROM " + Table_HabitHitSessions.TABLE_NAME
						+ " WHERE " + Table_HabitHitSessions.COLUMN_HABIT_ID + "=?"
						;
				db.execSQL(sql, bindArgs);
				
				//delete hits
				db.delete(Table_HabitHitSessions.TABLE_NAME, Table_HabitHitSessions.COLUMN_HABIT_ID + "=?", bindArgs);
				
				//insert hits into history table
				sql = "INSERT INTO " + Table_HabitHitsHistory.TABLE_NAME
						+ "(" + Table_HabitHitsHistory.COLUMN_HIT_ID
						+ ", " + Table_HabitHitsHistory.COLUMN_HABIT_ID
						+ ", " + Table_HabitHitsHistory.COLUMN_HIT_TIME
						+ ")"
						+ " SELECT "
						+ Table_HabitHits.COLUMN_ID
						+ ", " + Table_HabitHits.COLUMN_HABIT_ID
						+ ", " + Table_HabitHits.COLUMN_HIT_TIME
						+ " FROM " + Table_HabitHits.TABLE_NAME
						+ " WHERE " + Table_HabitHits.COLUMN_HABIT_ID + "=?"
						;
				db.execSQL(sql, bindArgs);
				
				//delete hits
				db.delete(Table_HabitHits.TABLE_NAME, Table_HabitHits.COLUMN_HABIT_ID + "=?", bindArgs);
				
				//insert targets into history table
				sql = "INSERT INTO " + Table_HabitTargetsHistory.TABLE_NAME
						+ "(" + Table_HabitTargetsHistory.COLUMN_HABIT_TARGET_ID
						+ ", " + Table_HabitTargetsHistory.COLUMN_HABIT_ID
						+ ", " + Table_HabitTargetsHistory.COLUMN_DESCRIPTION
						+ ", " + Table_HabitTargetsHistory.COLUMN_CREATED_TIME
						+ ", " + Table_HabitTargetsHistory.COLUMN_ACHIEVED_TIME
						+ ")"
						+ " SELECT "
						+ Table_HabitTargets.COLUMN_ID
						+ ", " + Table_HabitTargets.COLUMN_HABIT_ID
						+ ", " + Table_HabitTargets.COLUMN_DESCRIPTION
						+ ", " + Table_HabitTargets.COLUMN_CREATED_TIME
						+ ", " + Table_HabitTargets.COLUMN_ACHIEVED_TIME
						+ " FROM " + Table_HabitTargets.TABLE_NAME
						+ " WHERE " + Table_HabitTargets.COLUMN_HABIT_ID + "=?"
						;
				db.execSQL(sql, bindArgs);
				
				//delete targets
				int count = db.delete(Table_HabitTargets.TABLE_NAME, Table_HabitTargets.COLUMN_HABIT_ID + "=?", bindArgs);
				
				if(count > 1) throw new RuntimeException("archive failed for habit id " + id 
						+ ". The expected maximum number of deleted rows in target table is 1. Instead " + count + "rows were deleted"
						+ "The transaction has been rolled back.");
				
				//insert schedule into history table
				sql = "INSERT INTO " + Table_HabitSchedulesHistory.TABLE_NAME
						+ "(" + Table_HabitSchedulesHistory.COLUMN_HABIT_SCHEDULE_ID
						+ ", " + Table_HabitSchedulesHistory.COLUMN_HABIT_ID
						+ ", " + Table_HabitSchedulesHistory.COLUMN_DAILY_FREQUENCY
						+ ", " + Table_HabitSchedulesHistory.COLUMN_MONDAY
						+ ", " + Table_HabitSchedulesHistory.COLUMN_TUESDAY
						+ ", " + Table_HabitSchedulesHistory.COLUMN_WEDNESDAY
						+ ", " + Table_HabitSchedulesHistory.COLUMN_THURSDAY
						+ ", " + Table_HabitSchedulesHistory.COLUMN_FRIDAY
						+ ", " + Table_HabitSchedulesHistory.COLUMN_SATURDAY
						+ ", " + Table_HabitSchedulesHistory.COLUMN_SUNDAY
						+ ", " + Table_HabitSchedulesHistory.COLUMN_WEEK_FREQUENCY
						+ ", " + Table_HabitSchedulesHistory.COLUMN_CREATED_TIME
						+ ", " + Table_HabitSchedulesHistory.COLUMN_INVALIDATED_TIME
						+ ")"
						+ " SELECT "
						+ Table_HabitSchedules.COLUMN_ID
						+ ", " + Table_HabitSchedules.COLUMN_HABIT_ID
						+ ", " + Table_HabitSchedules.COLUMN_DAILY_FREQUENCY
						+ ", " + Table_HabitSchedules.COLUMN_MONDAY
						+ ", " + Table_HabitSchedules.COLUMN_TUESDAY
						+ ", " + Table_HabitSchedules.COLUMN_WEDNESDAY
						+ ", " + Table_HabitSchedules.COLUMN_THURSDAY
						+ ", " + Table_HabitSchedules.COLUMN_FRIDAY
						+ ", " + Table_HabitSchedules.COLUMN_SATURDAY
						+ ", " + Table_HabitSchedules.COLUMN_SUNDAY
						+ ", " + Table_HabitSchedules.COLUMN_WEEK_FREQUENCY
						+ ", " + Table_HabitSchedules.COLUMN_CREATED_TIME
						+ ", " + Table_HabitSchedules.COLUMN_INVALIDATED_TIME
						+ " FROM " + Table_HabitSchedules.TABLE_NAME
						+ " WHERE " + Table_HabitSchedules.COLUMN_HABIT_ID + "=?"
						;
				db.execSQL(sql, bindArgs);
				
				//delete schedule
				count = db.delete(Table_HabitSchedules.TABLE_NAME, Table_HabitSchedules.COLUMN_HABIT_ID + "=?", bindArgs);
				
				if(count > 1) throw new RuntimeException("archive failed for habit id " + id 
						+ ". The expected maximum number of deleted rows in schedule table is 1. Instead " + count + "rows were deleted"
						+ "The transaction has been rolled back.");
				
				//delete reminders(there is no history table for reminders, so doing only deletion)
				int reminderCount = db.delete(Table_HabitReminders.TABLE_NAME, Table_HabitReminders.COLUMN_HABIT_ID + "=?", bindArgs);
				
				//insert habit into history table
				sql = "INSERT INTO " + Table_HabitsHistory.TABLE_NAME
						+ "(" + Table_HabitsHistory.COLUMN_HABIT_ID
						+ ", " + Table_HabitsHistory.COLUMN_NAME
						+ ", " + Table_HabitsHistory.COLUMN_CREATED_TIME
						+ ", " + Table_HabitsHistory.COLUMN_ARCHIVED_TIME
						+ ")"
						+ " SELECT "
						+ Table_Habits.COLUMN_ID
						+ ", " + Table_Habits.COLUMN_NAME
						+ ", " + Table_Habits.COLUMN_CREATED_TIME
						+ ", '" + Utilities.getCurrentTimeFormattedUTC() + "'"
						+ " FROM " + Table_Habits.TABLE_NAME
						+ " WHERE " + Table_Habits.COLUMN_ID + "=?"
						;
				db.execSQL(sql, bindArgs);
				
				//delete habit
				int count = db.delete(Table_Habits.TABLE_NAME, Table_Habits.COLUMN_ID + "=?", bindArgs);
				
				if(count != 1) throw new RuntimeException("archive failed for habit id " + id 
						+ ". The expected number of deleted rows in habit table is 1. Instead " + count + "rows were deleted"
						);
				
				db.setTransactionSuccessful();
				if(reminderCount > 0) Reminder.setNextReminderAlarm();
			} catch(Exception e){
				isSuccess = false;
				if(BuildConfig.DEBUG){
					Log.d(LOG_TAG,"archive failed.will be rolled back");
					e.printStackTrace();
				}
			}finally {
				db.endTransaction();
				//localDBHelper.close();
			}
			return isSuccess;
		}*/
	
	
	
	/*public HabitEditableContent getEditableContent(){
		if(this.id == DUMMY_VALUE){
			throw new IllegalStateException("Cannot get Editor for this habit as this habit object's ID is a dummy value.");
		}
		
		Schedule schedule = new ScheduleDaoImpl().getLatestScheduleByHabitId(this.id);
		String habitName = getName();dfdfb
		Reminder[] reminderArr = getReminders();
		
		//HabitEditableContent result = new HabitEditableContent(habitName,targetDescription,habitSchedule,reminders);
		HabitEditableContent result = new HabitEditableContent(habitName,schedule,reminderArr);
		
		return result;
		
	}*/
	
	/*public Reminder[] getReminders(){
		if(this.id == DUMMY_VALUE){
			throw new IllegalStateException("the habit object's ID is a dummy value.");
		}
		
		//LocalDBHelper localDBHelper = new LocalDBHelper(context);
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
				
		//get the reminders from local DB
		String[] columns = new String[] {
				Table_HabitReminders.COLUMN_ID
				,Table_HabitReminders.COLUMN_REMINDER_TIME
			};
		
		final int COLUMN_INDEX_REMINDERID = 0;
		final int COLUMN_INDEX_REMINDERTIME = 1;
		
		String selection = Table_HabitReminders.COLUMN_HABIT_ID + "=?";
		String[] selectionArgs =new String[] {String.valueOf(this.id)};
		Cursor cur = db.query(Table_HabitReminders.TABLE_NAME,columns,selection,selectionArgs,null,null,null);

		//insert the retrieved reminders into a set of entries
		Reminder[] reminderArr = null;
		if(cur.getCount() > 0){
			reminderArr = new Reminder[cur.getCount()]; 
		}
		int i=0;
		while(cur.moveToNext()){
			reminderArr[i++] = new Reminder(cur.getLong(COLUMN_INDEX_REMINDERID), this.id, cur.getString(COLUMN_INDEX_REMINDERTIME));
		}
		cur.close();
		//localDBHelper.close();
		return reminderArr;
		
	}*/
	
	/*public String getName(){
		if(this.id == DUMMY_VALUE){
			throw new IllegalStateException("the habit object's ID is a dummy value.");
		}
		
		//LocalDBHelper localDBHelper = new LocalDBHelper(context);
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
				
		String[] columns = new String[] {
				Table_Habits.COLUMN_NAME
			};
		final int COLUMN_INDEX_NAME = 0;
		String selection = Table_Habits.COLUMN_ID + "=?";
		String[] selectionArgs =new String[] {String.valueOf(this.id)};
		Cursor cur = db.query(Table_Habits.TABLE_NAME,columns,selection,selectionArgs,null,null,null);
		cur.moveToFirst();
		String result = cur.getString(COLUMN_INDEX_NAME);

		cur.close();
		//localDBHelper.close();
		return result;
		
	}*/
	
/*	public boolean getScheduleStatus(){
		if(this.id == DUMMY_VALUE){
			throw new IllegalStateException("the habit object's ID is a dummy value.");
		}
		
		//LocalDBHelper localDBHelper = new LocalDBHelper(MyApplication.getContext());
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
				
		String[] columns = new String[] {
				Table_Habits.COLUMN_IS_SCHEDULE_ENABLED
			};
		String selection = Table_Habits.COLUMN_ID + "=?";
		String[] selectionArgs =new String[] {String.valueOf(this.id)};
		Cursor cur = db.query(Table_Habits.TABLE_NAME,columns,selection,selectionArgs,null,null,null);
		cur.moveToFirst();
		boolean result = (cur.getInt(0) == 1) ? true:false;

		cur.close();
		//localDBHelper.close();
		return result;
		
	}*/
	
	/*public String getTarget(){
		if(this.id == DUMMY_VALUE){
			throw new IllegalStateException("the habit object's ID is a dummy value.");
		}
		
		//LocalDBHelper localDBHelper = new LocalDBHelper(context);
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
				
		String[] columns = new String[] {
				Table_HabitTargets.COLUMN_DESCRIPTION
			};
		final int COLUMN_INDEX_DESCRIPTION = 0;
		String selection = Table_HabitTargets.COLUMN_HABIT_ID + "=? AND " + Table_HabitTargets.COLUMN_ACHIEVED_TIME + " IS NULL";
		String[] selectionArgs =new String[] {String.valueOf(this.id)};
		Cursor cur = db.query(Table_HabitTargets.TABLE_NAME,columns,selection,selectionArgs,null,null,null);

		String result = null;
		if(cur.moveToFirst()){
			result = cur.getString(COLUMN_INDEX_DESCRIPTION);
		}
		cur.close();
		//localDBHelper.close();
		return result;
		
	}*/
	
	/*public void achieveTarget(){
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"achieveTarget : START");}
		
		if(this.id == DUMMY_VALUE){
			throw new IllegalStateException("the habit object's ID is a dummy value.");
		}
		
		//LocalDBHelper localDBHelper = new LocalDBHelper(context);
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		
		String now = Utilities.getCurrentTimeFormattedUTC();
		
		ContentValues values = new ContentValues();
		values.put(Table_HabitTargets.COLUMN_ACHIEVED_TIME, now);
		db.update(Table_HabitTargets.TABLE_NAME, values
				, Table_HabitTargets.COLUMN_HABIT_ID + "=? AND " + Table_HabitTargets.COLUMN_ACHIEVED_TIME + " IS NULL"  
				, new String[]{String.valueOf(this.id)});
		
		if(BuildConfig.DEBUG){Log.d(LOG_TAG,"achieveTarget : END");}
	}*/
	
	/*public void updateSchedule(Schedule schedule){
		
		if(this.id == DUMMY_VALUE){
			throw new IllegalStateException("This habit object's ID is a dummy value.");
		}
		
		SQLiteDatabase db = null;
		LocalDBHelper localDBHelper = null;
		
		//create a new db connection if it was not passed in as parameter(i.e. null was passed in)
		if(existingDB == null){
			localDBHelper = new LocalDBHelper(context);
			db = localDBHelper.getWritableDatabase();
		}
		else db = existingDB;
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		
		String now = Utilities.getCurrentTimeFormattedUTC();
		
		//invalidate old schedule
		ContentValues values = new ContentValues();
		values.put(Table_HabitSchedules.COLUMN_INVALIDATED_TIME, now);
		db.update(Table_HabitSchedules.TABLE_NAME, values
				,Table_HabitSchedules.COLUMN_HABIT_ID + "=? AND " + Table_HabitSchedules.COLUMN_INVALIDATED_TIME + "=" + Table_HabitSchedules.NOT_INVALIDATED
				, new String[] {String.valueOf(this.id)});
		
		//insert new schedule
		values.clear();
		values.put(Table_HabitSchedules.COLUMN_HABIT_ID, this.id); 
		values.put(Table_HabitSchedules.COLUMN_CREATED_TIME, now);
		values.put(Table_HabitSchedules.COLUMN_DAILY_FREQUENCY, schedule.dailyFrequency);
		values.put(Table_HabitSchedules.COLUMN_MONDAY, schedule.monday?1:0);
		values.put(Table_HabitSchedules.COLUMN_TUESDAY, schedule.tuesday?1:0);
		values.put(Table_HabitSchedules.COLUMN_WEDNESDAY, schedule.wednesday?1:0);
		values.put(Table_HabitSchedules.COLUMN_THURSDAY, schedule.thursday?1:0);
		values.put(Table_HabitSchedules.COLUMN_FRIDAY, schedule.friday?1:0);
		values.put(Table_HabitSchedules.COLUMN_SATURDAY, schedule.saturday?1:0);
		values.put(Table_HabitSchedules.COLUMN_SUNDAY, schedule.sunday?1:0);
		values.put(Table_HabitSchedules.COLUMN_WEEK_FREQUENCY, schedule.weekFrequency);
		db.insertOrThrow(Table_HabitSchedules.TABLE_NAME, null, values);
		
		//close the db connection if it was not passed in as parameter
		if(existingDB == null){
			localDBHelper.close();
		}
		
	}*/
	
	/*public void createSchedule(Schedule schedule){
		
		if(this.id == DUMMY_VALUE){
			throw new IllegalStateException("This habit object's ID is a dummy value.");
		}
		
		SQLiteDatabase db = null;
		LocalDBHelper localDBHelper = null;
		
		//create a new db connection if it was not passed in as parameter(i.e. null was passed in)
		if(existingDB == null){
			localDBHelper = new LocalDBHelper(context);
			db = localDBHelper.getWritableDatabase();
		}
		else db = existingDB;
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		
		String now = Utilities.getCurrentTimeFormattedUTC();
		
		//insert new schedule
		ContentValues values = new ContentValues();
		values.put(Table_HabitSchedules.COLUMN_HABIT_ID, this.id); 
		values.put(Table_HabitSchedules.COLUMN_CREATED_TIME, now);
		values.put(Table_HabitSchedules.COLUMN_DAILY_FREQUENCY, schedule.dailyFrequency);
		values.put(Table_HabitSchedules.COLUMN_MONDAY, schedule.monday?1:0);
		values.put(Table_HabitSchedules.COLUMN_TUESDAY, schedule.tuesday?1:0);
		values.put(Table_HabitSchedules.COLUMN_WEDNESDAY, schedule.wednesday?1:0);
		values.put(Table_HabitSchedules.COLUMN_THURSDAY, schedule.thursday?1:0);
		values.put(Table_HabitSchedules.COLUMN_FRIDAY, schedule.friday?1:0);
		values.put(Table_HabitSchedules.COLUMN_SATURDAY, schedule.saturday?1:0);
		values.put(Table_HabitSchedules.COLUMN_SUNDAY, schedule.sunday?1:0);
		values.put(Table_HabitSchedules.COLUMN_WEEK_FREQUENCY, schedule.weekFrequency);
		db.insertOrThrow(Table_HabitSchedules.TABLE_NAME, null, values);
		
		//close the db connection if it was not passed in as parameter
		if(existingDB == null){
			localDBHelper.close();
		}
		
	}*/
	
	/*public void updateTargetDescription(String newTargetDescription){
		if(this.id == DUMMY_VALUE){
			throw new IllegalStateException("This habit object's ID is a dummy value.");
		}
		
		SQLiteDatabase db = null;
		LocalDBHelper localDBHelper = null;
		
		//create a new db connection if it was not passed in as parameter(i.e. null was passed in)
		if(existingDB == null){
			localDBHelper = new LocalDBHelper(context);
			db = localDBHelper.getWritableDatabase();
		}
		else db = existingDB;
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Table_HabitTargets.COLUMN_DESCRIPTION, newTargetDescription);

		db.update(Table_HabitTargets.TABLE_NAME, values
				, Table_HabitTargets.COLUMN_HABIT_ID + "=? AND " + Table_HabitTargets.COLUMN_ACHIEVED_TIME + " IS NULL" 
				, new String[] {String.valueOf(this.id)});
		
		//close the db connection if it was not passed in as parameter
		if(existingDB == null){
			localDBHelper.close();
		}
	}*/
	
	/*public void createTarget(String targetDescription){
		if(this.id == DUMMY_VALUE){
			throw new IllegalStateException("This habit object's ID is a dummy value.");
		}
		
		SQLiteDatabase db = null;
		LocalDBHelper localDBHelper = null;
		
		//create a new db connection if it was not passed in as parameter(i.e. null was passed in)
		if(existingDB == null){
			localDBHelper = new LocalDBHelper(context);
			db = localDBHelper.getWritableDatabase();
		}
		else db = existingDB;
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Table_HabitTargets.COLUMN_HABIT_ID, this.id); 
		values.put(Table_HabitTargets.COLUMN_CREATED_TIME, Utilities.getCurrentTimeFormattedUTC());
		values.put(Table_HabitTargets.COLUMN_DESCRIPTION, targetDescription);
		long habitTargetId = db.insertOrThrow(Table_HabitTargets.TABLE_NAME, null, values);
		if(habitTargetId == -1) throw new RuntimeException("insert failed for habit target - " + targetDescription);
		
		//close the db connection if it was not passed in as parameter
		if(existingDB == null){
			localDBHelper.close();
		}
		
	}*/
	
/*	public void deleteTarget(){
		if(this.id == DUMMY_VALUE){
			throw new IllegalStateException("This habit object's ID is a dummy value.");
		}
		
		SQLiteDatabase db = null;
		LocalDBHelper localDBHelper = null;
		
		//create a new db connection if it was not passed in as parameter(i.e. null was passed in)
		if(existingDB == null){
			localDBHelper = new LocalDBHelper(context);
			db = localDBHelper.getWritableDatabase();
		}
		else db = existingDB;
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		
		db.delete(Table_HabitTargets.TABLE_NAME
				, Table_HabitTargets.COLUMN_HABIT_ID + "=? AND " + Table_HabitTargets.COLUMN_ACHIEVED_TIME + " IS NULL" 
				, new String[] {String.valueOf(id)});
		
		//close the db connection if it was not passed in as parameter
		if(existingDB == null){
			localDBHelper.close();
		}
	}*/
	
	/*public void updateName(String newName){
		if(this.id == DUMMY_VALUE){
			throw new IllegalStateException("This habit object's ID is a dummy value.");
		}
		
		SQLiteDatabase db = null;
		LocalDBHelper localDBHelper = null;
		
		//create a new db connection if it was not passed in as parameter(i.e. null was passed in)
		if(existingDB == null){
			localDBHelper = new LocalDBHelper(context);
			db = localDBHelper.getWritableDatabase();
		}
		else db = existingDB;
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Table_Habits.COLUMN_NAME, newName);

		db.update(Table_Habits.TABLE_NAME, values, Table_Habits.COLUMN_ID + "=?", new String[] {String.valueOf(this.id)});
		
		//close the db connection if it was not passed in as parameter
		if(existingDB == null){
			localDBHelper.close();
		}
	}*/
	
/*	public void updateScheduleStatus(boolean isScheduleEnabled){
		if(this.id == DUMMY_VALUE){
			throw new IllegalStateException("This habit object's ID is a dummy value.");
		}
		
		SQLiteDatabase db = null;
		LocalDBHelper localDBHelper = null;
		
		//create a new db connection if it was not passed in as parameter(i.e. null was passed in)
		if(existingDB == null){
			localDBHelper = new LocalDBHelper(context);
			db = localDBHelper.getWritableDatabase();
		}
		else db = existingDB;
		SQLiteDatabase db = LocalDBHelper.getInstance().getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Table_Habits.COLUMN_IS_SCHEDULE_ENABLED, isScheduleEnabled == true ? 1:0);

		db.update(Table_Habits.TABLE_NAME, values, Table_Habits.COLUMN_ID + "=?", new String[] {String.valueOf(this.id)});
		
		//close the db connection if it was not passed in as parameter
		if(existingDB == null){
			localDBHelper.close();
		}
	}*/
	
}