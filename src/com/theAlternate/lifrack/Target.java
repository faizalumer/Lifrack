package com.theAlternate.lifrack;

import java.util.Date;

public class Target{
	private long id;
	private long habitId;
	private String description;
	private Date createdTime;
	private Date achievedTime;
	private final static long DUMMY_VALUE = -1;
	
	//constructor to create object from existing record
	public Target(long id, long habitId, String description, Date createdTime, Date achievedTime){
		this.id = id;
		this.habitId = habitId;
		this.description = description;
		this.createdTime = createdTime;
		this.achievedTime = achievedTime;
	}
	
	//constructor to create new object from non-existing record
	public Target(long habitId, String description, Date createdTime, Date achievedTime){
		this.id = DUMMY_VALUE;
		this.habitId = habitId;
		this.description = description;
		this.createdTime = createdTime;
		this.achievedTime = achievedTime;
	}
	
	public long getId(){
		if(!hasValidId()) throw new IllegalStateException("This target does not have a valid Id");
		return this.id;
	}
	
	public long gethabitId(){
		if(!hasValidHabitId()) throw new IllegalStateException("This target does not have a valid Habit Id");
		return this.habitId;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public Date getCreatedTime(){
		return this.createdTime;
	}
	
	public Date getAchievedTime(){
		if(!isAchieved()) throw new IllegalStateException("This target has not been achieved yet");
		return this.achievedTime;
	}
	
	public boolean hasValidId(){
		if(this.id == DUMMY_VALUE) return false;
		else return true;
	}
	
	public boolean hasValidHabitId(){
		if(this.habitId == DUMMY_VALUE) return false;
		else return true;
	}
	
	public void setDescription(String description){
		if(description==null || description.equals("")) throw new IllegalArgumentException("Target description cannot be null/empty");
		this.description = description;
	}
	
	public boolean isAchieved(){
		if(achievedTime == null) return false;
		else return true;
	}
	
	public void achieve(){
		if(isAchieved()) throw new IllegalStateException("This Target had already been achieved");
		this.achievedTime = new Date();
	}
	
	
}