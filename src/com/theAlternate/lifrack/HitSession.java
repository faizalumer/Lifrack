package com.theAlternate.lifrack;

import java.util.Date;

public class HitSession{

	private long id;
	private Long hitId;
	private long habitId;
	private Date startTime;
	private Date endTime;
	private static final long DUMMY_VALUE = -1;
	
	//constructor for hit existing in DB
	public HitSession(long id, Long hitId, long habitId, Date startTime, Date endTime){
		this.id = id;
		this.hitId = hitId;
		this.habitId = habitId;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	//constructor to create new hit session NOT existing in DB.
	public HitSession(long habitId, Date startTime){
		this.id = DUMMY_VALUE;
		this.hitId = null;
		this.habitId = habitId;
		this.startTime = startTime;
		this.endTime = null;
	}
	
	public boolean hasValidId(){
		if(id == DUMMY_VALUE) return false;
		else return true;
	}
	
	public Date getStartTime(){
		return startTime;
	}
	
	public Date getEndTime(){
		return endTime;
	}
	
	public long getId(){
		if(!hasValidId()) throw new IllegalStateException("this hitsession object does not have a valid id");
		return id;
	}
	
	public long getHabitId(){
		return habitId;
	}
	
	public Long getHitId(){
		return hitId;
	}
	
	public boolean hasEnded(){
		if(endTime == null) return false;
		else return true;
	}
	
	public void endSession(long hitId, Date endTime){
		if(hasEnded()) throw new IllegalStateException("this hit session has already ended");
		this.hitId = hitId;
		this.endTime = endTime;
	}
	
}