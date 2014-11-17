package com.theAlternate.lifrack;

public interface TargetDao{
	public void insert(Target target);
	public void delete(long targetId);
	public void deleteByHabitId(long habitId);
	public void updateDescription(Target target);
	public void updateAchievedTime(Target target);
	public void getAll();
	public void get(long targetId);
}