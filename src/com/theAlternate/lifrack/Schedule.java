package com.theAlternate.lifrack;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class Schedule{
	
	private static final String LOG_TAG = "Schedule";
	private long id;
	private long habitId;
	private int dailyFrequency;
	private boolean monday;
	private boolean tuesday;
	private boolean wednesday;
	private boolean thursday;
	private boolean friday;
	private boolean saturday;
	private boolean sunday;
	private int weekFrequency;
	private Date createdTime;
	private Date invalidatedTime;
	
	private static final int DUMMY_ID = -1;
	
	//constructor to create object from existing habit in local DB
	/*public Schedule(int id, int habitId, int dailyFrequency, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday, Schedule.WeekFrequency weekFrequency, Schedule.MonthFrequency monthFrequency){
		this.id = id;
		this.habitId = habitId;
		this.dailyFrequency = dailyFrequency;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
		this.weekFrequency = weekFrequency;
		this.monthFrequency = monthFrequency;
	}*/
	
	//constructor to create object for new schedule that does not exist in local DB yet
	/*public Schedule(int habitId, int dailyFrequency, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday, Schedule.WeekFrequency weekFrequency, Schedule.MonthFrequency monthFrequency){
		this.id = DUMMY_ID;
		this.habitId = habitId;
		this.dailyFrequency = dailyFrequency;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
		this.weekFrequency = weekFrequency;
		this.monthFrequency = monthFrequency;
	}*/
	
	//constructor to create object for new schedule that does not exist in local DB yet
	/*public Schedule(int dailyFrequency, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday, int weekFrequency){
			//this.id = DUMMY_ID;
			//this.habitId = DUMMY_ID;
			this.dailyFrequency = dailyFrequency;
			this.monday = monday;
			this.tuesday = tuesday;
			this.wednesday = wednesday;
			this.thursday = thursday;
			this.friday = friday;
			this.saturday = saturday;
			this.sunday = sunday;
			this.weekFrequency = weekFrequency;
			this.createdTime = null;
	}*/
	
	//constructor to create object from existing habit in local DB
	public Schedule(long id, long habitId, int dailyFrequency, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday, int weekFrequency, Date createdTime, Date invalidatedTime){
		this.id = id;
		this.habitId = habitId;
		this.dailyFrequency = dailyFrequency;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
		this.weekFrequency = weekFrequency;
		this.createdTime = createdTime;
		this.invalidatedTime = invalidatedTime;
		/*try {
			this.createdTime = Utilities.getSimpleDateFormatUTC().parse(createdTime);
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"test");}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			this.invalidatedTime = Utilities.getSimpleDateFormatUTC().parse(invalidatedTime);
			if(BuildConfig.DEBUG){Log.d(LOG_TAG,"test");}
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
	}
	
	//constructor to create new object NOT existing habit in local DB
	public Schedule(long habitId, int dailyFrequency, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday, int weekFrequency, Date createdTime){
		this.id = DUMMY_ID;
		this.habitId = habitId;
		this.dailyFrequency = dailyFrequency;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
		this.weekFrequency = weekFrequency;
		this.createdTime = createdTime;
		this.invalidatedTime = null;
	}
	
	public boolean hasValidId(){
		if(id==DUMMY_ID) return false;
		else return true;
	}
	
	public long getId(){
		if(!hasValidId()) throw new IllegalStateException("This scheduled does not have an ID.");
		return id;
	}
	
	/*public boolean hasValidHabitId(){
		if(habitId==DUMMY_ID) return false;
		else return true;
	}*/
	
	public long getHabitId(){
		//if(!hasValidHabitId()) throw new IllegalStateException("This scheduled does not have a habit ID.");
		return habitId;
	}
	
	public boolean isInvalidated(){
		if(invalidatedTime==null) return false;
		else return true;
	}
	
	public Date getInvalidatedTime(){
		return invalidatedTime;
	}
	
	public void invalidate(){
		if(isInvalidated()) throw new IllegalStateException("This schedule is already invalidated. Cannot change the invalidated time");
		this.invalidatedTime = new Date();
	}
	
	public boolean getMonday(){
		return monday;
	}
	
	public boolean getTuesday(){
		return tuesday;
	}
	
	public boolean getWednesday(){
		return wednesday;
	}
	
	public boolean getThursday(){
		return thursday;
	}
	
	public boolean getFriday(){
		return friday;
	}
	
	public boolean getSaturday(){
		return saturday;
	}
	
	public boolean getSunday(){
		return sunday;
	}
	
	public int getDailyFrequency(){
		return dailyFrequency;
	}
	
	public int getWeekFrequency(){
		return weekFrequency;
	}
	
	public Date getCreatedTime(){
		return createdTime;
	}
	
	
	/*Calendar does not follow ISO standards
	it considers Sunday as 1, Monday as 2..
	so adjusting for that by subtracting 1 from the DAY_OF_WEEK*/
	public static int getDayOfWeek(Calendar cal){
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if(dayOfWeek == 0) dayOfWeek = 7; //Sunday will be calculated to 0. so setting it to 7 manually
		return dayOfWeek;
	}
	
	private boolean[] getScheduleDaysOfWeek(){
		//Using [8] so that monday is 1 and sunday is 7. So skipping [0] 
		//in the array for better readability.
		boolean[] scheduledDays = new boolean[8];  
		scheduledDays[1] = monday;
		scheduledDays[2] = tuesday;
		scheduledDays[3] = wednesday;
		scheduledDays[4] = thursday;
		scheduledDays[5] = friday;
		scheduledDays[6] = saturday;
		scheduledDays[7] = sunday;
		
		return scheduledDays;
	}
	
	/*go through the list of scheduled day-of-week 
	to find which day is closest in the future(including the specified day)
	from the creation day-of-week*/
	private Integer getEarliestScheduleDayInSameWeekFromDay(int fromDay){
		Integer result = null;
		
		boolean[] scheduledDays = getScheduleDaysOfWeek();
		for(int i=fromDay;i<=7;i++){
			if(scheduledDays[i]){
				result = i;
				break;
			}
		}
		return result;
	}
	
	public static Calendar getMondayOfWeek(Calendar cal){
		Calendar result = (Calendar) cal.clone();
		result.add(Calendar.DAY_OF_MONTH, 1 - getDayOfWeek(cal));
		return result;
		
	}
	
	
	/*returns the next schedule date 
	 * including if it's the current day
	 * */
	public Calendar getNextScheduledDate(Calendar fromDay) throws ParseException{
		
		/*if null parameter was supplied,
		assume it as now*/
		if(fromDay == null) fromDay = Calendar.getInstance();
		
		Calendar earliestScheduledDate = null;
		
		/*if the first schedule date is in fromDay week
		*, try to get a scheduled date in the same week itself*/ 
		Calendar firstScheduleDate = getFirstScheduleDate();
		Calendar mondayOfFirstScheduledDateWeek = getMondayOfWeek(firstScheduleDate);
		Calendar mondayOf_fromDay_Week = getMondayOfWeek(fromDay);
		
		Calendar mondayTemp = (Calendar) mondayOfFirstScheduledDateWeek.clone();
		while(!Utilities.isDateMoreThanOrEqual(mondayTemp, mondayOf_fromDay_Week)){
			mondayTemp.add(Calendar.DAY_OF_MONTH, 7*weekFrequency);
		}
		if(Utilities.isDatesEqual(mondayTemp,mondayOf_fromDay_Week)){
			Integer a = getEarliestScheduleDayInSameWeekFromDay(getDayOfWeek(fromDay));
			if(a != null){
				earliestScheduledDate = (Calendar) fromDay.clone();
				earliestScheduledDate.add(Calendar.DAY_OF_MONTH, a - getDayOfWeek(fromDay));
				return earliestScheduledDate; 
			}
			else{
				mondayTemp.add(Calendar.DAY_OF_MONTH, 7*weekFrequency);
				Integer b = getEarliestScheduleDayInSameWeekFromDay(getDayOfWeek(mondayTemp));
				earliestScheduledDate = (Calendar) mondayTemp.clone();
				earliestScheduledDate.add(Calendar.DAY_OF_MONTH, b - 1);
				return earliestScheduledDate; 
			}
		}
		else{
			Integer a = getEarliestScheduleDayInSameWeekFromDay(getDayOfWeek(mondayTemp));
			earliestScheduledDate = (Calendar) mondayTemp.clone();
			earliestScheduledDate.add(Calendar.DAY_OF_MONTH, a - 1);
			return earliestScheduledDate; 
		}
		
		/*if(Utilities.isDatesEqual(mondayOfFirstScheduledDateWeek,mondayOf_fromDay_Week)){
			Integer a = getEarliestScheduleDayInSameWeekFromDay(getDayOfWeek(fromDay));
			if(a != null){
				earliestScheduledDate = (Calendar) mondayOfFirstScheduledDateWeek.clone();
				earliestScheduledDate.add(Calendar.DAY_OF_MONTH, a - 1);
				return earliestScheduledDate; 
			}
			else{}
			
		}
		
		if the schedule was not created in the fromDay week, 
		 * then check for subsequent scheduled weeks		
		else{
			Calendar mondayTemp = (Calendar) mondayOfFirstScheduledDateWeek.clone();
			while(!Utilities.isDateMoreThanOrEqual(mondayTemp, mondayOf_fromDay_Week)){
				mondayTemp.add(Calendar.DAY_OF_MONTH, 7*weekFrequency);
			}
			
			check if any scheduled day remaining in fromDay week
			 * 
			if(Utilities.isDatesEqual(mondayTemp, mondayOf_fromDay_Week)){
				if(getLastScheduledDayOfWeek() >=  fromDay.get(Calendar.DAY_OF_WEEK)){}
			}
			else{
				earliestScheduledDate = (Calendar) mondayTemp.clone();
				earliestScheduledDate.add(Calendar.DAY_OF_MONTH, getFirstScheduledDayOfWeek() - 1);
				return earliestScheduledDate;
			}
		}*/
	}
	
	/*go through the list of scheduled day-of-week 
	to find which day is the first day*/
	public int getFirstScheduledDayOfWeek() {
		boolean[] scheduledDays = getScheduleDaysOfWeek();
		int firstScheduledDayOfWeek = 0;
		for (int i = 1; i <= 7; i++) {
			if (scheduledDays[i]) {
				firstScheduledDayOfWeek = i;
			}
		}
		return firstScheduledDayOfWeek;
	}
	
	/*go through the list of scheduled day-of-week 
	to find which day is the last day*/
	public int getLastScheduledDayOfWeek() {
		boolean[] scheduledDays = getScheduleDaysOfWeek();
		int lastScheduledDayOfWeek = 7;
		for (int i = 7; i >= 1; i--) {
			if (scheduledDays[i]) {
				lastScheduledDayOfWeek = i;
			}
		}
		return lastScheduledDayOfWeek;
	}
	
	/*returns the date of the first scheduled date 
	 * based on the date the schedule was created
	*/
	public Calendar getFirstScheduleDate() throws ParseException{
		Calendar firstScheduledDate = null;
		
		//get the schedule created time
		//Date dt_createdDate = Utilities.getSimpleDateFormat().parse(createdTime); //createdTime gets converted to local time zone here
		Calendar cal_createdDate = Calendar.getInstance();
		cal_createdDate.setTime(createdTime);
		
		//get the day of week when the schedule was created
		int createdDay = getDayOfWeek(cal_createdDate);
		
		//get the earliest schedule day-of-week in the week that the schedule was created 
		Integer firstScheduledDayOfCreatedWeek = getEarliestScheduleDayInSameWeekFromDay(createdDay);
		
		/*get the date of the day-of-week found just now and calculate the result*/
		if(firstScheduledDayOfCreatedWeek != null){
			firstScheduledDate = (Calendar) cal_createdDate.clone(); 
			firstScheduledDate.add(Calendar.DAY_OF_MONTH, firstScheduledDayOfCreatedWeek - createdDay);
		}
		/*if there are no scheduled day-of-week remaining in the same week as the creation week
		then get the date for the first scheduled day-of-week of next week*/
		else{
			int daysToAdd = getFirstScheduledDayOfWeek() - createdDay + 7;
			firstScheduledDate = (Calendar) cal_createdDate.clone();
			firstScheduledDate.add(Calendar.DAY_OF_MONTH, daysToAdd);
		}
		
		return firstScheduledDate;
	}
	
	public boolean equals(Object other){
		if(other == null) return false;
		if(this.getClass() != other.getClass()) return false;
		
		if(this.dailyFrequency != ((Schedule)other).dailyFrequency) return false;
		if(this.monday != ((Schedule)other).monday) return false;
		if(this.tuesday != ((Schedule)other).tuesday) return false;
		if(this.wednesday != ((Schedule)other).wednesday) return false;
		if(this.thursday != ((Schedule)other).thursday) return false;
		if(this.friday != ((Schedule)other).friday) return false;
		if(this.saturday != ((Schedule)other).saturday) return false;
		if(this.sunday != ((Schedule)other).sunday) return false;
		if(this.weekFrequency != ((Schedule)other).weekFrequency) return false;
		
		return true;
	}
	
	/*public Uri add(Context context){
		
		if(id != DUMMY_ID){
			throw new IllegalStateException("This Schedule object has a valid ID."
					+ " It means it is already present in the local DB. so aborting insert operation.");
		}
		
		//get the values to add
		ContentValues values = new ContentValues();
		values.put(Table_HabitSchedules.COLUMN_HABIT_ID, habitId);
		values.put(Table_HabitSchedules.COLUMN_DAILY_FREQUENCY, dailyFrequency);
		values.put(Table_HabitSchedules.COLUMN_MONDAY, monday);
		values.put(Table_HabitSchedules.COLUMN_TUESDAY, tuesday);
		values.put(Table_HabitSchedules.COLUMN_WEDNESDAY, wednesday);
		values.put(Table_HabitSchedules.COLUMN_THURSDAY, thursday);
		values.put(Table_HabitSchedules.COLUMN_FRIDAY, friday);
		values.put(Table_HabitSchedules.COLUMN_SATURDAY, saturday);
		values.put(Table_HabitSchedules.COLUMN_SUNDAY, sunday);
		values.put(Table_HabitSchedules.COLUMN_WEEK_FREQUENCY, weekFrequency.value());
		values.put(Table_HabitSchedules.COLUMN_MONTH_FREQUENCY, monthFrequency.value());
		
		
		//insert into local DB
		return context.getContentResolver().insert(MyContentProvider.URI_HABIT_SCHEDULES,values);
		
	}*/
	
	/*Function to delete the habit target by setting the delete fields in the local DB
	 */
	/*public boolean delete(Context context){
		
		if(id != DUMMY_ID){
			throw new IllegalStateException("This Schedule object does not has a valid ID."
					+ " It might notbe present in the local DB. so aborting delete operation.");
		}
		
		ContentValues values = new ContentValues();
		values.put(Table_HabitSchedules.COLUMN_DELETED,Table_Habits.DELETED);
		values.put(Table_HabitSchedules.COLUMN_DELETED_TIME, Utilities.getCurrentTimeFormatted());
		
		//update the local DB
		context.getContentResolver().update(Uri.parse(MyContentProvider.URI_HABIT_SCHEDULES + "/" + id), values, null,null);
		
		return true;
	}
	
	Function to delete the habit target by setting the delete fields in the local DB
	 
	public boolean archive(Context context){
		
		if(id != DUMMY_ID){
			throw new IllegalStateException("This Schedule object does not has a valid ID."
					+ " It might notbe present in the local DB. so aborting archive operation.");
		}
		
		ContentValues values = new ContentValues();
		values.put(Table_HabitSchedules.COLUMN_ARCHIVED,Table_Habits.DELETED);
		values.put(Table_HabitSchedules.COLUMN_ARCHIVED_TIME, Utilities.getCurrentTimeFormatted());
		
		//update the local DB
		context.getContentResolver().update(Uri.parse(MyContentProvider.URI_HABIT_SCHEDULES + "/" + id), values, null,null);
		
		return true;
	}*/
	
	
}