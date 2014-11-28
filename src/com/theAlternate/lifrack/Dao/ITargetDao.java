package com.theAlternate.lifrack.Dao;

import com.theAlternate.lifrack.Target;

public interface ITargetDao{
	public void insert(Target target);
	public void delete(long targetId);
	public void deleteByHabitId(long habitId);
	public void update(Target target);
}