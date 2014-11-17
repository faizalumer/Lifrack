package com.theAlternate.lifrack;

public interface HitSessionDao{
	public void insert(HitSession hitSession);
	public void delete(long hitSessionId);
	public void deleteByHabitId(long habitId);
	public void update(HitSession hitSession);
}