package com.theAlternate.lifrack;

public interface HabitDao{
	public long insert(Habit habit);
	public int delete(long habitId);
	public Habit get(long habitId);
	public void update(Habit habit);
	
}