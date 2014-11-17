package com.theAlternate.lifrack;

import java.util.Date;

public class Hit{

	private long id;
	private long habitId;
	private Date hitTime;
	private static final long DUMMY_VALUE = -1;
	
	//constructor for hit existing in DB
	public Hit(long id, long habitId, Date hitTime){
		this.id = id;
		this.habitId = habitId;
		this.hitTime = hitTime;
	}
	
	//constructor to create new hit NOT existing in DB
	public Hit(long habitId, Date hitTime){
		this.id = DUMMY_VALUE;
		this.habitId = habitId;
		this.hitTime = hitTime;
	}
	
	public boolean hasValidId(){
		if(id == DUMMY_VALUE) return false;
		else return true;
	}
	
	public long getId(){
		if(!hasValidId()) throw new IllegalStateException("this hit object does not have a valid id");
		return id;
	}
	
	public long getHabitId(){
		return habitId;
	}
	
	public Date getHitTime(){
		return hitTime;
	}
	
	public String getDurationFromNow(){
		return Utilities.getDurationFromNow(hitTime);
	}
	
}