package com.theAlternate.lifrack.Dao;

import java.util.List;

import com.theAlternate.lifrack.Reminder;

//DAO interface for the Reminder entity
public interface IReminderDao{
	public void delete(long reminderId);
	public void deleteByHabitId(long habitId);
	public void update(Reminder reminder);
	public void insert(Reminder reminder);
	public Reminder get(long reminderId);
	public List<Reminder> getAllReminders();
	public List<Reminder> getAllRemindersByHabitId(long habitId);
}