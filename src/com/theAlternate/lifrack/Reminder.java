package com.theAlternate.lifrack;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


public class Reminder implements Parcelable{
	private static final long DUMMY_VALUE = -1;
	private long habitId;
	private long id;
	private int hour;
	private int minute;
	
	//constructor to create reminder from database record
	public Reminder(long id, long habitId, int hour, int minute){
		this.id = id;
		this.habitId = habitId;
		this.hour = hour;
		this.minute = minute;
	}
	
	//constructor to create a new reminder that does not exist in database
	public Reminder(int hour, int minute){
		this.id = DUMMY_VALUE;
		this.habitId = DUMMY_VALUE;
		this.hour = hour;
		this.minute = minute;
	}
	
	//constructor to create a new reminder that does not exist in database for an existing habit
	public Reminder(long habitId, int hour, int minute){
		this.id = DUMMY_VALUE;
		this.habitId = habitId;
		this.hour = hour;
		this.minute = minute;
	}
	
	@Override
	public boolean equals(Object other){
		if(other == null) return false;
		if(this.getClass() != other.getClass()) return false;
		Reminder otherReminder = (Reminder) other;
		//if(this.id != otherReminder.id) return false;
		//if(this.habitId != otherReminder.habitId) return false;
		if(this.hour != otherReminder.hour) return false;
		if(this.minute != otherReminder.minute) return false;
		
		return true;
	}
	
	public void setTime(int hour, int minute){
		this.hour = hour;
		this.minute = minute;
	}
	
	public String getFormattedTime(){
		return new StringBuilder().append(Utilities.padSingleDigit(hour)).append(":").append(Utilities.padSingleDigit(minute)).toString();
	}
	
	public int getHour(){
		return hour;
	}
	
	public int getMinute(){
		return minute;
	}
	
	public long getHabitId(){
		if(!hasValidHabitId()) throw new IllegalStateException("this reminder object does not have a valid habit Id");
		else return habitId;
	}
	
	public long getId(){
		if(!hasValidId()) throw new IllegalStateException("this reminder object does not have a valid Id");
		else return id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeLong(habitId);
		out.writeInt(hour);
		out.writeInt(minute);
		
	}
	
	public static final Parcelable.Creator<Reminder> CREATOR = new Parcelable.Creator<Reminder>() {
		@Override
		public Reminder createFromParcel(Parcel in){
			return new Reminder(in);
		}

		@Override
		public Reminder[] newArray(int size) {
			return new Reminder[size];
		}
	};
	
	private Reminder(Parcel in){
		this.id = in.readLong();
		this.habitId = in.readLong();
		this.hour = in.readInt();
		this.minute = in.readInt();
	}
	
	public static void setNextReminderAlarm(){
		Intent intent = new Intent(MyApplication.getContext(), AppService.class);
		intent.setAction(AppService.ACTION_SET_NEXT_REMINDER_ALARM);
		MyApplication.getContext().startService(intent);
	}
	
	/*
	 * returns true if the reminder time has already passed the current time
	 * */
	public boolean hasPassedAsOfNow(){
		boolean result = false;
		Calendar now = Calendar.getInstance();
		
		Calendar reminder = (Calendar) now.clone();
		reminder.set(Calendar.HOUR_OF_DAY, hour);
		reminder.set(Calendar.MINUTE, minute);
		reminder.set(Calendar.SECOND, 0);
		if(reminder.compareTo(now) == -1 ) result = true;
		
		return result;
	}
	
	/*
	 * Function to merge the reminder time to a provided date. 
	 * It returns a calendar object with the date part of the parameter and
	 * the time part from this reminder object*/
	public Calendar getWithDate(Calendar calendar){
		Calendar x = (Calendar) calendar.clone();
		x.set(Calendar.HOUR_OF_DAY, hour);
		x.set(Calendar.MINUTE, minute);
		x.set(Calendar.SECOND, 0);
		x.set(Calendar.MILLISECOND, 0);
		return x;
	}
	
	public boolean hasValidId(){
		if(id == DUMMY_VALUE) return false;
		else return true;
	}
	
	public boolean hasValidHabitId(){
		if(habitId == DUMMY_VALUE) return false;
		else return true;
	}
	
}