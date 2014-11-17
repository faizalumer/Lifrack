package com.theAlternate.lifrack;

import java.util.List;


public interface ScheduleDao{
	public void insert(Schedule schedule);
	public void update(Schedule schedule);
	public List<Schedule> getAllActiveSchedules();
	public Schedule getActiveScheduleByHabitId(long habitId);
	public Schedule getLatestScheduleByHabitId(long habitId);
	public void updateInvalidatedTime(Schedule schedule);
	public void deleteByHabitId(long habitId);
}