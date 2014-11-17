package com.theAlternate.lifrack;

public class HitEffort{
	int hitCount;
	int dayCount;
	
	public HitEffort(int hitCount, int dayCount){
		this.hitCount = hitCount;
		this.dayCount = dayCount;
	}
	
	@Override
	public String toString(){
		return String.valueOf(hitCount) + (hitCount == 1 ? " hit" : " hits") + " on " + String.valueOf(dayCount) + (dayCount == 1 ? " day" : " days");
	}
}