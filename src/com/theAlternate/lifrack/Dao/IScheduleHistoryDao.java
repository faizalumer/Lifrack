package com.theAlternate.lifrack.Dao;

public interface IScheduleHistoryDao{
	//public void insert(Schedule schedule);
	//public void update(Schedule schedule);
	//public List<Schedule> getAllActiveSchedules();
	//public Schedule getActiveScheduleByHabitId(long habitId);
	//public Schedule getLatestScheduleByHabitId(long habitId);
	//public void updateInvalidatedTime(Schedule schedule);
	public void deleteByHabitId(long habitId);
}