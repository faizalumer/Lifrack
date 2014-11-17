package com.theAlternate.lifrack;

import android.content.ContentValues;

public class HabitEditableContent{
	
	public ContentValues values;
	public Reminder[] reminders;
	public String habitName;
	public Schedule schedule;
	
	public HabitEditableContent(String habitName, Schedule schedule,Reminder[] reminders){
		//public HabitEditableContent(String habitName,String targetDescription,Schedule habitSchedule,ContentValues reminders){
		this.habitName = habitName;
		this.schedule = schedule;
		this.reminders = reminders;
		
	}
	
}