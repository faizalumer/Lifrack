package com.theAlternate.lifrack;

public interface HitDao{
	public long insert(Hit hit);
	public void deleteByHabitId(long habitId);
	public boolean delete(long[] hitId);
	public Hit getLastByHabitId(long habitId);
}