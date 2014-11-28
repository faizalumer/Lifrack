package com.theAlternate.lifrack.Dao;

import java.util.List;

import com.theAlternate.lifrack.Schedule;


public interface IScheduleDao{
	public void insert(Schedule schedule);
	public void update(Schedule schedule);
	public List<Schedule> getAllActiveSchedules();
	public Schedule getActiveScheduleByHabitId(long habitId);
	public Schedule getLatestScheduleByHabitId(long habitId);
	public void deleteByHabitId(long habitId);
}