package com.theAlternate.lifrack.Dao;

import java.util.List;

import com.theAlternate.lifrack.DayCount;

public interface IHitHistoryDao{
	//public long insert(Hit hit);
	public void deleteByHabitId(long habitId);
	public List<DayCount> getHitCountByHabitId(long habitId,int month);
}