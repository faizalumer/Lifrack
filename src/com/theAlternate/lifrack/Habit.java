package com.theAlternate.lifrack;

import java.util.Date;

public class Habit{
	private static final long DUMMY_VALUE = -1;
	private long id;
	private Date createdTime;
	private String name;
	private Date archivedTime;
	
	//constructor to create object from existing habit in local DB
	public Habit(long id, String name, Date createdTime){
		this.id = id;
		this.name = name;
		this.createdTime = createdTime;
		this.archivedTime = null;
	}
	
	//constructor to create object from existing archived habit in local DB
	public Habit(long id, String name, Date createdTime, Date archivedTime){
		this.id = id;
		this.name = name;
		this.createdTime = createdTime;
		this.archivedTime = archivedTime;
	}
	
	//constructor to create object NOT existing in local DB
	public Habit(String name, Date createdTime){
		this.id = DUMMY_VALUE;
		this.name = name;
		this.createdTime = createdTime;
		this.archivedTime = null;
	}
	
	public boolean hasValidId(){
		if(id == DUMMY_VALUE) return false;
		else return true;
	}
	
	public long getId(){
		if(!hasValidId()) throw new IllegalStateException("this habit object does not have a valid ID");
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public Date getCreatedTime(){
		return createdTime;
	}
	
	public Date getArchivedTime(){
		return archivedTime;
	}
	
}