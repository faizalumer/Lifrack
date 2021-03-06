package com.theAlternate.lifrack.Dao;

public interface IHabitOperationsManagerDao{
	public boolean deleteHabit(long habitId);
	public boolean archiveHabit(long habitId);
	public boolean deleteArchivedHabit(long habitId);
	public boolean unarchiveHabit(long habitId);
	public boolean achieveTarget(long targetId);
	public boolean unachieveTarget(long targetId);
	
}