package com.theAlternate.lifrack.Dao;

import com.theAlternate.lifrack.HitSession;

public interface IHitSessionDao{
	public void insert(HitSession hitSession);
	public void delete(long hitSessionId);
	public void deleteByHabitId(long habitId);
	public void update(HitSession hitSession);
}