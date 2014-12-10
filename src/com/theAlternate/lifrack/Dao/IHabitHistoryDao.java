package com.theAlternate.lifrack.Dao;

import com.theAlternate.lifrack.Habit;

public interface IHabitHistoryDao{
	//public long insert(Habit habit);
	public int delete(long habitId);
	public Habit get(long habitId);
	//public void update(Habit habit);
	
}