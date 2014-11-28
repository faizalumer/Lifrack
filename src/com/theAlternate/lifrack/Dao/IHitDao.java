package com.theAlternate.lifrack.Dao;

import com.theAlternate.lifrack.Hit;

public interface IHitDao{
	public long insert(Hit hit);
	public void deleteByHabitId(long habitId);
	public boolean delete(long[] hitId);
	public Hit getLastByHabitId(long habitId);
}