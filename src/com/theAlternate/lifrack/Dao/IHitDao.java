package com.theAlternate.lifrack.Dao;

import java.util.List;

import com.theAlternate.lifrack.DayCount;
import com.theAlternate.lifrack.Hit;

public interface IHitDao{
	public long insert(Hit hit);
	public void deleteByHabitId(long habitId);
	public boolean delete(long[] hitId);
	public Hit getLastByHabitId(long habitId);
	public List<DayCount> getHitCountByHabitId(long habitId,int month);
}